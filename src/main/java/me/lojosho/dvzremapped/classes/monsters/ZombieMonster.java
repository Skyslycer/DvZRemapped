package me.lojosho.dvzremapped.classes.monsters;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.lojosho.dvzremapped.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class ZombieMonster extends Monster {

    public ZombieMonster() {
        super("zombie", Material.MUSIC_DISC_WAIT, 1, EntityType.ZOMBIE);
    }

    @Override
    public void transmute(@NotNull User user) {
        // Nothing
    }

    @Override
    public void setup(@NotNull User user) {

        Player player = user.getPlayer();

        Disguise disguise = new MobDisguise(DisguiseType.getType(getEntityType()));
        disguise.setEntity(user.getPlayer());
        disguise.setSelfDisguiseVisible(true);
        disguise.setHidePlayer(true);
        disguise.setMobsIgnoreDisguise(true);
        disguise.startDisguise();
        //DisguiseAPI.disguiseToAll(player, disguise);

        player.getInventory().clear();

        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta swordItemMeta = sword.getItemMeta();
        swordItemMeta.setUnbreakable(true);
        sword.setItemMeta(swordItemMeta);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        ItemStack healingPotion = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta healingPotionItemMeta = (PotionMeta) healingPotion.getItemMeta();
        healingPotionItemMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        healingPotion.setItemMeta(healingPotionItemMeta);

        player.getInventory().addItem(healingPotion);
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 1));
    }
}
