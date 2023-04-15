package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.util.MessagesUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class AlchemistDwarf extends Dwarf {

    private static ItemStack potionOfMundane;
    private static ItemStack potionOfHealing;
    private static ItemStack potionOfSwiftness;
    private static ItemStack potionOfStrength;
    private static ItemStack potionOfFireResistance;

    public AlchemistDwarf() {
        super("alchemist", Material.MUSIC_DISC_MELLOHI, NamedTextColor.DARK_PURPLE, ChatColor.DARK_PURPLE, .5f, "Transmutate mundane potions to get potions to save and powerup dwarves!");

        // Set up potions

        ItemStack mundanePotion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) mundanePotion.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.MUNDANE));
        mundanePotion.setItemMeta(meta);
        potionOfMundane = mundanePotion;

        ItemStack healingPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta healingPotionItemMeta = (PotionMeta) healingPotion.getItemMeta();
        healingPotionItemMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        healingPotion.setItemMeta(healingPotionItemMeta);
        potionOfHealing = healingPotion;

        ItemStack swiftnessPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta swiftnessMeta = (PotionMeta) swiftnessPotion.getItemMeta();
        swiftnessMeta.setBasePotionData(new PotionData(PotionType.SPEED, true, false));
        swiftnessPotion.setItemMeta(swiftnessMeta);
        potionOfSwiftness = swiftnessPotion;

        ItemStack strengthPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta strengthPotionItemMeta = (PotionMeta) strengthPotion.getItemMeta();
        strengthPotionItemMeta.setBasePotionData(new PotionData(PotionType.STRENGTH, false, true));
        strengthPotion.setItemMeta(strengthPotionItemMeta);
        potionOfStrength = strengthPotion;

        ItemStack fireResistance = new ItemStack(Material.SPLASH_POTION);
        PotionMeta fireResistanceItemMeta = (PotionMeta) fireResistance.getItemMeta();
        fireResistanceItemMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE, true, false));
        fireResistance.setItemMeta(fireResistanceItemMeta);
        potionOfFireResistance = fireResistance;


    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();

        if (!isSkillReady(user, 2000)) {
            MessagesUtil.sendMessage(player, "<#CE4B9C>Your transmutation is on cooldown! ");
            return;
        }

        ItemStack mundanePotion = potionOfMundane.clone();

        Random random = new Random();

        if (player.getInventory().containsAtLeast(mundanePotion, 3)) {
            player.getInventory().removeItem(mundanePotion);
            player.getInventory().removeItem(mundanePotion);
            player.getInventory().removeItem(mundanePotion);
            player.getInventory().addItem(new ItemStack(Material.BONE, random.nextInt(3, 8)));
            player.getInventory().addItem(new ItemStack(Material.MILK_BUCKET, random.nextInt(1, 2)));
            player.getInventory().addItem(new ItemStack(Material.SAND, 9));
            player.getInventory().addItem(new ItemStack(Material.REDSTONE, 8));
            if (random.nextInt(1, 3) != 1) {
                player.getInventory().addItem(potionOfHealing.clone());
                player.getInventory().addItem(potionOfHealing.clone());
            } else {
                player.getInventory().addItem(potionOfSwiftness.clone());
            }
            // Either Strength or Fire Resistance
            if (random.nextInt(1, 3) == 1) {
                player.getInventory().addItem(potionOfFireResistance.clone());
            } else {
                player.getInventory().addItem(potionOfStrength.clone());
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        } else {
            MessagesUtil.sendMessage(player, "<RED>You need at least 3 mundane potions to transmute! ");
            return;
        }

        user.setLastSkillUse(System.currentTimeMillis());
    }

    @Override
    public void setup(@NotNull User user) {
        Player player = user.getPlayer();

        // Transmutable Item
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<#CE4B9C>Transmutation Skill Book"));
        meta.lore(List.of(
                Component.empty(),
                MiniMessage.miniMessage().deserialize("<GRAY>Transmutes <WHITE>3x Mundane Potions <gray>to "),
                MiniMessage.miniMessage().deserialize("<WHITE>   Potions + Milk + Bones + Redstone + Sand"),
                MiniMessage.miniMessage().deserialize("<GRAY><italic>(Left-Click to use)"),
                Component.empty())
        );
        book.setItemMeta(meta);
        // Starting Items
        ItemStack item1 = new ItemStack(Material.BREWING_STAND, 2);
        ItemStack item2 = new ItemStack(Material.CAULDRON, 2);
        ItemStack item3 = new ItemStack(Material.CHEST, 2);
        ItemStack item4 = new ItemStack(Material.REDSTONE, 5);
        ItemStack item9 = new ItemStack(Material.BLAZE_ROD, 6);
        ItemStack item5 = new ItemStack(Material.LAPIS_BLOCK, 64);
        ItemStack item6 = new ItemStack(Material.GLASS, 64);
        ItemStack item7 = new ItemStack(Material.OAK_SIGN, 3);
        ItemStack item8 = new ItemStack(Material.BUCKET, 1);

        player.getInventory().addItem(book, item1, item2, item3, item4, item5, item6, item7, item8, item9);

        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Fortuna Favors the Bold!").color(TextColor.color(0, 255, 0)));
        player.sendTitlePart(TitlePart.TITLE, Component.text(" "));

        MessagesUtil.sendMessage(player, "<br><#CE4B9C>You picked the Alchemist class! <br><br><#CE4B9C>Transmutation Skill <br><GRAY>Transmutes <WHITE>3x Mundane Potions <gray>to <br><WHITE>   Potions + Milk + Bones + Redstone + Sand <br>");
    }
}
