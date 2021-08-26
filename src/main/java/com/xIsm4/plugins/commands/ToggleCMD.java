package  com.xIsm4.plugins.commands;

import com.xIsm4.plugins.Main;
import  com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleCMD implements CommandExecutor {
    private boolean toggle = true;
    private Main core;
    public ToggleCMD(Main core){
        this.core = core;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            core.getLogger().warning(ChatColor.RED+("&cYou cannot run commands from the console"));
            return false;
        }
        Player player = (Player) sender;
        if(args.length > 0){
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!player.hasPermission("sternalboard.toggle")) {
                    player.sendMessage(PlaceholderUtils.colorize("&cU don't have permissions to use this command"));
                    return false;
                }
                if (getToggle()) {
                    SternalBoard board = core.getScoreboardManager().getBoards().remove(player.getUniqueId());
                    if (board != null) {
                        board.delete();
                        setToggle(false);
                    }
                }else{
                    SternalBoard board = new SternalBoard(player);
                    if (core.getConfig().getInt("settings.scoreboard.update") > 0) {
                        core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("settings.scoreboard.title"))), 0, core.getConfig().getInt("settings.scoreboard.update", 20));
                    }

                    core.getScoreboardManager().getBoards().put(player.getUniqueId(), board);
                    setToggle(true);
                }
            }
        }
        return true;
    }
    public boolean getToggle(){
        return toggle;
    }
    public void setToggle(boolean toggle){
        this.toggle = toggle;
    }
}