package vip.ruoyun.template;

public class LogM {

    public static void log(Object log) {
        if (ConstantValue.isLog) {
            System.out.println(log);
        }
    }

}
