package vip.ruoyun.plugin.utils;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class LogM {

    private static Logger mLogger = Logging.getLogger(LogM.class);

    /**
     * 输出默认颜色日志
     */
    public static boolean isLog = true;

    /**
     * 输出红色日志
     */
    public static boolean isHint = true;

    public static void log(String log) {
        if (isLog) {
            mLogger.warn(log);
        }
    }

    public static void log(String var1, Object var2) {
        if (isLog) {
            mLogger.warn(var1, var2);
        }
    }

    public static void log(String var1, Object var2, Object var3) {
        if (isLog) {
            mLogger.warn(var1, var2);
        }
    }

    public static void log(String var1, Object... var2) {
        if (isLog) {
            mLogger.warn(var1, var2);
        }
    }

    public static void log(String var1, Throwable var2) {
        if (isLog) {
            mLogger.warn(var1, var2);
        }
    }

    public static void hint(String log) {
        if (isHint) {
            mLogger.error(log);
        }
    }

    public static void hint(String var1, Object var2) {
        if (isHint) {
            mLogger.error(var1, var2);
        }
    }

    public static void hint(String var1, Object var2, Object var3) {
        if (isHint) {
            mLogger.error(var1, var2, var3);
        }
    }

    public static void hint(String var1, Object... var2) {
        if (isHint) {
            mLogger.error(var1, var2);
        }
    }

    public static void hint(String var1, Throwable var2) {
        if (isHint) {
            mLogger.error(var1, var2);
        }
    }
}
