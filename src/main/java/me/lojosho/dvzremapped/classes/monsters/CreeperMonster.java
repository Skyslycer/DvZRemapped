package me.lojosho.dvzremapped.classes.monsters;

import me.lojosho.dvzremapped.user.User;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreeperMonster extends Monster {

    public CreeperMonster() {
        super("creeper", Material.MUSIC_DISC_CHIRP, NamedTextColor.RED, ChatColor.RED, .15f, EntityType.CREEPER,
                List.of("Blow up the fortress", "with your explosive skill!"), -1);
    }

    @Override
    public void transmute(@NotNull User user) {
        ItemStack heldItem = user.getPlayer().getInventory().getItemInMainHand();
        if (heldItem == null) return;
        if (heldItem.getType().equals(Material.GUNPOWDER)) {
            user.getPlayer().setHealth(0);
            user.getPlayer().getLocation().createExplosion(7, true, true);
            user.getPlayer().getInventory().clear();
        }
    }

    @Override
    public void setup(@NotNull User user) {
        Player player = user.getPlayer();
        super.setup(user);

        player.getInventory().addItem(new ItemStack(Material.GUNPOWDER, 1));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 1));
    }
}
