package me.lojosho.dvzremapped.classes.monsters;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class Monster extends PlayerClass {
    private final @NotNull EntityType type;

    public Monster(@NotNull String id, @NotNull Material selectionMaterial, @NotNull TextColor color, @NotNull ChatColor legacyColor,
                   float chance, @NotNull EntityType type, @NotNull String description) {
        super(id, selectionMaterial, color, legacyColor, description, chance, UserStatus.MONSTER);
        Monsters.add(this);
        this.type = type;
    }

    public @NotNull EntityType getEntityType() {
        return type;
    }

    public void setup(@NotNull User user) {
        Disguise disguise = new MobDisguise(DisguiseType.getType(getEntityType()));
        disguise.setEntity(user.getPlayer());
        disguise.setSelfDisguiseVisible(true);
        disguise.setHidePlayer(true);
        disguise.startDisguise();

        user.getPlayer().getInventory().clear();
    };

}
