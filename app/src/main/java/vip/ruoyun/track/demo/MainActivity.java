package vip.ruoyun.track.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import vip.ruoyun.track.core.annotation.SpiderManIgnore;
import vip.ruoyun.track.core.annotation.SpiderManPage;
import vip.ruoyun.track.demo.databinding.ActivityMainBinding;

@SpiderManPage(name = "主界面")
@SpiderManIgnore
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
