package me.activated.prefixes.commands;

import lombok.Setter;
import me.activated.prefixes.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Setter
public abstract class BaseCommand extends BukkitCommand {

    public Prefixes plugin;

    private boolean playerOnly = true;
    private boolean async = false;

    private String permission = "";
    private List<String> allies = new ArrayList<>();

    public BaseCommand(Prefixes plugin, String name) {
        super(name);
        this.plugin = plugin;

        super.setAliases(this.allies);
        if (!permission.isEmpty()) {
            super.setPermission(this.permission);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player) && this.playerOnly) {
            sender.sendMessage(ChatColor.RED + "For player use only!");
            return false;
        }

        if (!sender.hasPermission(this.permission) && !this.permission.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return false;
        }

        if (this.async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> this.execute(sender, args));
        } else {
            this.execute(sender, args);
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public void register() {
        if (!(this.plugin.getServer().getPluginManager() instanceof SimplePluginManager)) return;

        SimplePluginManager manager = (SimplePluginManager) this.plugin.getServer().getPluginManager();
        SimpleCommandMap commandMap = null;

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");

            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            commandMap = (SimpleCommandMap) field.get(manager);

            field.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (commandMap != null) {
            commandMap.register(plugin.getDescription().getName(), this);
        }
    }
}
