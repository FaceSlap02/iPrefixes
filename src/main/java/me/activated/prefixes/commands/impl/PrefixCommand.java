package me.activated.prefixes.commands.impl;

import me.activated.prefixes.Prefixes;
import me.activated.prefixes.commands.BaseCommand;
import me.activated.prefixes.menu.impl.PrefixesMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixCommand extends BaseCommand {

    public PrefixCommand(Prefixes plugin, String name) {
        super(plugin, name);

        this.setAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        new PrefixesMenu().open((Player) sender);
    }
}
