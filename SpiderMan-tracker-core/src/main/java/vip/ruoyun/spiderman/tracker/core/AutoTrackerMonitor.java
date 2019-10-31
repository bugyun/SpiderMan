package vip.ruoyun.spiderman.tracker.core;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by ruoyun on 2019-10-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class AutoTrackerMonitor {

    public static void trackView(View view) {

    }

    /**
     * 获得焦点
     */
    public static void onResume(Fragment fragment) {
        //FragmentTransaction 切换 getUserVisibleHint()一直为 true,fragment.isHidden()为 false
        //viewpager 切换 getUserVisibleHint()为 true,fragment.isHidden()一直为 false
        if (!fragment.isHidden() && fragment.getUserVisibleHint()) {//表示 fragment 显示
            //此时来标记第一个 fragment 打开

        }
    }

    /**
     * 失去焦点
     */
    public static void onPause(Fragment fragment) {

    }

    /**
     * fragment 的方法，用来监听 tab 切换 fragment
     */
    public static void onHiddenChanged(Fragment fragment, boolean hidden) {
        if (!hidden) {//显示
            //此时用来表示界面打开

        }

    }

    /**
     * 用来监听 ViewPager 中 fragment 的切换
     */
    public static void setUserVisibleHint(Fragment fragment, boolean isVisibleToUser) {

    }

}
