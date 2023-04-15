package me.lojosho.dvzremapped.classes.monsters;

import me.lojosho.dvzremapped.classes.PlayerClass;
import me.lojosho.dvzremapped.util.MessagesUtil;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Monsters {

    public static final @NotNull HashMap<@NotNull String, @Nullable Monster> monsters = new HashMap<>();

    // Classes
    private static ZombieMonster zombieMonster = new ZombieMonster();
    private static SkeletonMonster skeletonMonster = new SkeletonMonster();
    private static CreeperMonster creeperMonster = new CreeperMonster();
    private static BroodmotherMonster broodmotherMonster = new BroodmotherMonster();
    private static SpiderMonster spiderMonster = new SpiderMonster();


    public static void add(@NotNull Monster monster) {
        monsters.put(monster.getId(), monster);
    }


    public static @Nullable Monster remove(@NotNull String id) {
        return monsters.remove(id);
    }

    public static @Nullable Monster get(@NotNull String id) {
        return monsters.get(id.toLowerCase());
    }

    public static List<PlayerClass> getRandomMonsterClasses() {
        Random random = new Random();
        List<PlayerClass> classes = new ArrayList<>();
        for (Monster monster : monsters.values()) {
            float randomnumber = random.nextFloat(0, 1);
            MessagesUtil.debug("Random # is " + randomnumber);
            if (randomnumber < monster.getChance()) {
                classes.add(monster);
            }
        }
        return classes;
    }

    public static Monster getMonsterClass(Material material) {
        for (Monster monster : monsters.values()) {
            if (monster.getSelectionMaterial() == material) return monster;
        }
        return null;
    }

}
