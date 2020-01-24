package me.activated.prefixes;

import lombok.Getter;
import me.activated.prefixes.commands.BaseCommand;
import me.activated.prefixes.commands.impl.PrefixCommand;
import me.activated.prefixes.data.PlayerData;
import me.activated.prefixes.listener.MenuListener;
import me.activated.prefixes.listener.PlayerListener;
import me.activated.prefixes.utilities.ConfigurationFile;
import me.activated.prefixes.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Getter
public class Prefixes extends JavaPlugin {

    public static Prefixes INSTANCE;
    public static String STORE_URL = "store.example.com";

    private ConfigurationFile configuration;
    private Connection connection;

    private Map<UUID, PlayerData> playerData = new HashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.configuration = new ConfigurationFile(this, "config.yml");

        this.setupStorage();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        this.setupCommands();

        CC.log(" ");
        CC.log("&ePrefix plugin has been enabled.");
        CC.log("&eVersion&7: &v" + this.getDescription().getVersion());
        CC.log(" ");
    }

    private void setupCommands() {
        Arrays.asList(
                new PrefixCommand(this, "prefix")
        ).forEach(BaseCommand::register);
    }

    private void setupStorage() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" +
                            configuration.getString("mysql.database.host") + ":" +
                            configuration.getInt("mysql.database.port") + "/" +
                            configuration.getString("mysql.database.database") +
                            "?characterEncoding=latin1&useConfigs=maxPerformance",
                    configuration.getString("mysql.database.user"),
                    configuration.getString("mysql.database.password"));
            CC.log("&aMySQL has been connected.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            CC.log("&cThere was an error trying to connect to your MySQL Database, Prefixes will be disabled!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        PreparedStatement statement;

        try {
            statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata("
                    + "uuid VARCHAR(64) NOT NULL,"
                    + "name VARCHAR(16) NOT NULL,"
                    + "nameLowerCase VARCHAR(16) NOT NULL,"
                    + "prefix VARCHAR(64) DEFAULT NULL"
                    + ")");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close(AutoCloseable... closeables) {
        Arrays.stream(closeables).filter(Objects::nonNull).forEach(autoCloseable -> {
            try {
                autoCloseable.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
