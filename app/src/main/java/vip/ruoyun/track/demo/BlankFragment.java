package vip.ruoyun.track.demo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import vip.ruoyun.spiderman.tracker.annotation.AutoTrackerPage;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
@AutoTrackerPage(name = "我是fragment")
public class BlankFragment extends BaseFragment {

    public BlankFragment() {
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
        TextView mTextView = view.findViewById(R.id.mTextView);
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                System.out.println("1212");
            }
        });

    }

}
