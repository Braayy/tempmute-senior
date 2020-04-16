package braayy.tempmute.listener;

import braayy.tempmute.TempmutePlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TempmuteListener implements Listener {

    private TempmutePlugin main;

    private final String muteMessage;

    public TempmuteListener(TempmutePlugin main) {
        this.main = main;

        this.muteMessage = main.getConfig().getString("messages.muted").replace('&', ChatColor.COLOR_CHAR);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (main.getCacheService().isMuted(e.getPlayer())) {
            long end = main.getCacheService().getEnd(e.getPlayer());
            long diff = end - System.currentTimeMillis() / 1000;

            if (diff > 0) {
                int minutes = (int) (diff / 60);
                int seconds = (int) (diff % 60);

                e.setCancelled(true);
                e.getPlayer().sendMessage(this.muteMessage.replace("{minutes}", String.valueOf(minutes)).replace("{seconds}", String.valueOf(seconds)));
            } else {
                main.getCacheService().unmute(e.getPlayer());
            }
        }
    }

}