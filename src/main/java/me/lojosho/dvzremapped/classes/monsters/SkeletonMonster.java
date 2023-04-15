package me.lojosho.dvzremapped.classes.monsters;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.lojosho.dvzremapped.user.User;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class SkeletonMonster extends Monster {

    public SkeletonMonster() {
        super("skeleton", Material.MUSIC_DISC_STRAD, .25f, EntityType.SKELETON);
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
        disguise.startDisguise();
        //DisguiseAPI.disguiseToAll(player, disguise);

        player.getInventory().clear();

        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        bowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
        bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
        bowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        bowMeta.setUnbreakable(true);
        bow.setItemMeta(bowMeta);

        player.getInventory().addItem(bow);
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));

        ItemStack fireResistancePotion = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta fireResistancePotionItemMeta = (PotionMeta) fireResistancePotion.getItemMeta();
        fireResistancePotionItemMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE, false, false));
        fireResistancePotion.setItemMeta(fireResistancePotionItemMeta);
        player.getInventory().addItem(fireResistancePotion);

        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 1));
    }
}
