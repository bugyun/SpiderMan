package vip.ruoyun.track.utils;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import vip.ruoyun.template.TemplatePlugin;
import vip.ruoyun.template.constant.ConstantValue;

public class LogM {

    private static Logger mLogger = Logging.getLogger(TemplatePlugin.class);

    public static void log(Object log) {
        if (ConstantValue.isLog) {
            mLogger.warn("{}", log);
        }
    }

    public static void log(String var1, Object var2) {
        if (ConstantValue.isLog) {
            mLogger.warn(var1, var2);
        }
    }

    public static void hint(String log) {
        mLogger.error(log);
    }

    public static void hint(String var1, Object var2) {
        mLogger.error(var1, var2);
    }

    public static void hint(String var1, Object var2, Object var3) {
        mLogger.error(var1, var2, var3);
    }

    public static void hint(String var1, Object... var2) {
        mLogger.error(var1, var2);
    }

    public static void hint(String var1, Throwable var2) {
        mLogger.error(var1, var2);
    }
}
