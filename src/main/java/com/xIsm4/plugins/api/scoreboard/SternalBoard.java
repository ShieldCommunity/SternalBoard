package com.xIsm4.plugins.api.scoreboard;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import com.xIsm4.plugins.utils.compatibility.ReflectionUtils;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SternalBoard {

    private static final VersionType VERSION_TYPE;

    // Packets sending
    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;
    private static final Method PLAYER_GET_HANDLE;

    // Chat components
    private static final Class<?> CHAT_COMPONENT_CLASS;
    private static final Method MESSAGE_FROM_STRING;

    // Scoreboard packets
    private static final Constructor<?> PACKET_SB_OBJ;
    private static final Constructor<?> PACKET_SB_DISPLAY_OBJ;
    private static final Constructor<?> PACKET_SB_SCORE;
    private static final Constructor<?> PACKET_SB_TEAM;

    // Scoreboard enums
    private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
    private static final Class<?> ENUM_SB_ACTION;
    private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
    private static final Object ENUM_SB_ACTION_CHANGE;
    private static final Object ENUM_SB_ACTION_REMOVE;

    static {
        try {
            if (ReflectionUtils.nmsOptionalClass("ScoreboardServer$Action").isPresent()) {
                VERSION_TYPE = VersionType.V1_13;
            } else if (ReflectionUtils.nmsOptionalClass("IScoreboardCriteria$EnumScoreboardHealthDisplay").isPresent()) {
                VERSION_TYPE = VersionType.V1_8;
            } else {
                VERSION_TYPE = VersionType.V1_7;
                
            }

            Class<?> craftChatMessageClass = ReflectionUtils.obcClass("util.CraftChatMessage");
            Class<?> entityPlayerClass = ReflectionUtils.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = ReflectionUtils.nmsClass("PlayerConnection");
            Class<?> craftPlayerClass = ReflectionUtils.obcClass("entity.CraftPlayer");
            boolean sbTeamClass = VersionType.V1_17.isHigherOrEqual();

            MESSAGE_FROM_STRING = craftChatMessageClass.getDeclaredMethod("fromString", String.class);
            CHAT_COMPONENT_CLASS = ReflectionUtils.nmsClass("IChatBaseComponent");

            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = entityPlayerClass.getDeclaredField("playerConnection");
            SEND_PACKET = playerConnectionClass.getDeclaredMethod("sendPacket", ReflectionUtils.nmsClass("Packet"));

            PACKET_SB_OBJ = ReflectionUtils.nmsClass("PacketPlayOutScoreboardObjective").getConstructor();
            PACKET_SB_DISPLAY_OBJ = ReflectionUtils.nmsClass("PacketPlayOutScoreboardDisplayObjective").getConstructor();
            PACKET_SB_SCORE = ReflectionUtils.nmsClass("PacketPlayOutScoreboardScore").getConstructor();
            PACKET_SB_TEAM = ReflectionUtils.nmsClass("PacketPlayOutScoreboardTeam").getConstructor();

            if (VersionType.V1_8.isHigherOrEqual()) {
                String enumSbActionClass = VersionType.V1_13.isHigherOrEqual()
                        ? "ScoreboardServer$Action"
                        : "PacketPlayOutScoreboardScore$EnumScoreboardAction";
                ENUM_SB_HEALTH_DISPLAY = ReflectionUtils.nmsClass("IScoreboardCriteria$EnumScoreboardHealthDisplay");
                ENUM_SB_ACTION = FastReflection.nmsClass(enumSbActionClass);
                ENUM_SB_HEALTH_DISPLAY_INTEGER = ReflectionUtils.enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER");
                ENUM_SB_ACTION_CHANGE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "CHANGE");
                ENUM_SB_ACTION_REMOVE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "REMOVE");
            } else {
                ENUM_SB_HEALTH_DISPLAY = null;
                ENUM_SB_ACTION = null;
                ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
                ENUM_SB_ACTION_CHANGE = null;
                ENUM_SB_ACTION_REMOVE = null;
            }
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Player player;
    private final String id;

    private String title = ChatColor.RESET.toString();
    private final List<String> lines = new ArrayList<>();

    private boolean deleted = false;

    /**
     * Creates a new SternalBoard.
     *
     * @param player the player the scoreboard is for
     */
    public SternalBoard(Player player) {
        this.player = Objects.requireNonNull(player, "player");

        this.id = "sb-" + Double.toString(Math.random()).substring(2, 10);

        try {
            sendObjectivePacket(ObjectiveMode.CREATE);
            sendDisplayObjectivePacket();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the scoreboard title.
     *
     * @return the scoreboard title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Update the scoreboard title.
     *
     * @param title the new scoreboard title
     * @throws IllegalArgumentException if the title is longer than 32 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    public void updateTitle(String title) {
        if (this.title.equals(Objects.requireNonNull(title, "title"))) {
            return;
        }

        if (!VersionType.V1_13.isHigherOrEqual() && title.length() > 32) {
            title = title.substring(0, 32);
        }

        this.title = PlaceholderUtils.colorize(title);

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the scoreboard lines.
     *
     * @return the scoreboard lines
     */
    public List<String> getLines() {
        return new ArrayList<>(this.lines);
    }

    /**
     * Get the specified scoreboard line.
     *
     * @param line the line number
     * @return the line
     * @throws IndexOutOfBoundsException if the line is higher than {@code size}
     */
    public String getLine(int line) {
        checkLineNumber(line, true, false);

        return this.lines.get(line);
    }

    /**
     * Update a single scoreboard line.
     *
     * @param line the line number
     * @param text the new line text
     * @throws IndexOutOfBoundsException if the line is higher than {@code size} + 1
     */
    public synchronized void updateLine(int line, String text) {
        checkLineNumber(line, false, true);

        try {
            if (line < size()) {
                this.lines.set(line, text);

                sendTeamPacket(getScoreByLine(line), TeamMode.UPDATE);
                return;
            }

            List<String> newLines = new ArrayList<>(this.lines);

            if (line > size()) {
                for (int i = size(); i < line; i++) {
                    newLines.add("");
                }
            }

            newLines.add(text);

            updateLines(newLines);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove a scoreboard line.
     *
     * @param line the line number
     */
    public synchronized void removeLine(int line) {
        checkLineNumber(line, false);

        if (line >= size()) {
            return; // The line don't exists
        }

        List<String> newLines = new ArrayList<>(this.lines);
        newLines.remove(line);
        updateLines(newLines);
    }

    /**
     * Update all the scoreboard lines.
     *
     * @param lines the new lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    public void updateLines(String... lines) {
        updateLines(Arrays.asList(lines));
    }

    /**
     * Update the lines of the scoreboard
     *
     * @param lines the new scoreboard lines
     * @throws IllegalStateException if {@link #delete()} was call before
     */
    public synchronized void updateLines(Collection<String> lines) {
        Objects.requireNonNull(lines, "lines");
        checkLineNumber(lines.size(), false, true);

        final List<String> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        final int linesSize = this.lines.size();

        if (!VersionType.V1_13.isHigherOrEqual()) {
            final ListIterator<String> iterator = this.lines.listIterator();
            while (iterator.hasNext()) {
                final String line = iterator.next();
                if (line != null && line.length() > 30) {
                    iterator.set(line.substring(0, 30));
                }
            }
        }

        try {
            if (oldLines.size() != linesSize) {
                List<String> oldLinesCopy = new ArrayList<>(oldLines);

                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);
                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);

                        oldLines.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < linesSize; i++) {
                        sendScorePacket(i, ScoreboardAction.CHANGE);
                        sendTeamPacket(i, TeamMode.CREATE);

                        oldLines.add(oldLines.size() - i, getLineByScore(i));
                    }
                }
            }

            for (int i = 0; i < linesSize; i++) {
                if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
                    sendTeamPacket(i, TeamMode.UPDATE);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the player who has the scoreboard.
     *
     * @return current player for this FastBoard
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the scoreboard id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get if the scoreboard is deleted.
     *
     * @return true if the scoreboard is deleted
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Get the scoreboard size (the number of lines).
     *
     * @return the size
     */
    public int size() {
        return this.lines.size();
    }

    /**
     * Delete this FastBoard, and will remove the scoreboard for the associated player if he is online.
     * After this, all uses of {@link #updateLines} and {@link #updateTitle} will throws an {@link IllegalStateException}
     *
     * @throws IllegalStateException if this was already call before
     */
    public void delete() {
        try {
            for (int i = 0; i < this.lines.size(); i++) {
                sendTeamPacket(i, TeamMode.REMOVE);
            }

            sendObjectivePacket(ObjectiveMode.REMOVE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        this.deleted = true;
    }
    
        /**
     * Return if the player has a prefix/suffix characters limit.
     * By default, it returns true only in 1.12 or lower.
     * This method can be overridden to fix compatibility with some versions support plugin.
     *
     * @return max length
     */
    protected boolean hasLinesMaxLength() {
        return !VersionType.V1_13.isHigherOrEqual();
    }

    private void checkLineNumber(int line, boolean checkMax, boolean checkValid) {
        if (line < 0) {
            throw new IllegalArgumentException("Line number must be positive");
        }

        if (checkMax && line >= this.lines.size()) {
            throw new IllegalArgumentException("Line number must be under " + this.lines.size());
        }
        
        if (checkValid && line >= ChatColor.values().length - 1) {
            throw new IllegalArgumentException("Line number is too high: " + this.lines.size());
        }
    }

    private String getColorCode(int score) {
        return ChatColor.values()[score].toString();
    }
    
    private int getScoreByLine(int line) {
        return this.lines.size() - line - 1;
    }

    private String getLineByScore(int score) {
        return getLineByScore(this.lines, score);
    }

    private String getLineByScore(List<String> lines, int score) {
        return lines.get(lines.size() - score - 1);
    }

    private void sendObjectivePacket(ObjectiveMode mode) throws ReflectiveOperationException {
        Object packet = PACKET_SB_OBJ.newInstance();

        setField(packet, String.class, this.id);
        setField(packet, int.class, mode.ordinal());

        if (mode != ObjectiveMode.REMOVE) {
            setComponentField(packet, this.title, 1);

            if (VersionType.V1_8.isHigherOrEqual()) {
                setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
            }
        } else if (VERSION_TYPE == VersionType.V1_7) {
            setField(packet, String.class, "", 1);
        }

        sendPacket(packet);
    }

    private void sendDisplayObjectivePacket() throws ReflectiveOperationException {
        Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

        setField(packet, int.class, 1);
        setField(packet, String.class, this.id);

        sendPacket(packet);
    }

    private void sendScorePacket(int score, ScoreboardAction action) throws ReflectiveOperationException {
        Object packet = PACKET_SB_SCORE.newInstance();

        setField(packet, String.class, getColorCode(score), 0);

        if (VersionType.V1_8.isHigherOrEqual()) {
            setField(packet, ENUM_SB_ACTION, action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE);
        } else {
            setField(packet, int.class, action.ordinal(), 1);
        }

        if (action == ScoreboardAction.CHANGE) {
            setField(packet, String.class, this.id, 1);
            setField(packet, int.class, score);
        }

        sendPacket(packet);
    }

    private void sendTeamPacket(int score, TeamMode mode) throws ReflectiveOperationException {
        if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }

        int maxLength = hasLinesMaxLength() ? 16 : 1024;
        Object packet = PACKET_SB_TEAM.newInstance();

        setField(packet, String.class, this.id + ':' + score); // Team name
        setField(packet, int.class, mode.ordinal(), VERSION_TYPE == VersionType.V1_8 ? 1 : 0); // Update mode

        if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
            String line = getLineByScore(score);
            String prefix;
            String suffix = null;

            if (line == null || line.isEmpty()) {
                prefix = getColorCode(score) + ChatColor.RESET;
            } else if (line.length() <= maxLength) {
                prefix = line;
            } else {
                // Prevent splitting color codes
                int index = line.charAt(maxLength - 1) == ChatColor.COLOR_CHAR ? (maxLength - 1) : maxLength;
                prefix = line.substring(0, index);
                String suffixTmp = line.substring(index);
                ChatColor chatColor = null;

                if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
                    chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
                }

                String color = ChatColor.getLastColors(prefix);
                boolean addColor = chatColor == null || chatColor.isFormat();

                suffix = (addColor ? (color.isEmpty() ? ChatColor.RESET.toString() : color) : "") + suffixTmp;
            }

            if (prefix.length() > maxLength || (suffix != null && suffix.length() > maxLength)) {
                // Something went wrong, just cut to prevent client crash/kick
                prefix = prefix.substring(0, maxLength);
                suffix = (suffix != null) ? suffix.substring(0, maxLength) : null;
            }

            setComponentField(packet, prefix, 2); // Prefix
            setComponentField(packet, suffix == null ? "" : suffix, 3); // Suffix
            setField(packet, String.class, "always", 4); // Visibility for 1.8+
            setField(packet, String.class, "always", 5); // Collisions for 1.9+

            if (mode == TeamMode.CREATE) {
                setField(packet, Collection.class, Collections.singletonList(getColorCode(score))); // Players in the team
            }
        }

        sendPacket(packet);
    }

    private void sendPacket(Object packet) throws ReflectiveOperationException {
        if (this.deleted || !this.player.isOnline()) {
            return;
        }
        Object entityPlayer = PLAYER_GET_HANDLE.invoke(this.player);
        Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
        SEND_PACKET.invoke(playerConnection, packet);
    }

    private void setField(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException {
        setField(object, fieldType, value, 0);
    }

    private void setField(Object object, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException {
        int i = 0;
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getType() == fieldType && count == i++) {
                field.setAccessible(true);
                field.set(object, value);
            }
        }
    }

    private void setComponentField(Object object, String value, int count) throws ReflectiveOperationException {
        if (VERSION_TYPE != VersionType.V1_13) {
            setField(object, String.class, value, count);
            return;
        }

        int i = 0;
        for (Field field : object.getClass().getDeclaredFields()) {
            if ((field.getType() == String.class || field.getType() == CHAT_COMPONENT_CLASS) && count == i++) {
                field.setAccessible(true);
                field.set(object, Array.get(MESSAGE_FROM_STRING.invoke(null, value), 0));
            }
        }
    }

    enum ObjectiveMode {

        CREATE, REMOVE, UPDATE

    }

    enum TeamMode {

        CREATE, REMOVE, UPDATE, ADD_PLAYERS, REMOVE_PLAYERS

    }

    enum ScoreboardAction {

        CHANGE, REMOVE

    }

    enum VersionType {
        V1_7, V1_8, V1_13, V1_17;

        public boolean isHigherOrEqual() {
            return VERSION_TYPE.ordinal() >= ordinal();
        }
    }
}
