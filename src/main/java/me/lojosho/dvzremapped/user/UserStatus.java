package me.lojosho.dvzremapped.user;

/**
 * Represents the status of a {@link User}
 */
public enum UserStatus {
    /**
     * Neither {@link UserStatus#DWARF dwarf} or {@link UserStatus#MONSTER monster}
     */
    LIMBO,
    /**
     * A dwarf status
     */
    DWARF,
    /**
     * A monster status
     */
    MONSTER
}
