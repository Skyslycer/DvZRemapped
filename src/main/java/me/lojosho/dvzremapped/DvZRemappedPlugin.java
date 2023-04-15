package me.lojosho.dvzremapped;

import me.lojosho.dvzremapped.command.DvZCommand;
import me.lojosho.dvzremapped.command.DvZCommandTabComplete;
import me.lojosho.dvzremapped.listener.PlayerConnectionListener;
import me.lojosho.dvzremapped.listener.PlayerGameListener;
import me.lojosho.dvzremapped.util.DvZPlaceholderExpansion;
import me.lojosho.dvzremapped.util.WorldGuardFlags;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class DvZRemappedPlugin extends JavaPlugin {
    private static DvZRemappedPlugin instance;
    private static Location joinLocation;
    private static Location monsterSpawn;
    private static Location dwarfSpawn;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        instance = this;

        setup();

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGameListener(), this);

        // Commands
        getServer().getPluginCommand("dvz").setExecutor(new DvZCommand());
        getServer().getPluginCommand("dvz").setTabCompleter(new DvZCommandTabComplete());

        // PAPI
        new DvZPlaceholderExpansion().register();
    }

    public static void setup() {
        FileConfiguration config = getInstance().getConfig();
        dwarfSpawn = new Location(Bukkit.getWorld(config.getString("spawn-location.dwarves.world")),
                config.getDouble("spawn-location.dwarves.x"),
                config.getDouble("spawn-location.dwarves.y"),
                config.getDouble("spawn-location.dwarves.z"),
                (float) config.getDouble("spawn-location.dwarves.yaw"),
                (float) config.getDouble("spawn-location.dwarves.pitch")
        );
        joinLocation = new Location(Bukkit.getWorld(config.getString("spawn-location.join.world")),
                config.getDouble("spawn-location.join.x"),
                config.getDouble("spawn-location.join.y"),
                config.getDouble("spawn-location.join.z"),
                (float) config.getDouble("spawn-location.join.yaw"),
                (float) config.getDouble("spawn-location.join.pitch")
        );
        monsterSpawn = new Location(Bukkit.getWorld(config.getString("spawn-location.monster.world")),
                config.getDouble("spawn-location.monster.x"),
                config.getDouble("spawn-location.monster.y"),
                config.getDouble("spawn-location.monster.z"),
                (float) config.getDouble("spawn-location.monster.yaw"),
                (float) config.getDouble("spawn-location.monster.pitch")
        );
        getInstance().reloadConfig();
    }

    @Override
    public void onLoad() {
        // WorldGuard
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WorldGuardFlags.register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    /**
     * Gets an instance of the {@link DvZRemappedPlugin plugin}
     * @return An instance of the {@link DvZRemappedPlugin plugin}
     */
    public static DvZRemappedPlugin getInstance() {
        return instance;
    }

    public static Location getDwarfSpawn() {
        return dwarfSpawn;
    }

    public static Location getJoinLocation() {
        return joinLocation;
    }

    public static Location getMonsterSpawn() {
        return monsterSpawn;
    }

    public static void setDwarfSpawn(Location dwarfSpawn) {
        DvZRemappedPlugin.dwarfSpawn = dwarfSpawn;

        getInstance().getConfig().set("spawn-location.dwarves.world", dwarfSpawn.getWorld().getName());
        getInstance().getConfig().set("spawn-location.dwarves.x", dwarfSpawn.getX());
        getInstance().getConfig().set("spawn-location.dwarves.y", dwarfSpawn.getY());
        getInstance().getConfig().set("spawn-location.dwarves.z", dwarfSpawn.getZ());
        getInstance().getConfig().set("spawn-location.dwarves.pitch", dwarfSpawn.getPitch());
        getInstance().getConfig().set("spawn-location.dwarves.yaw", dwarfSpawn.getYaw());
        getInstance().saveConfig();
    }

    public static void setJoinLocation(Location joinLocation) {
        DvZRemappedPlugin.joinLocation = joinLocation;

        getInstance().getConfig().set("spawn-location.join.world", joinLocation.getWorld().getName());
        getInstance().getConfig().set("spawn-location.join.x", joinLocation.getX());
        getInstance().getConfig().set("spawn-location.join.y", joinLocation.getY());
        getInstance().getConfig().set("spawn-location.join.z", joinLocation.getZ());
        getInstance().getConfig().set("spawn-location.join.pitch", joinLocation.getPitch());
        getInstance().getConfig().set("spawn-location.join.yaw", joinLocation.getYaw());
        getInstance().saveConfig();
    }

    public static void setMonsterSpawn(Location monsterSpawn) {
        DvZRemappedPlugin.monsterSpawn = monsterSpawn;

        getInstance().getConfig().set("spawn-location.monster.world", monsterSpawn.getWorld().getName());
        getInstance().getConfig().set("spawn-location.monster.x", monsterSpawn.getX());
        getInstance().getConfig().set("spawn-location.monster.y", monsterSpawn.getY());
        getInstance().getConfig().set("spawn-location.monster.z", monsterSpawn.getZ());
        getInstance().getConfig().set("spawn-location.monster.pitch", monsterSpawn.getPitch());
        getInstance().getConfig().set("spawn-location.monster.yaw", monsterSpawn.getYaw());
        getInstance().saveConfig();
    }
}
