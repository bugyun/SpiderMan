package vip.ruoyun.template.utils;

import vip.ruoyun.template.constant.ConstantValue;

public class LogM {

    public static void log(Object log) {
        if (ConstantValue.isLog) {
            System.out.println(log);
        }
    }

}
