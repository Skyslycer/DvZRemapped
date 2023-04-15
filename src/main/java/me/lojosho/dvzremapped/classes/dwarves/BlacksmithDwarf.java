package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.util.MessagesUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class BlacksmithDwarf extends Dwarf {

    List<ItemStack> items = List.of(new ItemStack(Material.DIAMOND_PICKAXE), new ItemStack(Material.FEATHER, 32), new ItemStack(Material.FLINT, 32), new ItemStack(Material.STRING, 3), new ItemStack(Material.SHIELD), new ItemStack(Material.BLAZE_ROD, 4));

    public BlacksmithDwarf() {
        super("blacksmith", Material.MUSIC_DISC_STAL, .3f);
    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();

        if (!isSkillReady(user, 2000)) {
            MessagesUtil.sendMessage(player, "<#CE4B9C>Your transmutation is on cooldown! ");
            return;
        }

        ItemStack clock = new ItemStack(Material.CLOCK);

        Random random = new Random();

        if (player.getInventory().containsAtLeast(clock, 3)) {
            player.getInventory().removeItem(clock.asQuantity(3));
            player.getInventory().addItem(new ItemStack(Material.COAL, 8));
            player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 2));
            player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
            for (ItemStack item : items) {
                int chance = random.nextInt(0, 3);
                if (chance == 1) player.getInventory().addItem(item.clone());
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        } else {
            MessagesUtil.sendMessage(player, "<RED>You need at least 3 clocks to transmutate! ");
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
                MiniMessage.miniMessage().deserialize("<gray>Transmutes <WHITE>3x Clocks <gray>to"),
                MiniMessage.miniMessage().deserialize("<WHITE>   Diamond Sword + Blaze Rod + Coal "),
                MiniMessage.miniMessage().deserialize("<WHITE>   (%) Flint + String + Feathers "),
                MiniMessage.miniMessage().deserialize("<GRAY><italic>(Left-Click to use)"),
                Component.empty())
        );
        book.setItemMeta(meta);
        // Starting Items
        ItemStack ironPick = unbreakableItem(Material.IRON_PICKAXE);
        ItemStack item1 = new ItemStack(Material.FURNACE, 2);
        ItemStack item2 = new ItemStack(Material.CHEST, 2);
        ItemStack item3 = new ItemStack(Material.COAL, 10);
        ItemStack item4 = new ItemStack(Material.GOLD_ORE, 24);
        ItemStack item5 = new ItemStack(Material.NETHER_BRICKS, 64);
        ItemStack item6 = new ItemStack(Material.REDSTONE, 12);
        ItemStack item7 = new ItemStack(Material.OAK_SIGN, 3);
        ItemStack item8 = new ItemStack(Material.BUCKET, 1);

        player.getInventory().addItem(book, ironPick, item1, item2, item3, item4, item5, item6, item7, item8);

        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Fortuna Favors the Bold!").color(TextColor.color(0, 255, 0)));
        player.sendTitlePart(TitlePart.TITLE, Component.text(" "));
        // <gray>Transmutes <WHITE>2x Orange Dye <gray>to <WHITE>Diamond Armor + 10x Gold Ore + Food
        MessagesUtil.sendMessage(player, "<br><#CE4B9C>You picked the Blacksmith class! <br><br><#CE4B9C>Transmutation Skill <br><gray>Transmutes <WHITE>3x Clocks <gray>to <br><WHITE>  Diamond Sword + Blaze Rod + Coal <br><WHITE>  (%) Flint + String + Feathers<br>");
    }
}
