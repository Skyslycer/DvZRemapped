package me.lojosho.dvzremapped.util;

public class TimeUtil {

    public static String formatTime(long time) {
        return (time / 3600) + ":" + ((time % 3600) / 60) + ":" + ((time % 3600) % 60);
    }

}
