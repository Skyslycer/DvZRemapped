package me.lojosho.dvzremapped.classes;

import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Class {
    private final String id;
    private final Material selectionMaterial;
    private final float chance;
    private final UserStatus type;

    protected Class(@NotNull String id, @NotNull Material selectionMaterial, float chance, UserStatus type) {
        this.id = id;
        this.selectionMaterial = selectionMaterial;
        this.chance = chance;
        this.type = type;
    }

    public abstract void setup(@NotNull User user);

    public abstract void transmute(@NotNull User user);

    public final @NotNull String getId() {
        return id;
    }

    public final @NotNull Material getSelectionMaterial() {
        return selectionMaterial;
    }

    public final float getChance() {
        return chance;
    }

    public @NotNull UserStatus getType() {
        return type;
    }

    // Returns if a skill is ready
    public final boolean isSkillReady(@NotNull User user, long cooldown) {
        if (user.getLastSkillUse() == -1) return true;
        return System.currentTimeMillis() - user.getLastSkillUse() >= cooldown;
    }

    // Returns long of how much longer the skill takes

    public final Long isSkillReady(@NotNull User user, long cooldown, boolean returns) {
        if (user.getLastSkillUse() == -1) return (long) -1;
        return System.currentTimeMillis() - user.getLastSkillUse();
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
}
