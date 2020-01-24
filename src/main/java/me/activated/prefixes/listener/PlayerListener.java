package me.activated.prefixes.listener;

import lombok.RequiredArgsConstructor;
import me.activated.prefixes.Prefixes;
import me.activated.prefixes.data.PlayerData;
import me.activated.prefixes.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final Prefixes plugin;

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        if (!this.plugin.getPlayerData().containsKey(event.getUniqueId())) {
            this.plugin.getPlayerData().put(event.getUniqueId(), new PlayerData(this.plugin, event.getName(), event.getUniqueId()));
        }

        PlayerData playerData = this.plugin.getPlayerData().get(event.getUniqueId());
        playerData.load();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());

        if (playerData.getPrefix() == null) return;

        event.setFormat(CC.translate(playerData.getPrefix().getFormat()) + ChatColor.RESET + event.getFormat());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, playerData::save);

        plugin.getPlayerData().remove(player.getUniqueId());
    }
}
