package vip.ruoyun.track.demo;

import android.util.Log;
import androidx.fragment.app.Fragment;

/**
 * Created by ruoyun on 2019-10-22.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        if (hashCode() == 1) {
            return;
        }
        int i = 2;
        Log.e("zyh", i + "");
    }

    @Override
    public void onPause() {


        super.onPause();
    }

    @Override
    public void onHiddenChanged(final boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
