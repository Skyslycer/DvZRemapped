package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.util.MessagesUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public abstract class Dwarf extends PlayerClass {
    public Dwarf(@NotNull String id, @NotNull Material selectionMaterial, @NotNull TextColor color, @NotNull ChatColor legacyColor,
                 float chance, @NotNull String description, long cooldown) {
        super(id, selectionMaterial, color, legacyColor, description, chance, UserStatus.DWARF, cooldown);
        Dwarves.add(this);
    }

    @Override
    public boolean checkSkillReady(@NotNull User user) {
        var time = getSkillTime(user, getCooldown());
        if (time > 0) {
            MessagesUtil.sendMessage(user.getPlayer(), "<#CE4B9C>Your transmutation is on cooldown! " + time + "s");
            return false;
        }
        return true;
    }

    @Override
    public void setup(@NotNull User user) {
        var title = Title.title(
                Component.text(StringUtils.capitalizeFirstLetter(getId())).color(getColor()),
                Component.text("Fortuna favors the bold!").color(NamedTextColor.GREEN),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofSeconds(1))
        );
        user.getPlayer().showTitle(title);
        user.getPlayer().playSound(user.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
