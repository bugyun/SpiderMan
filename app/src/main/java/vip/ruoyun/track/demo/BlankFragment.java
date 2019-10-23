package vip.ruoyun.track.demo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vip.ruoyun.track.core.SpiderManTracker;
import vip.ruoyun.track.core.annotation.SpiderManPage;


/**
 * A simple {@link Fragment} subclass.
 */
@SpiderManPage(name = "我是fragment")
public class BlankFragment extends BaseFragment {

    public BlankFragment() {
        // Required empty public constructor

        System.out.println("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        SpiderManTracker.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SpiderManTracker.onPause(this);
    }

    @Override
    public void onHiddenChanged(final boolean hidden) {
        super.onHiddenChanged(hidden);
        SpiderManTracker.onHiddenChanged(this, hidden);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        SpiderManTracker.setUserVisibleHint(this, isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SpiderManTracker.onDestroy(this);
    }
}
