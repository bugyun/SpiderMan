package vip.ruoyun.track.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import vip.ruoyun.spiderman.tracker.annotation.AutoTrackerIgnore;
import vip.ruoyun.spiderman.tracker.annotation.AutoTrackerPage;
import vip.ruoyun.spiderman.tracker.annotation.PageMapping;
import vip.ruoyun.track.demo.databinding.ActivityMainBinding;

@AutoTrackerPage(
        name = "主界面",
        properties = {
                @PageMapping(key = "", value = ""),
                @PageMapping(key = "", value = "")
        })
@AutoTrackerIgnore
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainBinding.mButton.setOnClickListener(this);
        mMainBinding.mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                System.out.println("12123");
                if (v.getId() == R.id.mButton) {
                    System.out.println("12123");
                    return;
//                    throw new RuntimeException();
                } else {
                    System.out.println("12123");
                    return;
                }
            }
        });

        Button button = findViewById(R.id.mButton);

        button.setOnClickListener(v -> {
//                SpiderManTracker.trackView(v);
            System.out.println("12123");

        });
    }

    @Override
    public void onClick(View v) {
        System.out.println("12123");
//        SpiderManTracker.trackView(v);
    }


    //    @TestAnnotation("你好")
    public void test(String string) {
        System.out.println("我是第一行");

    }

    @TestAnnotation("方法")
    public void test01(String string) {
        System.out.println("我是第一行");

    }
}
