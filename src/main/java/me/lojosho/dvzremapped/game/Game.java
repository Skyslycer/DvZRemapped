package me.lojosho.dvzremapped.game;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.lojosho.dvzremapped.DvZRemappedPlugin;
import me.lojosho.dvzremapped.user.User;
import me.lojosho.dvzremapped.user.UserStatus;
import me.lojosho.dvzremapped.user.Users;
import me.lojosho.dvzremapped.util.MessagesUtil;
import me.lojosho.dvzremapped.util.WorldGuardFlags;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game {
    public final static int SHRINE_MAX_HEALTH = 750;
    //private final static int MONSTER_RELEASE_TIME = 30;
    private static int monsterReleaseTime = 2100;
    private final static int BOSS_RELEASE_TIME = 2101; // Night 3
    private static int timeCounted = -1;
    private static int shrineHealth = -1;
    private static BossBar bossBar;
    private static GameStatus status;
    private static int taskId;

    public static void startGame() {
        if (status == GameStatus.RUNNING) return;
        monsterReleaseTime = DvZRemappedPlugin.getInstance().getConfig().getInt("MONSTER-RELEASE-TIME", 2100);
        shrineHealth = SHRINE_MAX_HEALTH;
        status = GameStatus.RUNNING;

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = Users.get(player.getUniqueId());
            if (user.getStatus() == UserStatus.LIMBO) {
                player.getInventory().clear();
                player.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
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

        Bukkit.broadcast(Component.text("DvZ has started! Fortuna Favors the bold! ").color(TextColor.color(0, 255, 0)));

        bossBar = BossBar.bossBar(Component.text("Shrine Health"), 1, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

        BukkitRunnable run = new BukkitRunnable() {
            @Override
            public void run() {
                timeCounted = timeCounted + 1;

                if (timeCounted == monsterReleaseTime) {
                    Bukkit.broadcast(Component.text("A plague has ripped through the fortress! Monsters appear on the horizon...").color(TextColor.color(255, 0, 0)));
                    int playerSize = Bukkit.getOnlinePlayers().size();
                    int numberToKillDwarves = 2;

                    if (playerSize >= 30) {
                        numberToKillDwarves = Integer.parseInt(Integer.toString(playerSize).substring(0, 1)); // Takes first digit of number
                    }

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
                            user.getPlayer().sendMessage(Component.text("You died of the plague... your soul walks free now. ").color(TextColor.color(255, 0, 0)));
                            killedDwarves = killedDwarves + 1;
                        }
                    }
                    world.strikeLightningEffect(DvZRemappedPlugin.getMonsterSpawn());
                }
                if (timeCounted == BOSS_RELEASE_TIME) {
                    /*
                    EnderDragon entity = (EnderDragon) DvZRemappedPlugin.getMonsterSpawn().getWorld().spawnEntity(DvZRemappedPlugin.getMonsterSpawn(), EntityType.ENDER_DRAGON);
                    Bukkit.getMobGoals().getAllGoals(entity).clear();
                    entity.setPhase(EnderDragon.Phase.STRAFING);
                    //Bukkit.getMobGoals().addGoal(entity, 3, );
                     */
                    // Dragon spawns in
                }
                bossBar.progress((float) shrineHealth / SHRINE_MAX_HEALTH);
                // Check any monsters in thing
                for (Player player : Bukkit.getOnlinePlayers()) {
                    User user = Users.get(player.getUniqueId());

                    player.showBossBar(bossBar);

                    if (user.getStatus().equals(UserStatus.MONSTER)) {
                        for (final ProtectedRegion r : WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()))
                                .getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()))) {
                            if (r.getFlags().containsKey(WorldGuardFlags.getShrineFlag())) {
                                if (r.getFlags().get(WorldGuardFlags.getShrineFlag()).toString().equalsIgnoreCase("ALLOW")) {
                                    shrineHealth = shrineHealth - 1;
                                    MessagesUtil.debug("Shrine is dying! " + shrineHealth);
                                    if (shrineHealth % 100 == 0) {
                                        Bukkit.broadcast(Component.text("Shrine health is now at " + shrineHealth + "/" + SHRINE_MAX_HEALTH));
                                    }
                                } else {
                                    // Woah!...  He is out of the region!
                                }
                            }
                        }
                    }
                }

                checkEndGame(); // Checks if the game should end
            }
        };

        run.runTaskTimer(DvZRemappedPlugin.getInstance(), 0, 20);
        taskId = run.getTaskId();

    }

    public static void endGame() {
        if (status != GameStatus.RUNNING) return;
        status = GameStatus.WAITING;
        Bukkit.getScheduler().cancelTask(taskId);

        for (User user : Users.values()) {
            user.reset();
            World world = Bukkit.getWorld("world");
            if (bossBar != null) user.getPlayer().hideBossBar(bossBar);
        }

        Bukkit.broadcast(Component.text("DvZ has ended. Total Time: " + TimeUnit.SECONDS.toMinutes(timeCounted) + "m"));
        timeCounted = -1;
    }

    public static void checkEndGame() {
        if (status != GameStatus.RUNNING) return;
        // Do not end in the first 60 seconds
        if (timeCounted >= 60) {
            if (shrineHealth < 0 || Users.getDwarvesCounted() == 0) {
                endGame();
            }
        }
    }

    public static boolean isGameStart() {
        if (timeCounted != -1) return true;
        return false;
    }

    public static boolean isMonsterReleased() {
        if (timeCounted >= monsterReleaseTime) return true;
        return false;
    }

    public static GameStatus getStatus() {
        return status;
    }

    public static int getShrineHealth() {
        return shrineHealth;
    }
}
