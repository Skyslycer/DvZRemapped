package me.lojosho.dvzremapped.user;

import me.libraryaddict.disguise.DisguiseAPI;
import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.classes.dwarves.Dwarf;
import me.lojosho.dvzremapped.classes.monsters.Monster;
import me.lojosho.dvzremapped.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class User {
    private final Player player;
    private final UUID uuid;
    private UserStatus status;

    private PlayerClass userClass;
    private long lastSkillUse = -1;

    public User(@NotNull Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        status = UserStatus.LIMBO;
    }

    // Todo Take a look at this method and test it some other time
    public void convert(@NotNull PlayerClass userClass) {
        player.getInventory().clear();

        for (PotionEffect effect : getPlayer().getActivePotionEffects()) {
            getPlayer().removePotionEffect(effect.getType());
        }
        if (DisguiseAPI.isDisguised(getPlayer())) {
            DisguiseAPI.undisguiseToAll(getPlayer());
        }

        this.userClass = userClass;

        userClass.setup(this);
        setStatus(userClass.getType());

        if (userClass.getType() == UserStatus.DWARF)
            player.teleport(DvZRemappedPlugin.getDwarfSpawn());
        if (userClass.getType() == UserStatus.MONSTER)
            player.teleport(DvZRemappedPlugin.getMonsterSpawn());
        if (userClass.getType() == UserStatus.LIMBO) player.teleport(DvZRemappedPlugin.getJoinLocation());
    }

    public void turnDwarf(Dwarf dwarf) {
        Player player = getPlayer();
        player.getInventory().clear();
        userClass = dwarf;
        dwarf.setup(this);
        setStatus(UserStatus.DWARF);
        player.teleport(DvZRemappedPlugin.getDwarfSpawn());
    }

    public void turnMonster(Monster monster) {
        Player player = getPlayer();
        player.getInventory().clear();
        userClass = monster;
        monster.setup(this);
        setStatus(UserStatus.MONSTER);
        getPlayer().teleport(DvZRemappedPlugin.getMonsterSpawn());
    }

    public void reset() {
        userClass = null;
        setStatus(UserStatus.LIMBO);
        if (!getPlayer().isOnline()) return;
        getPlayer().getInventory().clear();
        getPlayer().teleport(DvZRemappedPlugin.getJoinLocation());
        Game.hideBossBar(getPlayer());
        for (PotionEffect effect : getPlayer().getActivePotionEffects()) {
            getPlayer().removePotionEffect(effect.getType());
        }
        if (DisguiseAPI.isDisguised(getPlayer())) {
            DisguiseAPI.undisguiseToAll(getPlayer());
        }
    }

    public void destroy() {
        // TODO
    }

    public void runSkill() {
        userClass.transmute(this);
    }

    public void setLastSkillUse(Long lastSkillUse) {
        this.lastSkillUse = lastSkillUse;
    }

    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    public @NotNull UserStatus getStatus() {
        return status;
    }

    public PlayerClass getUserClass() {
        return userClass;
    }

    public long getLastSkillUse() {
        return lastSkillUse;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    // Not safe for outside classes to call this method
    private void setStatus(@NotNull UserStatus status) { this.status = status; }
}
