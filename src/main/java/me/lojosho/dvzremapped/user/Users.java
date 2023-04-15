package me.lojosho.dvzremapped.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Manages fetching the internal {@link org.bukkit.entity.Player} wrapper, {@link User}
 */
public class Users {
    private static final @NotNull Map<@NotNull UUID, @Nullable User> users = new HashMap<>();

    /**
     * Associates the {@link UUID uuid} of the specified {@link User user} with the specified {@link User user}
     * @param user {@link User User} value to be associated with the users {@link UUID uuid}
     */
    public static void add(@NotNull User user) {
        users.put(user.getUniqueId(), user);
    }

    /**
     * Removes the mapped {@link User user} for the specified {@link UUID uuid} if present
     * @param uuid {@link UUID UUID} whose mapped {@link User user} is to be removed
     * @return The previous {@link User user} associated with the {@link UUID uuid}, or {@code null} if there was no mapping for the {@link UUID uuid} previously
     */
    public static @Nullable User remove(@NotNull UUID uuid) {
        return users.remove(uuid);
    }

    /**
     * Returns the {@link User user} to which the specified {@link UUID uuid} is mapped, or {@code null} if there is no mapped {@link User user} for the {@link UUID uuid}
     * @param uuid {@link UUID UUID} whose mapped {@link User user} is to be returned
     * @return The mapped {@link User user} associated with the specified {@link UUID uuid}, or {@code null} if there is no mapped {@link User user} for the {@link UUID uuid}
     */
    public static @Nullable User get(@NotNull UUID uuid) {
        return users.get(uuid);
    }

    /**
     * Returns a {@link Collection<User>} view of the values mapped
     * @return A {@link Collection<User>} view of the values mapped
     */
    public static @NotNull Collection<@Nullable User> values() {
        return users.values();
    }

    public static @NotNull Collection<@Nullable User> valuesRandomized() {
        List<User> randomList = users.values().stream().toList();
        Collections.shuffle(randomList);
        return randomList;
    }

    public static boolean contains(UUID uniqueId) {
        return values().contains(uniqueId);
    }

    public static int getDwarvesCounted() {
        int amount = 0;
        for (User user : values()) {
            if (user.getStatus() == UserStatus.DWARF) {
                amount = amount + 1;
            }
        }
        return amount;
    }
}
