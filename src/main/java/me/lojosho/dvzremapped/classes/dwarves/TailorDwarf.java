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
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class TailorDwarf extends Dwarf {

    List<ItemStack> items = List.of(new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.SHIELD));

    public TailorDwarf() {
        super("tailor", Material.MUSIC_DISC_OTHERSIDE, NamedTextColor.BLUE, ChatColor.BLUE, .5f, "Transmutate mundane potions to get potions to save and powerup dwarves!");
    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();

        if (!isSkillReady(user, 2000)) {
            MessagesUtil.sendMessage(player, "<#CE4B9C>Your transmutation is on cooldown! ");
            return;
        }

        ItemStack dye = new ItemStack(Material.ORANGE_DYE);

        Random random = new Random();

        if (player.getInventory().containsAtLeast(dye, 2)) {
            player.getInventory().removeItem(dye);
            player.getInventory().removeItem(dye);
            player.getInventory().addItem(new ItemStack(Material.GOLD_ORE, 10));
            player.getInventory().addItem(new ItemStack(Material.CAKE));
            for (ItemStack item : items) {
                player.getInventory().addItem(item.clone());
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        } else {
            MessagesUtil.sendMessage(player, "<RED>You need 2 orange dyes to transmutate! ");
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
                MiniMessage.miniMessage().deserialize("<gray>Transmutes <WHITE>2x Orange Dye <gray>to"),
                MiniMessage.miniMessage().deserialize("<WHITE>  Diamond Armor + Gold Ore + Cake "),
                MiniMessage.miniMessage().deserialize("<GRAY><italic>(Left-Click to use)"),
                Component.empty())
        );
        book.setItemMeta(meta);
        // Starting Items
        ItemStack ironPick = new ItemStack(Material.GRASS_BLOCK, 64);
        ItemStack item1 = new ItemStack(Material.BONE, 6);
        ItemStack item5 = new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 64);
        ItemStack item6 = new ItemStack(Material.ORANGE_DYE, 4);
        ItemStack item7 = new ItemStack(Material.OAK_SIGN, 3);

        player.getInventory().addItem(book, ironPick, item1, item5, item6, item7);

        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Fortuna Favors the Bold!").color(TextColor.color(0, 255, 0)));
        player.sendTitlePart(TitlePart.TITLE, Component.text(" "));
        // <gray>Transmutes <WHITE>Air <gray>to <WHITE>
        MessagesUtil.sendMessage(player, "<br><#CE4B9C>You picked the tailor class! <br><br><#CE4B9C>Transmutation Skill <br><gray>Transmutes <WHITE>2x Orange Dye <gray>to<br>  <WHITE>Diamond Armor + Gold Ore + Food <br>");
    }
}
