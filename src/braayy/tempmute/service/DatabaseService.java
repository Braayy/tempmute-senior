package braayy.tempmute.service;

import braayy.tempmute.TempmutePlugin;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class DatabaseService {

    private final TempmutePlugin main;

    private final String host;
    private final String user;
    private final String pass;
    private final String database;
    private final int port;

    private Connection connection;

    public DatabaseService(TempmutePlugin main) {
        this.main = main;

        this.host = main.getConfig().getString("database.host");
        this.user = main.getConfig().getString("database.user");
        this.pass = main.getConfig().getString("database.pass");
        this.database = main.getConfig().getString("database.database");
        this.port = main.getConfig().getInt("database.port");
    }

    public void setup() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + port + "/" + this.database, this.user, this.pass);

            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS tempmutes(uuid varchar(36) not null, end bigint not null, primary key(uuid))");

            this.main.getLogger().info("MySQL connected with success");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void mute(OfflinePlayer player, long end) {
        try (PreparedStatement stat = this.connection.prepareStatement("INSERT INTO tempmutes VALUES(?, ?) ON DUPLICATE KEY UPDATE end = ?")) {
            stat.setString(1, player.getUniqueId().toString());
            stat.setLong(2, end);
            stat.setLong(3, end);
            stat.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void unmute(OfflinePlayer player) {
        try (PreparedStatement stat = this.connection.prepareStatement("DELETE FROM tempmutes WHERE uuid = ?")) {
            stat.setString(1, player.getUniqueId().toString());
            stat.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void unmute(UUID uuid) {
        try (PreparedStatement stat = this.connection.prepareStatement("DELETE FROM tempmutes WHERE uuid = ?")) {
            stat.setString(1, uuid.toString());
            stat.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateCache(CacheService cacheService) {
        try (PreparedStatement stat = this.connection.prepareStatement("SELECT * FROM tempmutes")) {
            ResultSet set = stat.executeQuery();
            while (set.next()) {
                UUID uuid = UUID.fromString(set.getString("uuid"));
                long end = set.getLong("end");
                int diff = (int) (end - System.currentTimeMillis() / 1000);

                if (diff <= 0) {
                    unmute(uuid);
                } else {
                    cacheService.mute(uuid, end);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}