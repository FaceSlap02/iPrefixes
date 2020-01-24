package me.activated.prefixes.listener;

import lombok.AllArgsConstructor;
import me.activated.prefixes.Prefixes;
import me.activated.prefixes.menu.InventorySlot;
import me.activated.prefixes.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

@AllArgsConstructor
public class MenuListener implements Listener {

    private final Prefixes plugin;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Menu menu = Menu.players.get(player.getUniqueId());

        if (menu == null) return;

        event.setCancelled(true);

        if (event.getSlot() != event.getRawSlot()) return;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            InventorySlot slot = menu.getSlotByNumber(event.getSlot());

            if (slot == null) return;

            slot.onClick(player, event.getSlot(), event.getClick());
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Menu menu = Menu.players.get(player.getUniqueId());

        if (menu == null) return;

        Menu.players.remove(player.getUniqueId());
    }
}
