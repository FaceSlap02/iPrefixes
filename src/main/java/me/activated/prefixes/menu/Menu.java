package me.activated.prefixes.menu;

import me.activated.prefixes.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Menu {

    public static Map<UUID, Menu> players = new HashMap<>();

    public abstract String getTitle();

    public abstract List<InventorySlot> getInventorySlots();

    public InventorySlot getSlotByNumber(int number) {
        return getInventorySlots().stream().filter(inventorySlot -> inventorySlot.hasSlot(number)).findFirst().orElse(null);
    }

    public void open(Player player) {

        String title = this.getTitle();
        if (title.length() > 32) title = title.substring(0, 32);

        Inventory inventory = Bukkit.createInventory(null, this.getSize(this.getInventorySlots()), CC.translate(title));

        this.getInventorySlots().forEach(inventorySlot -> {
            inventory.setItem(inventorySlot.get(), inventorySlot.getItem(player));

            if (inventorySlot.getMultiple() != null) {
                IntStream.range(0, inventorySlot.getMultiple().length).forEach(slot -> inventory.setItem(slot, inventorySlot.getItem(player)));
            }
        });

        player.openInventory(inventory);
        Menu.players.put(player.getUniqueId(), this);
    }

    private int getSize(List<InventorySlot> slots) {
        int highest = 0;
        if (!slots.isEmpty()) {
            highest = slots.stream().sorted(Comparator.comparingInt(InventorySlot::get).reversed()).map(InventorySlot::get).collect(Collectors.toList()).get(0);
        }

        for (InventorySlot slot : slots) {
            if (slot.getMultiple() != null) {
                for (int i = 0; i < slot.getMultiple().length; i++) {
                    if (slot.getMultiple()[i] > highest) {
                        highest = slot.getMultiple()[i];
                    }
                }
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }
}
