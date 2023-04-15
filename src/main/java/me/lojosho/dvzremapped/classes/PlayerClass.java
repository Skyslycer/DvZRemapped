package me.lojosho.dvzremapped.classes;

import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerClass {
    private final String id;
    private final Material selectionMaterial;
    private final TextColor color;
    private final ChatColor legacyColor;
    private final String description;
    private final float chance;
    private final UserStatus type;
    private final long cooldown;

    protected PlayerClass(@NotNull String id, @NotNull Material selectionMaterial, TextColor color, ChatColor legacyColor, String description, float chance, UserStatus type, long cooldown) {
        this.id = id;
        this.selectionMaterial = selectionMaterial;
        this.color = color;
        this.legacyColor = legacyColor;
        this.description = description;
        this.chance = chance;
        this.type = type;
        this.cooldown = cooldown;
    }

    public abstract void setup(@NotNull User user);

    public abstract void transmute(@NotNull User user);

    public final @NotNull String getId() {
        return id;
    }

    public final @NotNull Material getSelectionMaterial() {
        return selectionMaterial;
    }

    public final TextColor getColor() {
        return color;
    }

    public final String getDescription() {
        return description;
    }

    public final float getChance() {
        return chance;
    }

    public @NotNull UserStatus getType() {
        return type;
    }

    public ChatColor getLegacyColor() {
        return legacyColor;
    }

    public long getCooldown() {
        return cooldown;
    }

    public abstract boolean checkSkillReady(@NotNull User user);

    // Returns if a skill is ready
    public final boolean isSkillReady(@NotNull User user, long cooldown) {
        if (user.getLastSkillUse() == -1) return true;
        return System.currentTimeMillis() - user.getLastSkillUse() >= cooldown;
    }

    // Returns long of how much longer the skill takes

    public final long getSkillTime(@NotNull User user, long cooldown) {
        if (user.getLastSkillUse() == -1) return -1;
        return cooldown - (System.currentTimeMillis() - user.getLastSkillUse());
    }

    public final ItemStack unbreakableItem(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public final void addItem(User user, ItemStack itemStack) {
        Player player = user.getPlayer();
        var drops = player.getInventory().addItem(itemStack);
        drops.values().forEach(left -> player.getLocation().getWorld().dropItemNaturally(player.getLocation(), left));
    }

    public static List<ItemStack> getRandomClassesItems(List<PlayerClass> classes) {
        List<ItemStack> items = new ArrayList<>();
        for (PlayerClass type : classes) {
            var item = new ItemStack(type.getSelectionMaterial());
            var meta = item.getItemMeta();
            meta.displayName(Component.text(StringUtils.capitalizeFirstLetter(type.getId())).color(type.getColor()));
            meta.lore(List.of(Component.text(type.getDescription()).color(NamedTextColor.WHITE)));
            item.setItemMeta(meta);
            items.add(item);
        }
        return items;
    }

}
