package me.lojosho.dvzremapped.classes.monsters;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.lojosho.dvzremapped.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CreeperMonster extends Monster {

    public CreeperMonster() {
        super("creeper", Material.MUSIC_DISC_CHIRP, .15f, EntityType.CREEPER);
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

        Disguise disguise = new MobDisguise(DisguiseType.getType(getEntityType()));
        disguise.setEntity(user.getPlayer());
        disguise.setSelfDisguiseVisible(true);
        disguise.setHidePlayer(true);
        disguise.startDisguise();
        //DisguiseAPI.disguiseToAll(player, disguise);

        player.getInventory().clear();
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
