package me.lojosho.dvzremapped.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lojosho.dvzremapped.DvZRemappedPlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public final class MessagesUtil {
    public static void debug(@NotNull String message) {
        debug(Level.INFO, message);
    }

    public static void debug(@NotNull Level level, @NotNull String message) {
        DvZRemappedPlugin.getInstance().getLogger().log(level, message);
    }

    public static void sendMessage(Player player, String message) {
        Component finalMessage = processStringNoKey(player, message);
        player.sendMessage(finalMessage);
    }

    @NotNull
    public static Component processStringNoKey(Player player, String message) {
        return processStringNoKey(player, message, null);
    }

    @NotNull
    public static Component processStringNoKey(Player player, String message, TagResolver placeholders) {
        message = PlaceholderAPI.setPlaceholders(player, message);
        if (placeholders != null ) {
            return MiniMessage.miniMessage().deserialize(message, placeholders);
        }
        return MiniMessage.miniMessage().deserialize(message);
    }

}
