package vip.ruoyun.track.demo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vip.ruoyun.track.core.annotation.SpiderManPage;


/**
 * A simple {@link Fragment} subclass.
 */
@SpiderManPage(name = "我是fragment")
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
