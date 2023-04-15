package me.lojosho.dvzremapped.classes.dwarves;

import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.util.MessagesUtil;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Manages fetching the internal {@link org.bukkit.entity.Player} wrapper, {@link Dwarf}
 */
public final class Dwarves {
    public static final @NotNull HashMap<@NotNull String, @Nullable Dwarf> dwarves = new HashMap<>();

    // Classes
    private static BuilderDwarf builderDwarf = new BuilderDwarf();
    private static AlchemistDwarf alchemistDwarf = new AlchemistDwarf();
    private static TailorDwarf tailorDwarf = new TailorDwarf();
    private static BlacksmithDwarf blacksmithDwarf = new BlacksmithDwarf();

    /**
     * Associates the {@link String id} of the specified {@link Dwarf dwarf} with the specified {@link Dwarf dwarf}
     * @param dwarf {@link Dwarf Dwarf} value to be associated with the dwarves {@link String id}
     */
    public static void add(@NotNull Dwarf dwarf) {
        dwarves.put(dwarf.getId(), dwarf);
    }

    /**
     * Removes the mapped {@link Dwarf dwarf} for the specified {@link String id} if present
     * @param id {@link String Id} whose mapped {@link Dwarf dwarf} is to be removed
     * @return The previous {@link Dwarf dwarf} associated with the {@link String id}, or {@code null} if there was no mapping for the {@link String id} previously
     */
    public static @Nullable Dwarf remove(@NotNull String id) {
        return dwarves.remove(id);
    }

    /**
     * Returns the {@link Dwarf dwarf} to which the specified {@link String id} is mapped, or {@code null} if there is no mapped {@link Dwarf dwarf} for the {@link String id}
     * @param id {@link String Id} whose mapped {@link Dwarf dwarf} is to be returned
     * @return The mapped {@link Dwarf dwarf} associated with the specified {@link String id}, or {@code null} if there is no mapped {@link Dwarf dwarf} for the {@link String id}
     */
    public static @Nullable Dwarf get(@NotNull String id) {
        return dwarves.get(id.toLowerCase());
    }


    public static List<PlayerClass> getRandomDwarfClasses() {
        Random random = new Random();
        List<PlayerClass> classes = new ArrayList<>();
        for (Dwarf dwarfclass : dwarves.values()) {
            float randomnumber = random.nextFloat(0, 1);
            MessagesUtil.debug("Random # is " + randomnumber);
            if (randomnumber < dwarfclass.getChance()) {
                classes.add(dwarfclass);
            }
        }
        return classes;
    }

    public static Dwarf getDwarfClass(Material material) {
        for (Dwarf dwarf : dwarves.values()) {
            if (dwarf.getSelectionMaterial() == material) return dwarf;
        }
        return null;
    }
}
