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

    private MethodContainer container;

    private static final String RIGHT = "\"}";
    private static final String LEFT = "{\"text\":\"";

    private Method playerHandler;

    private final SternalBoardPlugin sternalBoardPlugin;

    public LegacyTabExecutor(SternalBoardPlugin sternalBoardPlugin) {
        this.sternalBoardPlugin = sternalBoardPlugin;

        generate();
    }

    public void generate() {
        if (!baseComponent.exists()) {
            return;
        }

        container = MethodContainer.builder(
                MethodData.build(
                        baseComponent.getResult().getDeclaredClasses()[0],
                        MethodData.SearchMethod.DECLARED,
                        0,
                        "a",
                        String.class
                )
        );

        if (player.exists()) {
            try {
                playerHandler = player.getResult().getDeclaredMethod("getHandle");
            } catch (Exception ignored) {

            }
        } else {
            playerHandler = null;
        }
    }

    private Object buildComponent(String text) {
        if (container.exists()) {
            return container.execute(
                    null,
                    LEFT + text + RIGHT
            );
        }
        return null;
    }

    @Override
    public void sendTab(Player player, String headerText, String footerText) {
        if (!packet.exists() || playerHandler == null) {
            return;
        }

        String tempHeader;
        String tempFooter;

        tempHeader = TextUtils.processPlaceholders(sternalBoardPlugin, player, check(headerText)
        );
        tempFooter = TextUtils.processPlaceholders(sternalBoardPlugin, player, check(footerText));

        try {
            Object header = buildComponent(tempHeader);
            Object footer = buildComponent(tempFooter);

            Object packet = this.packet.getResult().getConstructor().newInstance();

            Field aField;
            Field bField;

            try {
                aField = packet.getClass().getDeclaredField("a");
                bField = packet.getClass().getDeclaredField("b");
            } catch (Exception ex) {
                aField = packet.getClass().getDeclaredField("header");
                bField = packet.getClass().getDeclaredField("footer");
            }

            aField.setAccessible(true);
            aField.set(packet, header);

            bField.setAccessible(true);
            bField.set(packet, footer);

            sendPacket(player, packet);
        } catch (Exception ignored) {

        }
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
    public CompletableFuture<Void> sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
        return CompletableFuture.runAsync(() -> sendPacketSync(player, packets));
    }

    /**
     * Sends a packet to the player synchronously if they're online.
     *
     * @param player  the player to send the packet to.
     * @param packets the packets to send.
     *
     * @see #sendPacket(Player, Object...)
     * @since 2.0.0
     */
    public void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
        for (Object packet : packets) {
            sendDirectPacket(player, packet);
        }
    }

    public void sendDirectPacket(Player player, Object packet) {
        try {
            Object connection = playerHandler.invoke(getCraftPlayer(player));

            Field playerConnectionField = connection.getClass().getDeclaredField("playerConnection");

            Object obtainConnection = playerConnectionField.get(connection);

            Method sendPacket = obtainConnection.getClass().getDeclaredMethod("sendPacket", Presets.PACKET.getResult());

            sendPacket.invoke(
                    obtainConnection,
                    packet
            );
        } catch (Exception ignored) {}
    }

    public Object getCraftPlayer(Player player) {
        return this.player.getResult().cast(player);
    }
}
