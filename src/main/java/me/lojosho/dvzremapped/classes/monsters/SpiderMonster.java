package me.lojosho.dvzremapped.classes.monsters;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SpiderMonster extends Monster {

    public SpiderMonster() {
        super("spider", Material.MUSIC_DISC_MALL, .15f, EntityType.SPIDER);
    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();
        ItemStack heldItem = user.getPlayer().getInventory().getItemInMainHand();
        if (heldItem == null) return;
        if (heldItem.getType().equals(Material.SPIDER_EYE)) {

            if (!isSkillReady(user, 30000)) {
                player.sendMessage(Component.text("Cooldown! "));
                return;
            }

            Location location = player.getLocation();

            for (Player goingDieSoonPlayer : location.getNearbyPlayers(10)) {
                User user1 = Users.get(goingDieSoonPlayer.getUniqueId());
                if (user1.getStatus().equals(UserStatus.MONSTER)) continue;
                goingDieSoonPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 1));
                goingDieSoonPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 1));
            }

            user.setLastSkillUse(System.currentTimeMillis());
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
        player.getInventory().addItem(new ItemStack(Material.SPIDER_EYE, 1));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().addItem(new ItemStack(Material.VINE, 6));

        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 1));
    }
}
