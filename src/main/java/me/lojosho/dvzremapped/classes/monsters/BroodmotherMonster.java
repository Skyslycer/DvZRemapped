package me.lojosho.dvzremapped.classes.monsters;

import me.lojosho.dvzremapped.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class BroodmotherMonster extends Monster {

    public BroodmotherMonster() {
        super("Broodmother", Material.MUSIC_DISC_WARD, NamedTextColor.DARK_GREEN, ChatColor.DARK_GREEN, .05f, EntityType.SILVERFISH, "Spawn silverfish and attack the fortress!");
    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();
        ItemStack heldItem = user.getPlayer().getInventory().getItemInMainHand();
        if (heldItem == null) return;
        if (heldItem.getType().equals(Material.GRAY_DYE)) {

            if (!isSkillReady(user, 30000)) {
                player.sendMessage(Component.text("Ability is on cooldown! "));
                return;
            }

            Location location = player.getLocation();

            int amount = 0;
            while (amount <= 5) {
                location.getWorld().spawnEntity(location, EntityType.SILVERFISH);
                amount = amount + 1;
            }

            user.setLastSkillUse(System.currentTimeMillis());
        }
    }

    @Override
    public void setup(@NotNull User user) {
        Player player = user.getPlayer();
        super.setup(user);

        player.getInventory().addItem(new ItemStack(Material.GRAY_DYE, 1));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().addItem(new ItemStack(Material.INFESTED_STONE_BRICKS, 4));

        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 2));
    }
}
