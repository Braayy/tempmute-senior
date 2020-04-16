package braayy.tempmute;

import braayy.tempmute.command.TempmuteCommand;
import braayy.tempmute.command.UnmuteCommand;
import braayy.tempmute.listener.TempmuteListener;
import braayy.tempmute.service.DatabaseService;
import braayy.tempmute.service.CacheService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TempmutePlugin extends JavaPlugin {

    private CacheService cacheService;
    private DatabaseService databaseService;

    @Override
    public void onEnable() {
        this.cacheService = new CacheService(this);
        this.databaseService = new DatabaseService(this);

        this.databaseService.setup();
        this.databaseService.populateCache(this.cacheService);

        saveDefaultConfig();

        getCommand("tempmute").setExecutor(new TempmuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getServer().getPluginManager().registerEvents(new TempmuteListener(this), this);
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }
}