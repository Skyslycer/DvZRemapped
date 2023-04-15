package me.lojosho.dvzremapped.game;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.classes.dwarves.Dwarves;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import me.lojosho.dvzremapped.util.MessagesUtil;
import me.lojosho.dvzremapped.util.PlayerUtil;
import me.lojosho.dvzremapped.util.TimeUtil;
import me.lojosho.dvzremapped.util.WorldGuardFlags;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

public class Game {
    public final static int SHRINE_MAX_HEALTH = 750;
    //private final static int MONSTER_RELEASE_TIME = 30;
    private static int monsterReleaseTime = 2100;
    private final static int BOSS_RELEASE_TIME = 2101; // Night 3
    private static int timeCounted = -1;
    private static int shrineHealth = -1;
    private static BossBar bossBar;
    private static GameStatus status;
    private static BukkitTask task;

    public static void startGame() {
        if (status == GameStatus.RUNNING) return;
        monsterReleaseTime = DvZRemappedPlugin.getInstance().getConfig().getInt("MONSTER-RELEASE-TIME", 2100);
        shrineHealth = SHRINE_MAX_HEALTH;
        status = GameStatus.RUNNING;

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = Users.get(player.getUniqueId());
            if (user.getStatus() == UserStatus.LIMBO) {
                player.getInventory().clear();
                PlayerUtil.giveAll(player, PlayerClass.getRandomClassesItems(Dwarves.getRandomDwarfClasses()));
            }
        }

        World world = DvZRemappedPlugin.getDwarfSpawn().getWorld();
        if (world != null) {
            world.setTime(0);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.DISABLE_RAIDS, true);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.showTitle(Title.title(Component.text("DvZ has started!").color(NamedTextColor.RED),
                    Component.text("Fortuna favors the bold!").color(NamedTextColor.WHITE),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(10), Duration.ofSeconds(2))));
            player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1, 1));
        });

        bossBar = BossBar.bossBar(Component.text("Shrine Health"), 1, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

        task = Bukkit.getScheduler().runTaskTimer(DvZRemappedPlugin.getInstance(), () -> {
            timeCounted = timeCounted + 1;

            if (timeCounted == monsterReleaseTime) {
                runPlague(world);
            }

            // Show updated shrine health
            Bukkit.getOnlinePlayers().forEach(player -> player.showBossBar(bossBar));
            bossBar.progress((float) shrineHealth / SHRINE_MAX_HEALTH);

            // Check any monsters in shrine region
            checkShrine();

            // Checks if the game should end
            checkEndGame();
        }, 0, 20);
    }

    public static void endGame() {
        if (status != GameStatus.RUNNING) return;
        status = GameStatus.WAITING;
        task.cancel();

        for (User user : Users.values()) {
            if (!user.getPlayer().isOnline()) {
                Users.remove(user.getPlayer().getUniqueId());
                continue;
            }
            user.reset();
            user.getPlayer().playSound(Sound.sound(Key.key("entity.ender_dragon.death"), Sound.Source.MASTER, 1, 1));
            user.getPlayer().showTitle(Title.title(Component.text("DvZ has ended!").color(NamedTextColor.RED),
                    Component.text("Time: " + TimeUtil.formatTime(timeCounted)).color(NamedTextColor.WHITE),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(10), Duration.ofSeconds(2))));
            hideBossBar(user.getPlayer());
        }
        timeCounted = -1;
    }

    public static void checkEndGame() {
        if (status != GameStatus.RUNNING) return;
        // Do not end in the first 60 seconds
        if (timeCounted >= 60) {
            if (shrineHealth <= 0 || (isMonsterReleased() && Users.getCounted(UserStatus.DWARF) <= 0)) {
                endGame();
            }
        }
    }

    private static void checkShrine() {
        Users.values().stream().filter(user -> user.getStatus() == UserStatus.MONSTER)
                .map(User::getPlayer).forEach(player -> {
                    for (final ProtectedRegion r : WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()))
                            .getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()))) {
                        if (r.getFlags().containsKey(WorldGuardFlags.getShrineFlag()) && r.getFlags().get(WorldGuardFlags.getShrineFlag())
                                .toString().equalsIgnoreCase("ALLOW")) {
                            shrineHealth = shrineHealth - 1;
                            MessagesUtil.debug("Shrine is dying! " + shrineHealth);
                            if (shrineHealth % 100 == 0) {
                                Bukkit.broadcast(Component.text("Shrine health is now at " + shrineHealth + "/" + SHRINE_MAX_HEALTH));
                            }
                            break;
                        }
                    }
                });
    }

    private static void runPlague(World world) {
        Bukkit.broadcast(Component.text("A plague has ripped through the fortress! Monsters appear on the horizon...").color(NamedTextColor.RED));
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(Sound.sound(Key.key("entity.wither.spawn"), Sound.Source.HOSTILE, 1, 1));
            player.showTitle(Title.title(Component.text("The Plague").color(NamedTextColor.RED),
                    Component.text("Monster appear on the horizon...").color(NamedTextColor.WHITE),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
        });
        int playerSize = Bukkit.getOnlinePlayers().size();
        int numberToKillDwarves = (int) Math.max(playerSize * 0.1, 1); // 10% of players, or 1, whichever is higher

        int killedDwarves = 0; // From plague, kills 2 dwarves
        // Begin deciding monsters
        for (User user : Users.valuesRandomized()) {
            if (user.getStatus() == UserStatus.LIMBO) {
                Player player = user.getPlayer();
                player.getInventory().clear();
                player.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
            }
            if (user.getStatus() == UserStatus.DWARF && killedDwarves < numberToKillDwarves) {
                user.getPlayer().setHealth(0);
                user.getPlayer().sendMessage(Component.text("You died of the plague... your soul walks free now.").color(NamedTextColor.DARK_GREEN).style(Style.style(TextDecoration.ITALIC)));
                user.getPlayer().sendMessage(Component.text("Destroy the shrine you worked so hard to protect!").color(NamedTextColor.RED).style(Style.style(TextDecoration.BOLD)));
                killedDwarves = killedDwarves + 1;
            }
        }
        world.strikeLightningEffect(DvZRemappedPlugin.getMonsterSpawn());
    }

    public static void hideBossBar(Player player) {
        if (bossBar != null) player.hideBossBar(bossBar);
    }

    public static boolean isGameStart() {
        return timeCounted != -1;
    }

    public static boolean isMonsterReleased() {
        return timeCounted >= monsterReleaseTime;
    }

    public static GameStatus getStatus() {
        return status;
    }

    public static int getShrineHealth() {
        return shrineHealth;
    }
}
