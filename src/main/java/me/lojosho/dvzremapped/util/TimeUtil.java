package me.lojosho.dvzremapped.util;

public class TimeUtil {

    public static String formatTime(long time) {
        var hours = String.valueOf(time / 3600);
        var minutes = String.valueOf((time % 3600) / 60);
        var seconds = String.valueOf((time % 3600) % 60);

        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }

        return hours + ":" + minutes + ":" + seconds;
    }

}
