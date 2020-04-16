package braayy.tempmute.service;

import braayy.tempmute.TempmutePlugin;
import org.bukkit.OfflinePlayer;

import java.time.Instant;
import java.util.*;

public class CacheService {

    private final TempmutePlugin main;

    private final Map<UUID, Long> cache;

    public CacheService(TempmutePlugin main) {
        this.main = main;

        this.cache = new HashMap<>();
    }

    public void mute(OfflinePlayer player, long end) {
        this.cache.put(player.getUniqueId(), end);

        main.getDatabaseService().mute(player, end);
    }

    void mute(UUID uuid, long end) {
        this.cache.put(uuid, end);
    }

    public boolean isMuted(OfflinePlayer player) {
        long end = this.cache.getOrDefault(player.getUniqueId(), (long) -1);

        return end > 0;
    }

    public void unmute(OfflinePlayer player) {
        this.cache.remove(player.getUniqueId());

        main.getDatabaseService().unmute(player);
    }

    public long getEnd(OfflinePlayer player) {
        return this.cache.getOrDefault(player.getUniqueId(), (long) -1);
    }
}