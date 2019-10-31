package vip.ruoyun.spiderman.tracker;

import android.app.Application;
import android.content.Context;
import android.view.View;
import vip.ruoyun.spiderman.tracker.core.IActivityTracker;
import vip.ruoyun.spiderman.tracker.core.IFragmentTracker;
import vip.ruoyun.spiderman.tracker.core.IViewTracker;

/**
 * Created by ruoyun on 2019-10-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class AutoTracker {

    public static void main(Application application) {
        AutoTracker.with(application).track();
    }

    private static Context mContext;

    public static void setTrackData(View view, Object object) {
        view.setTag(R.id.auto_tracker_tag_view_value_data, object);
    }

    public static Builder with(Application application) {
        mContext = application;
        return new Builder(application);
    }

    private static class Builder {
        //上传策略  Upload strategy

        private String appChannel;

        private Application mApplication;

        private IFragmentTracker mFragmentTracker;

        private IViewTracker mViewTracker;

        private IActivityTracker mActivityTracker;

        private String businessType;

        private boolean isDebugUploadInfo;

        private int maxUploadNum;

        private boolean isDebug;


        private Builder(Application application) {
            this.mApplication = application;
        }

        public Builder isDebugUploadInfo(boolean isDebugUploadInfo) {
            this.isDebugUploadInfo = isDebugUploadInfo;
            return this;
        }

        public Builder maxUploadNum(int maxUploadNum) {
            this.maxUploadNum = maxUploadNum;
            return this;
        }

        public Builder appChannel(final String appChannel) {
            this.appChannel = appChannel;
            return this;
        }

        public Builder businessType(String businessType) {
            this.businessType = businessType;
            return this;
        }

        public Builder viewTracker(IViewTracker viewTracker) {
            this.mViewTracker = viewTracker;
            return this;
        }

        public Builder fragmentTracker(IFragmentTracker fragmentTracker) {
            this.mFragmentTracker = fragmentTracker;
            return this;
        }

        public Builder activityTracker(IActivityTracker activityTracker) {
            this.mActivityTracker = activityTracker;
            return this;
        }

        public Builder isDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public void track() {
            if (mActivityTracker != null) {
                mApplication.registerActivityLifecycleCallbacks(mActivityTracker);
            }
        }

    }

}
