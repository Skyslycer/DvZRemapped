package me.lojosho.dvzremapped.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerUtil {

    public static void giveAll(Player player, List<ItemStack> items) {
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
    }

}
