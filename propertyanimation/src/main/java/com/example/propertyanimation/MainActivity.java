package com.example.propertyanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_animation;
    private Button testTweenAnimation;
    private Button testPropertyAnimation;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    /**
     * 初始化UI空间
     */
    private void initView() {
        iv_animation = (ImageView) findViewById(R.id.iv_animation);
        iv_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了图片",
                        Toast.LENGTH_SHORT).show();
            }
        });
        testTweenAnimation = (Button) findViewById(R.id.testTweenAnimation);
        testTweenAnimation.setOnClickListener(this);
        testPropertyAnimation = (Button) findViewById(R.id.testPropertyAnimation);
        testPropertyAnimation.setOnClickListener(this);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testTweenAnimation:
                testTweenAnimation();
                break;

            case R.id.testPropertyAnimation:
                testPropertyAnimation();
                break;

            case R.id.reset:
                resetAnimation();
                break;
        }
    }

    /**
     * 设置属性动画
     */
    private void testPropertyAnimation() {
        //iv_animation.setTranslationX(float translationX);
        //ofFloat主要看setTranslationX(float translationX)中的参数类型决定
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_animation, "translationX",
                0, iv_animation.getWidth());
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_animation, "translationY",
                0, iv_animation.getHeight());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(oa1, oa2);
        animatorSet.setDuration(500);
        animatorSet.start();
          //另外一种写法
//        iv_animation.animate()
//                 .translationXBy(iv_animation.getWidth())
//                 .translationYBy(iv_animation.getWidth())
//                 .setDuration(2000)
//                 .setInterpolator(new BounceInterpolator())
//                 .start();


    }

    /**
     * 清除动画
     */
    private void resetAnimation() {
        //清除图片上的动画
        // ta.setFillAfter(false);
        iv_animation.clearAnimation();
    }

    /**
     * 设置平移动画
     */
    private void testTweenAnimation() {
        //View动画跟Drawable动画不能改变图片的位置

        TranslateAnimation ta = new TranslateAnimation(0, iv_animation.getWidth(),
                0, iv_animation.getHeight());
        ta.setDuration(500);
        // ta.setFillAfter(true);
        iv_animation.startAnimation(ta);
    }
}
