package com.example.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 主要使用了自定义的MyScroller
 * Created by yueyue on 2017/5/16.
 */

public class MyViewPager extends ViewGroup {
    private GestureDetector detector;
    private MyScroller myScroller;

    private Context mContext;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        this.mContext=context;
    }

    /**
     * 初始化UI
     *
     * @param context
     */
    private void initView(Context context) {
        myScroller = new MyScroller();
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollBy((int) distanceX, 0);

                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }

    }

    //按下的x跟y轴坐标
    private float downStartX;
    private float downStartY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        detector.onTouchEvent(ev);//可以避免因onTouchEvent中的ACTION_DOWN没有执行导致闪动问题
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("onInterceptTouchEvent==ACTION_DOWN");
                downStartX = ev.getX();
                downStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onInterceptTouchEvent==ACTION_MOVE");
                float downDownX = ev.getX();
                float downDownY = ev.getY();

                int distanceX= (int) Math.abs(downDownX-downStartX);
                int distanceY= (int) Math.abs(downDownY-downStartY);

                if (distanceX>distanceY && distanceX> DensityUtil.dip2px(mContext,5)) {
                    isIntercept=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onInterceptTouchEvent==ACTION_UP");
                break;

        }


        return isIntercept;
    }

    private float startX;
    private int currentIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("onTouchEvent==ACTION_DOWN");
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onTouchEvent==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onTouchEvent==ACTION_UP");
                float endX = event.getX();

                //下标位置
                int tempIndex = currentIndex;
                if ((endX - startX) > getWidth() / 2) {
                    tempIndex--;
                } else if ((startX - endX) > getWidth() / 2) {
                    tempIndex++;
                }

                //根据下标位置移动到指定的界面
                scrollToPager(tempIndex);

                break;
        }

        return true;
    }


    public void scrollToPager(int tempIndex) {

        //屏蔽非法值
        if (tempIndex < 0) {
            tempIndex = 0;
        } else if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }

        currentIndex = tempIndex;

        //移动到指定的界面
        //scrollTo(currentIndex * getWidth(), 0);
        int distanceX = currentIndex * getWidth() - getScrollX();
        myScroller.startScroll(getScrollX(), getScrollY(), distanceX, 0);

        invalidate();//这个方法会重新调用onDraw()以及computeScroll()方法

        //RadioGroup跳到这位置
        if (mOnPagerChangeListener != null) {
            mOnPagerChangeListener.onScrollToPager(currentIndex);
        }

    }

    //移动
    @Override
    public void computeScroll() {
        super.computeScroll();//一般里面是空的话都可以选删除
        if (myScroller.computeScrollOffset()) {
            //得到需要移动到的坐标
            float currentX = myScroller.getCurrentX();
            //移动到我们需要的坐标
            scrollTo((int) currentX, 0);

            invalidate();//重绘,递归调用
        }

    }

    private onPagerChangeListener mOnPagerChangeListener;

    public void setOnPagerChangeListener(onPagerChangeListener onPagerChangeListener) {
        this.mOnPagerChangeListener = onPagerChangeListener;
    }

    interface onPagerChangeListener {
        public abstract void onScrollToPager(int position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //子View测量自己的宽高,http://blog.csdn.net/xmxkf/article/details/51490283
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
