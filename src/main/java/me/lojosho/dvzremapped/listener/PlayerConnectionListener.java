package me.lojosho.dvzremapped.listener;

import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.classes.dwarves.Dwarves;
import me.lojosho.dvzremapped.classes.monsters.Monsters;
import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.game.GameStatus;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import me.lojosho.dvzremapped.util.PlayerUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerConnectionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        event.joinMessage(MiniMessage.miniMessage().deserialize("<GRAY>(<GREEN>+<GRAY>) " + event.getPlayer().getName()));
        Player player = event.getPlayer();
        // Player should now be able to carry over connections
        if (!Users.contains(event.getPlayer().getUniqueId())) {
            User user = new User(event.getPlayer());
            Users.add(user);
            user.reset();
        } else if (Users.get(event.getPlayer().getUniqueId()).getStatus() == UserStatus.LIMBO) {
            Users.get(event.getPlayer().getUniqueId()).reset();
        } else {
            return;
        }

        if (Game.getStatus() == GameStatus.RUNNING) {
            if (Game.isMonsterReleased()) {
                PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Monsters.getRandomMonsterClasses()));
            } else {
                PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Dwarves.getRandomDwarfClasses()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<GRAY>(<RED>-<GRAY>) " + event.getPlayer().getName()));
    }
}
