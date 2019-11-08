package vip.ruoyun.spiderman.tracker.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ruoyun on 2019-10-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PageMapping {

    String key();

    String value();
}
