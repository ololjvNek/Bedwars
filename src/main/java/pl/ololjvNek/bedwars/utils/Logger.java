package pl.ololjvNek.bedwars.utils;

import pl.ololjvNek.bedwars.Main;

import java.util.logging.Level;


public final class Logger
{
    public static void info(final String... logs) {
        for (final String s : logs) {
            log(Level.INFO, s);
        }
    }

    public static void warning(final String... logs) {
        for (final String s : logs) {
            log(Level.WARNING, s);
        }
    }

    public static void severe(final String... logs) {
        for (final String s : logs) {
            log(Level.SEVERE, s);
        }
    }

    public static void log(final Level level, final String log) {
        Main.getPlugin().getLogger().log(level, log);
    }

    public static void exception(final Throwable cause) {
        cause.printStackTrace();
    }
}