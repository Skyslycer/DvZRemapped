package me.lojosho.dvzremapped.listener;

import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.Users;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerConnectionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        event.joinMessage(MiniMessage.miniMessage().deserialize("<GRAY>(<GREEN>+<GRAY>) " + event.getPlayer().getName()));
        Player player = event.getPlayer();
        // In the future players may be able to carry over between connections
        if (!Users.contains(event.getPlayer().getUniqueId())) {
            User user = new User(event.getPlayer());
            Users.add(user);
            player.teleport(DvZRemappedPlugin.getJoinLocation());
            player.getInventory().clear();
            if (Game.isGameStart()) player.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
            return;
        }

        //User user = Users.get(event.getPlayer().getUniqueId());

        if (Game.isMonsterReleased()) {
            // Turn to monsters
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<GRAY>(<RED>-<GRAY>) " + event.getPlayer().getName()));
        Users.remove(event.getPlayer().getUniqueId());
    }
}
