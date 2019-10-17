package vip.ruoyun.track.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

@TestAnnotation("MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    @TestAnnotation("你好")
    public void test(String string) {

    }

    @TestAnnotation("方法")
    public void test01(String string) {

    }

}
