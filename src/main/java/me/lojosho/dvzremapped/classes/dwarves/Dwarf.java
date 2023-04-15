package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.classes.Class;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Dwarf extends Class {
    public Dwarf(@NotNull String id, @NotNull Material selectionMaterial, float chance) {
        super(id, selectionMaterial, chance, UserStatus.DWARF);
        Dwarves.add(this);
    }
}
