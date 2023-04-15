package me.lojosho.dvzremapped.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.game.Game;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public final class DvZPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "LoJoSho & Craftinators";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "DvZ";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @NotNull String onRequest(@NotNull OfflinePlayer player, @NotNull String params) {
        User user = Users.get(player.getUniqueId());

        return switch (params.toLowerCase()) {
            case "user_class" -> {
                if (user == null) yield "";

                PlayerClass userClass = user.getUserClass();
                if (userClass == null) yield ChatColor.GRAY + "Classless" + ChatColor.RESET;

                yield userClass.getLegacyColor() + StringUtils.capitalizeFirstLetter(user.getUserClass().getId().toLowerCase()) + ChatColor.RESET;
            }
            case "game_status" -> StringUtils.capitalizeFirstLetter(Game.getStatus().toString().toLowerCase());
            case "current_shrine_health" -> String.valueOf(Game.getShrineHealth());
            case "max_shrine_health" -> String.valueOf(Game.SHRINE_MAX_HEALTH);
            case "dwarves_left" -> String.valueOf(Users.getCounted(UserStatus.DWARF));
            case "monsters" -> String.valueOf(Users.getCounted(UserStatus.MONSTER));
            default -> "";
        };
    }
}
