package braayy.tempmute.command;

import braayy.tempmute.TempmutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnmuteCommand implements CommandExecutor {

    private TempmutePlugin main;

    private final String permission;
    private final String noPermissionMessage;
    private final String usageMessage;
    private final String errorMessage;
    private final String successMessage;

    public UnmuteCommand(TempmutePlugin main) {
        this.main = main;
        this.permission = main.getConfig().getString("tempmute-permission");
        this.noPermissionMessage = main.getConfig().getString("messages.no-permission").replace('&', ChatColor.COLOR_CHAR);
        this.usageMessage = main.getConfig().getString("messages.usage-unmute", "&colaaa").replace('&', ChatColor.COLOR_CHAR);
        this.errorMessage = main.getConfig().getString("messages.error-unmute").replace('&', ChatColor.COLOR_CHAR);
        this.successMessage = main.getConfig().getString("messages.success-unmute").replace('&', ChatColor.COLOR_CHAR);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(this.permission)) {
            sender.sendMessage(this.noPermissionMessage);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(this.usageMessage);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!main.getCacheService().isMuted(target)) {
            sender.sendMessage(this.errorMessage.replace("{player}", args[0]));
            return true;
        }

        main.getCacheService().unmute(target);
        sender.sendMessage(this.successMessage.replace("{player}", target.getName()));

        return true;
    }

}