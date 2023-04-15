package me.lojosho.dvzremapped.command;

import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.Users;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DvZCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String args1 = args[0].toLowerCase();

        switch (args1) {
            case "reload":
                DvZRemappedPlugin.setup();
                break;
            case "start":
                Game.startGame();
                break;
            case "end":
                Game.endGame();
                break;
            case "reset":
                User user = Users.get(((Player) sender).getUniqueId());
                if (args.length == 2) {
                    user = Users.get(Bukkit.getPlayerUniqueId(args[1]));
                }
                if (user == null) {
                    sender.sendMessage("Invalid player");
                    return true;
                }
                user.reset();
                user.getPlayer().getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
                break;
            case "joinloc":
                DvZRemappedPlugin.setJoinLocation(((Player) sender).getLocation());
                sender.sendMessage("Join Loc Set " + ((Player) sender).getPlayer().getLocation());
                break;
            case "dwarfloc":
                DvZRemappedPlugin.setDwarfSpawn(((Player) sender).getLocation());
                sender.sendMessage("Dwarf Loc Set " + ((Player) sender).getPlayer().getLocation());
                break;
            case "monloc":
                DvZRemappedPlugin.setMonsterSpawn(((Player) sender).getLocation());
                sender.sendMessage("Monster Loc Set " + ((Player) sender).getPlayer().getLocation());
                break;
        }

        return true;
    }
}
