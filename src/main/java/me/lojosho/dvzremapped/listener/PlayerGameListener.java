package me.lojosho.dvzremapped.listener;

import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.classes.dwarves.Dwarf;
import me.lojosho.dvzremapped.classes.dwarves.Dwarves;
import me.lojosho.dvzremapped.classes.monsters.Monster;
import me.lojosho.dvzremapped.classes.monsters.Monsters;
import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import me.lojosho.dvzremapped.util.PlayerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerGameListener implements Listener {

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material selectionMaterial = player.getInventory().getItemInMainHand().getType();
        User user = Users.get(player.getUniqueId());

        if (user == null || !Game.isGameStart()) {
            return;
        }

        if (user.getStatus().equals(UserStatus.LIMBO)) {
            if (Game.isMonsterReleased()) {
                Monster monster = Monsters.getMonsterClass(selectionMaterial);
                if (monster != null) {
                    user.turnMonster(monster);
                }
            } else {
                Dwarf dwarf = Dwarves.getDwarfClass(selectionMaterial);
                if (dwarf != null) {
                    user.turnDwarf(dwarf);
                }
            }
        } else if (user.getStatus().equals(UserStatus.DWARF)) {
            if (selectionMaterial.equals(Material.BOOK)) {
                user.runSkill();
            }
        } else if (user.getStatus().equals(UserStatus.MONSTER)) {
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.CRAFTING_TABLE) || event.getClickedBlock().getType().equals(Material.FURNACE)) {
                    event.setCancelled(true);
                }
            }
            if (event.hasItem()) {
                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DEAD_BUSH)) {
                    event.getPlayer().setHealth(0);
                    return;
                }
            }
            user.runSkill();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        User user = Users.get(player.getUniqueId());

        if (user == null) return;

        if (user.getStatus().equals(UserStatus.DWARF)) {
            if (Game.isMonsterReleased()) {
                user.reset();
                int dwarves = Users.getCounted(UserStatus.DWARF);
                if (dwarves != 0) {
                    event.deathMessage(Component.text(player.getName() + " has fallen! There are " + dwarves + " Dwarves left! ").color(NamedTextColor.RED));
                } else {
                    event.deathMessage(Component.text(player.getName() + " has fallen! The fortress is lost...").color(NamedTextColor.RED));
                }
            } else {
                event.setKeepInventory(true);
                event.getDrops().clear();
                event.deathMessage(Component.text(player.getName() + " has fallen, but will be revived! The fortress is safe for now...").color(NamedTextColor.RED));
            }
        }
        if (user.getStatus().equals(UserStatus.MONSTER)) {
            user.reset();
            event.getDrops().clear();
            if (event.getPlayer().getKiller() != null) {
                event.deathMessage(MiniMessage.miniMessage().deserialize("<GRAY>" + event.getPlayer().getKiller().getName() + " <RED>\uD83D\uDDE1 <GRAY>" + event.getPlayer().getName()));  // 🗡
            } else {
                event.deathMessage(Component.empty());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        User user = Users.get(player.getUniqueId());

        if (user == null) return;

        if (user.getStatus() == UserStatus.DWARF) {
            event.setRespawnLocation(DvZRemappedPlugin.getDwarfSpawn());
            return;
        }

        if (Game.isMonsterReleased()) {
            PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Monsters.getRandomMonsterClasses()));
        } else {
            PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Dwarves.getRandomDwarfClasses()));
        }

        event.setRespawnLocation(DvZRemappedPlugin.getJoinLocation());
    }

    @EventHandler
    public void onPlayerItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        User user = Users.get(event.getEntity().getUniqueId());
        if (user == null) return;

        if (user.getStatus().equals(UserStatus.MONSTER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        User user = Users.get(event.getPlayer().getUniqueId());
        if (user == null) return;

        if (user.getStatus().equals(UserStatus.DWARF) && event.getItemDrop().getItemStack().getType().equals(Material.BOOK)) {
            event.setCancelled(true);
        }

        if (user.getStatus().equals(UserStatus.MONSTER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attackedPlayer = ((Player) event.getDamager()).getPlayer();
            Player defendingPlayer = ((Player) event.getEntity()).getPlayer();

            User user = Users.get(attackedPlayer.getUniqueId());
            User dUser = Users.get(defendingPlayer.getUniqueId());

            if (user.getStatus().equals(UserStatus.MONSTER) && dUser.getStatus().equals(UserStatus.MONSTER)) {
                event.setCancelled(true);
            }
        }
    }
}
