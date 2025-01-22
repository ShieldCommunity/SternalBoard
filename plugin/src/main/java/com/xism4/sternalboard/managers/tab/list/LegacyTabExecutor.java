package com.xism4.sternalboard.managers.tab.list;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.TabExecutor;
import com.xism4.sternalboard.utils.TextUtils;
import me.blueslime.nmshandlerapi.SpecifiedClass;
import me.blueslime.nmshandlerapi.method.MethodContainer;
import me.blueslime.nmshandlerapi.method.MethodData;
import me.blueslime.nmshandlerapi.utils.presets.Presets;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class LegacyTabExecutor extends TabExecutor {

    private static final String JSON_LEFT = "{\"text\":\"";
    private static final String JSON_RIGHT = "\"}";

    private final SpecifiedClass baseComponent = SpecifiedClass.build(
            false,
            "[minecraft].[version].IChatBaseComponent",
            "[minecraft].IChatBaseComponent"
    );

    private final SpecifiedClass packet = SpecifiedClass.build(
            false,
            "[minecraft].[version].PacketPlayOutPlayerListHeaderFooter",
            "[minecraft].PacketPlayOutPlayerListHeaderFooter"
    );

    private final SpecifiedClass player = SpecifiedClass.build(
            false,
            "[craftbukkit].[version].entity.CraftPlayer",
            "[craftbukkit].entity.CraftPlayer",
            "[craftbukkit].CraftPlayer"
    );

    private final SternalBoardPlugin plugin;
    private MethodContainer container;
    private Method playerHandler;

    public LegacyTabExecutor(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        startMethod();
    }

    private void startMethod() {
        if (baseComponent.exists()) {
            container = MethodContainer.builder(
                    MethodData.build(
                            baseComponent.getResult().getDeclaredClasses()[0],
                            MethodData.SearchMethod.DECLARED,
                            0,
                            "a",
                            String.class
                    )
            );
        }

        if (player.exists()) {
            try {
                playerHandler = player.getResult().getDeclaredMethod("getHandle");
            } catch (Exception e) {
                playerHandler = null;
            }
        }
    }

    private Object buildComponent(String text) {
        if (container != null && container.exists()) {
            return container.execute(null, JSON_LEFT + text + JSON_RIGHT);
        }
        return null;
    }

    @Override
    public void sendTab(Player player, String headerText, String footerText) {
        if (!packet.exists() || playerHandler == null) {
            return;
        }

        String processedHeader = TextUtils.processPlaceholders(plugin, player, check(headerText));
        String processedFooter = TextUtils.processPlaceholders(plugin, player, check(footerText));

        try {
            Object headerComponent = buildComponent(processedHeader);
            Object footerComponent = buildComponent(processedFooter);

            if (headerComponent == null || footerComponent == null) {
                return; // check if components are null and avoid sending the packet
            }

            Object packet = this.packet.getResult().getConstructor().newInstance();

            setPacketFields(packet, headerComponent, footerComponent);

            sendAsyncPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setPacketFields(Object packet, Object header, Object footer) throws Exception {
        Field headerField;
        Field footerField;

        try {
            headerField = packet.getClass().getDeclaredField("a");
            footerField = packet.getClass().getDeclaredField("b");
        } catch (Exception e) {
            headerField = packet.getClass().getDeclaredField("header");
            footerField = packet.getClass().getDeclaredField("footer");
        }

        headerField.setAccessible(true);
        headerField.set(packet, header);

        footerField.setAccessible(true);
        footerField.set(packet, footer);
    }

    /**
     * Sends a packet to the player asynchronously if they're online.
     * Packets are thread-safe.
     *
     * @param player  the player to send the packet to.
     * @param packets the packets to send.
     *
     * @return the async thread handling the packet.
     * @see #sendPacketSync(Player, Object...)
     * @since 1.0.0
     */
    @Nonnull
    private CompletableFuture<Void> sendAsyncPacket(@Nonnull Player player, @Nonnull Object... packets) {
        return CompletableFuture.runAsync(() -> sendPacketSync(player, packets));
    }

    /**
     * Sends a packet to the player synchronously if they're online.
     *
     * @param player  the player to send the packet to.
     * @param packets the packets to send.
     *
     * @see #sendAsyncPacket(Player, Object...) (Player, Object...)
     * @since 2.0.0
     */
    private void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
        for (Object packet : packets) {
            sendDirectPacket(player, packet);
        }
    }

    private void sendDirectPacket(Player player, Object packet) {
        try {
            Object connection = playerHandler.invoke(getCraftPlayer(player));

            Field connectionField = connection.getClass().getDeclaredField("playerConnection");
            connectionField.setAccessible(true);

            Object playerConnection = connectionField.get(connection);

            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", Presets.PACKET.getResult());
            sendPacketMethod.setAccessible(true);

            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getCraftPlayer(Player player) {
        return this.player.getResult().cast(player);
    }
}