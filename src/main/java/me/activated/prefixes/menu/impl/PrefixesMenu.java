package me.activated.prefixes.menu.impl;

import lombok.AllArgsConstructor;
import me.activated.prefixes.Prefixes;
import me.activated.prefixes.data.PlayerData;
import me.activated.prefixes.impl.Prefix;
import me.activated.prefixes.impl.PrefixWeight;
import me.activated.prefixes.menu.InventorySlot;
import me.activated.prefixes.menu.Menu;
import me.activated.prefixes.utilities.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixesMenu extends Menu {

    private Comparator<Prefix> PREFIX_COMPRATOR = Comparator.comparingInt(Enum::ordinal);

    @Override
    public String getTitle() {
        return "Prefixes";
    }

    @Override
    public List<InventorySlot> getInventorySlots() {
        AtomicInteger order = new AtomicInteger();
        return Stream.of(Prefix.values()).sorted(PREFIX_COMPRATOR).map(prefix -> new PrefixSlot(order.getAndIncrement(), prefix)).collect(Collectors.toList());
    }

    @AllArgsConstructor
    private class PrefixSlot extends InventorySlot {

        private int slot;
        private Prefix prefix;

        @Override
        public int get() {
            return slot;
        }

        @Override
        public int[] getMultiple() {
            return null;
        }

        @Override
        public ItemStack getItem(Player player) {
            ItemStack item = new ItemStack(Material.WOOL);
            item.setDurability((short) (player.hasPermission(prefix.getPermission()) ? 5 : 7));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(CC.translate(prefix.getDisplayName().replace("<format>", prefix.getFormat())).replace("<weight>", prefix.getPrefixWeight().getName()));

            List<String> lore = new ArrayList<>();

            if (!player.hasPermission(prefix.getPermission())) {
                if (prefix.isPurchasable()) {
                    if (prefix.getPrefixWeight() == PrefixWeight.LIMITED) {
                        lore.add("&eLimited time only. &a" + Prefixes.STORE_URL + "&e.");
                        lore.add("&ePermament and Global prefix.");
                    } else {
                        lore.add("&7Purchase now. &a" + Prefixes.STORE_URL + "&e.");
                        lore.add("&7Permament and Global prefix.");
                    }
                } else {
                    lore.add("&7This prefix is unavailable and unpurchaseable.");
                }
            } else {
                lore.add("&aClick to apply!");
            }

            itemMeta.setLore(CC.translate(lore));
            item.setItemMeta(itemMeta);

            return item;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (player.hasPermission(prefix.getPermission()) || player.hasPermission("prefixes.all")) {
                PlayerData playerData = Prefixes.INSTANCE.getPlayerData().get(player.getUniqueId());
                playerData.setPrefix(prefix);

                player.closeInventory();
                player.sendMessage(CC.translate("&ePrefix " + prefix.getFormat() + " &ehas been applied!"));
            }
        }
    }
}
