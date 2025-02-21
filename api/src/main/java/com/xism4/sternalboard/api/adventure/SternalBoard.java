package com.xism4.sternalboard.api.adventure;

import com.xism4.sternalboard.api.SternalBoardHandler;
import com.xism4.sternalboard.api.SternalReflection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 */
public class SternalBoard extends SternalBoardHandler<Component> {

    private static final MethodHandle COMPONENT_METHOD;
    private static final Object EMPTY_COMPONENT;
    private static final boolean ADVENTURE_SUPPORT;

    static {
        ADVENTURE_SUPPORT = SternalReflection
                .optionalClass("io.papermc.paper.adventure.PaperAdventure")
                .isPresent();
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            if (ADVENTURE_SUPPORT) {
                Class<?> paperAdventure = Class.forName("io.papermc.paper.adventure.PaperAdventure");
                Method method = paperAdventure.getDeclaredMethod("asVanilla", Component.class);
                COMPONENT_METHOD = lookup.unreflect(method);
                EMPTY_COMPONENT = COMPONENT_METHOD.invoke(Component.empty());
            } else {
                Class<?> craftChatMessageClass = SternalReflection.obcClass("util.CraftChatMessage");
                COMPONENT_METHOD = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
                EMPTY_COMPONENT = Array.get(COMPONENT_METHOD.invoke(""), 0);
            }
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SternalBoard(Player player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendLineChange(int score) throws Throwable {
        Component line = getLineByScore(score);

        sendTeamPacket(score, SternalBoardHandler.TeamMode.UPDATE, line, null);
    }

    @Override
    protected Object toMinecraftComponent(Component component) throws Throwable {
        if (component == null) {
            return EMPTY_COMPONENT;
        }

        // If the server isn't running adventure natively, we convert the component to legacy text
        // and then to a Minecraft chat component
        if (!ADVENTURE_SUPPORT) {
            String legacy = serializeLine(component);

            return Array.get(COMPONENT_METHOD.invoke(legacy), 0);
        }

        return COMPONENT_METHOD.invoke(component);
    }

    @Override
    protected String serializeLine(Component value) {
        return LegacyComponentSerializer.legacySection().serialize(value);
    }

    @Override
    protected Component emptyLine() {
        return Component.empty();
    }
}