package me.lojosho.dvzremapped.classes.monsters;

import me.lojosho.dvzremapped.classes.Class;
import me.lojosho.dvzremapped.user.UserStatus;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class Monster extends Class {
    private final @NotNull EntityType type;

    public Monster(@NotNull String id, @NotNull Material selectionMaterial, float chance, @NotNull EntityType type) {
        super(id, selectionMaterial, chance, UserStatus.MONSTER);
        Monsters.add(this);
        this.type = type;
    }

    public @NotNull EntityType getEntityType() {
        return type;
    }
}
