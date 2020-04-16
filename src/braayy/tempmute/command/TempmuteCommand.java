package braayy.tempmute.command;

import braayy.tempmute.TempmutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class TempmuteCommand implements CommandExecutor {

    private TempmutePlugin main;

    private final String permission;
    private final String noPermissionMessage;
    private final String usageMessage;
    private final String notExistsMessage;
    private final String notNumberMessage;
    private final String successMessage;

    public TempmuteCommand(TempmutePlugin main) {
        this.main = main;
        this.permission = main.getConfig().getString("tempmute-permission");
        this.noPermissionMessage = main.getConfig().getString("messages.no-permission").replace('&', ChatColor.COLOR_CHAR);
        this.usageMessage = main.getConfig().getString("messages.usage-tempmute").replace('&', ChatColor.COLOR_CHAR);
        this.notExistsMessage = main.getConfig().getString("messages.not-exists").replace('&', ChatColor.COLOR_CHAR);
        this.notNumberMessage = main.getConfig().getString("messages.not-number").replace('&', ChatColor.COLOR_CHAR);
        this.successMessage = main.getConfig().getString("messages.success-tempmute").replace('&', ChatColor.COLOR_CHAR);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(this.permission)) {
            sender.sendMessage(this.noPermissionMessage);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(this.usageMessage);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(this.notExistsMessage.replace("{player}", args[0]));
            return true;
        }

        try {
            int amount = Integer.parseInt(args[1]);
            long end = System.currentTimeMillis() / 1000 + TimeUnit.MINUTES.toSeconds(amount);

            main.getCacheService().mute(target, end);
            sender.sendMessage(this.successMessage.replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount)));
        } catch (NumberFormatException exception) {
            sender.sendMessage(this.notNumberMessage);
        }

        return true;
    }

}