package com.example.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by yueyue on 2017/5/15.
 */

public class MyToggleButton extends View implements View.OnClickListener {

    private static final String TAG = "MyToggleButton";
    //背景图片
    private Bitmap backgroundBitmap;
    //滑动图片
    private Bitmap slidingBitmap;
    //开关状态
    private boolean isOpen = false;

    //滑动距离
    private int slideLeft = 0;


    /**
     * 距离左边最大距离
     */
    private int slidLeftMax;
    private Paint paint;

    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始化相关信息
     */
    private void initView() {
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿

        //加载背景图片跟滑动图片
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        //求出最大的滑动距离
        slidLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();
        //设置点击事件
        setOnClickListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果按照下面这样输进去,根据源码,系统可能会给一个0,0(源码里面会帮你选择它说的最优尺寸)
//        super.onMeasure(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        //保存我们需要的尺寸
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.i(TAG, "onDraw方法");
        //使用画笔把背景图片跟滑动图片绘制到控件MyToggleButton
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slidingBitmap, slideLeft, 0, paint);
    }

    @Override
    public void onClick(View v) {
        if (isClickEnable) {

            isOpen = !isOpen;
            flushView();
            Log.i(TAG,"点击事件消费了");

//        if (isOpen) {
//            slideLeft = slidLeftMax;
//        } else {
//            slideLeft = 0;
//        }
        }
    }

    //开始位置
    private float startX;
    //上一次的位置
    private float lastX;

    //是否是点击事件处理(事件移动距离大于5则为话滑动事件,小于5则为点击事件)
    private boolean isClickEnable = true;


    //设置滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);// 这一句不能少，否则无法触发onclick事件
        //详情看博客:http://blog.csdn.net/cyp331203/article/details/40779335
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClickEnable = true;//点下也可能是click事件的,之后看距离
                lastX = startX =  event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float distanceX = endX - startX;
                //不断累加
                slideLeft += distanceX;

                if (slideLeft < 0) {
                    slideLeft = 0;
                } else if (slideLeft > slidLeftMax) {
                    slideLeft = slidLeftMax;
                }

                //强制重绘
                invalidate();
                startX = endX;

                //如果大于5的话,则为滑动事件
                if (Math.abs(endX - lastX) > DensityUtil.dip2px(getContext(),10)) {
                    isClickEnable = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!isClickEnable) {
                    if (slideLeft < slidLeftMax / 2) {
                        //不够1/2
                        isOpen = false;
                        slideLeft = 0;
                    } else {
                        slideLeft = slidLeftMax;
                        isOpen = true;
                    }
                    Log.i(TAG,"滑动...");

                    flushView();
                }


                break;
        }

        return true;
    }

    /**
     * 强制重绘
     */
    private void flushView() {
        //开状态距离为0,关状态距离为slidLeftMax
        slideLeft = isOpen == true ? slidLeftMax : 0;
        //强制重绘
        invalidate();
    }
}
