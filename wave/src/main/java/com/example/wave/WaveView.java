package com.example.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yueyue on 2017/5/18.
 */

public class WaveView extends View {
    private Paint paint;
    private int radio = 5;//设置园的半径

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radio = 5;//需要重置半径
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setColor(Color.RED);//设置画笔颜色为红色
        paint.setStyle(Paint.Style.STROKE);//设置样式为圆环
        paint.setStrokeWidth(radio / 3);//设置圆环的大小

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            radio += 5;

            //获得画笔的颜色
            int alpha = paint.getAlpha();
            alpha -= 5;
            if (alpha < 0) {
                alpha = 0;
            }

            paint.setAlpha(alpha);//用于设置画笔的透明度
            paint.setStrokeWidth(radio / 3);//设置圆环的大小

            invalidate();//强制重绘


        }
    };

    private float downX;
    private float downY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                downX=event.getX();
                downY=event.getY();
                initView();
                invalidate();//强制重绘
                break;

        }
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint.getAlpha() > 0 && downX>0 && downY>0) {
            canvas.drawCircle(downX, downY, radio, paint);
            mHandler.sendEmptyMessageDelayed(0, 50);

        }
    }
}
