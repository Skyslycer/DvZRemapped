package me.lojosho.dvzremapped.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DvZCommandTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> finalCompletions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("end");
            completions.add("reset");

            completions.add("joinloc");
            completions.add("dwarfloc");
            completions.add("monloc");

            StringUtil.copyPartialMatches(args[0], completions, finalCompletions);
        }

        Collections.sort(finalCompletions);
        return finalCompletions;
    }
}
