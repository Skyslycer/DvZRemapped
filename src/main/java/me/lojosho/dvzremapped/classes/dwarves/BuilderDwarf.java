package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.util.MessagesUtil;
import me.lojosho.dvzremapped.util.PlayerUtil;
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

public class BuilderDwarf extends Dwarf {

    List<Material> blocks = List.of(Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.STONE_BRICKS);

    public BuilderDwarf() {
        super(Game.BUILDER, Material.MUSIC_DISC_CAT, NamedTextColor.GREEN, ChatColor.GREEN, 1,
                List.of("Transmutate air to get", "blocks to build the fortress!"), 10000);
    }

    @Override
    public void transmute(@NotNull User user) {
        Player player = user.getPlayer();

        if (!checkSkillReady(user)) {
            return;
        }
        Random ran = new Random();
        player.giveExp(ran.nextInt(1, 5));
        PlayerUtil.give(player,new ItemStack(Material.GLOWSTONE_DUST, 3));
        PlayerUtil.give(player, new ItemStack(blocks.get(ran.nextInt(0, blocks.size() - 1)), 64));
        if (ran.nextInt(1, 12) == 2) {
            PlayerUtil.give(player, new ItemStack(Material.STICK, 12));
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        user.setLastSkillUse(System.currentTimeMillis());
    }

    @Override
    public void setup(@NotNull User user) {
        super.setup(user);
        Player player = user.getPlayer();

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><#CE4B9C>Transmutation Skill Book"));
        meta.lore(List.of(
                Component.empty(),
                MiniMessage.miniMessage().deserialize("<gray>Transmutes <WHITE>Air <gray>to <WHITE>Stone Bricks + Glowstone"),
                MiniMessage.miniMessage().deserialize("<WHITE>                        (%) Sticks + Experience"),
                MiniMessage.miniMessage().deserialize("<GRAY><italic>(Left-Click to use)"),
                Component.empty())
        );
        book.setItemMeta(meta);
        ItemStack iron1 = unbreakableItem(Material.IRON_PICKAXE);
        ItemStack iron2 = unbreakableItem(Material.IRON_SHOVEL);
        ItemStack iron3 = unbreakableItem(Material.IRON_AXE);
        ItemStack leather1 = unbreakableItem(Material.LEATHER_HELMET);
        ItemStack leather2 = unbreakableItem(Material.LEATHER_CHESTPLATE);
        ItemStack leather3 = unbreakableItem(Material.LEATHER_LEGGINGS);
        ItemStack leather4 = unbreakableItem(Material.LEATHER_BOOTS);

        // Unbreakable stuff

        player.getInventory().addItem(book, iron1, iron2, iron3);
        player.getInventory().setHelmet(leather1);
        player.getInventory().setChestplate(leather2);
        player.getInventory().setLeggings(leather3);
        player.getInventory().setBoots(leather4);

        MessagesUtil.sendMessage(player, "<br><#CE4B9C>You picked the builder class! <br><br><#CE4B9C>Transmutation Skill <br><gray>Transmutes <WHITE>Air <gray>to <WHITE>64x Stone Bricks + 3 Glowstone <br><WHITE>                        (%) Sticks + Experience<br>");
    }
}
