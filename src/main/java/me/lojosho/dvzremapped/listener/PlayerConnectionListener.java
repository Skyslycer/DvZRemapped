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
        if (!Game.isGameStart()) {
            Game.hideBossBar(player);
        }
        // Player should now be able to carry over connections
        if (!Users.contains(event.getPlayer().getUniqueId())) {
            System.out.println("a");
            User user = new User(event.getPlayer());
            Users.add(user);
            user.reset();
        } else {
            var user = Users.get(event.getPlayer().getUniqueId());
            System.out.println(user.getStatus());
            if (user.getStatus() == UserStatus.LIMBO) {
                user.reset();
            } else if (user.getLogoutLocation() != null) {
                System.out.println("hmm");
                user.getPlayer().teleport(user.getLogoutLocation());
                return;
            }
        }

        if (Game.getStatus() == GameStatus.RUNNING) {
            if (Game.isMonsterReleased()) {
                PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Monsters.getRandomMonsterClasses()));
            } else {
                System.out.println("what");
                PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Dwarves.getRandomDwarfClasses()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<GRAY>(<RED>-<GRAY>) " + event.getPlayer().getName()));
        if (Users.contains(event.getPlayer().getUniqueId())) {
            User user = Users.get(event.getPlayer().getUniqueId());
            user.setLogoutLocation(event.getPlayer().getLocation());;
        }
    }
}
