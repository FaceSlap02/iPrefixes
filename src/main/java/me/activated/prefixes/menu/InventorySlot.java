package me.activated.prefixes.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public abstract class InventorySlot {

    public abstract int get();

    public abstract int[] getMultiple();

    public abstract ItemStack getItem(Player player);

    public abstract void onClick(Player player, int slot, ClickType clickType);

    public boolean hasSlot(int slot) {
        if (slot == this.get()) {
            return true;
        }
        if (this.getMultiple() == null) return false;

        return IntStream.range(0, this.getMultiple().length).filter(s -> s == slot).findAny().orElse(-69) != -69;
    }
}
