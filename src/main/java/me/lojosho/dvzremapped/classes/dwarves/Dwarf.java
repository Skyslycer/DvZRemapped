package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.user.UserStatus;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public abstract class Dwarf extends PlayerClass {
    public Dwarf(@NotNull String id, @NotNull Material selectionMaterial, @NotNull TextColor color, @NotNull ChatColor legacyColor,
                 float chance, @NotNull String description) {
        super(id, selectionMaterial, color, legacyColor, description, chance, UserStatus.DWARF);
        Dwarves.add(this);
    }
}
