package com.example.myviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private MyViewPager myviewpager;



    private RadioGroup rg_main;

    private int[] ids = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        myviewpager = (MyViewPager) findViewById(R.id.myviewpager);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        initViewPager();

        initListener();
    }

    /**
     * 监听注册器
     */
    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myviewpager.scrollToPager(checkedId);//checkedId定义时候是0-5,刚好跟我们准备的页面位置一样
            }
        });

        //回掉
        myviewpager.setOnPagerChangeListener(new MyViewPager.onPagerChangeListener() {
            @Override
            public void onScrollToPager(int position) {
                rg_main.check(position);
            }
        });
    }

    //初始化ViewPager
    private void initViewPager() {
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            myviewpager.addView(imageView);

            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);//设置id,不设置的话可能会导致无法是唯一的
            if (i==0) {
                radioButton.setChecked(true);
            }
            rg_main.addView(radioButton);

        }

        //添加测试界面
        View testView = View.inflate(this, R.layout.test, null);
        myviewpager.addView(testView,2);


    }
}
