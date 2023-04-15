package me.lojosho.dvzremapped.util;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import java.util.logging.Level;

public final class WorldGuardFlags {
    private static StateFlag SHRINE_FLAG;

    private WorldGuardFlags() {}

    public static void register() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            StateFlag stateFlag = new StateFlag("shrine", false);
            registry.register(stateFlag);
            SHRINE_FLAG = stateFlag;
        } catch (FlagConflictException exception) {
            MessagesUtil.debug(Level.SEVERE, "Failed to register shrine StateFlag!");
        }
    }

    public static StateFlag getShrineFlag() {
        return SHRINE_FLAG;
    }
}
