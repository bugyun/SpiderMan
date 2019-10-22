package vip.ruoyun.track.core;

/**
 * Created by ruoyun on 2019-10-09.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class SpiderMan {


    private class Builder {

        //上传策略  Upload strategy

        /**
         * 打开界面结束上传
         */
        public Builder enableActivity() {
            return this;
        }


        /**
         * 打开 fragment 界面切换上传
         */
        public Builder enableFragment() {
            return this;
        }
    }

}
