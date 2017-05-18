package com.example.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by yueyue on 2017/5/18.
 */

public class SlideLayout extends FrameLayout {

    private static final String TAG = SlideLayout.class.getSimpleName();
    private View contentView;
    private View menuView;

    /**
     * 滚动者
     */
    private Scroller scroller;

    /**
     * Content的宽
     */
    private int contentWidth;
    private int menuWidth;
    private int viewHeight;//他们的高都是相同的

    private Context context;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
    }

    /**
     * 当布局文件加载完成的时候回调这个方法,在onResume()方法之前
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 当布局文件以及代码创建控件加载完成的时候回调这个方法,在onResume()方法之后
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //找到我们需要的组件
        contentView = getChildAt(0);//item_content
        menuView = getChildAt(1);//item_menu
    }

    /**
     * 测量,在此获得宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得contentView的高度
        contentWidth = contentView.getMeasuredWidth();
        //获得menuView的高度
        menuWidth = menuView.getMeasuredWidth();
        //获得SlideLayout的高度
        viewHeight = this.getMeasuredHeight();
    }

    /**
     * 布局子View的在布局中的位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //指定contentView以及menuView的位置
        contentView.layout(0, 0, contentWidth, viewHeight);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }


    private float startX;
    private float startY;
    private float downX;//只赋值一次
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);//没有它点击事件跟长按点击事件无效
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的坐标
                downX=startX = event.getX();
                downY=startY = event.getY();

                Log.i(TAG,"onTouchEvent()方法的ACTION_DOWN执行了");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"onTouchEvent()方法的ACTION_MOVE执行了");
                //记录按下的坐标
                float endX = event.getX();
                float endY=event.getY();

                float distanceX = (int) (endX - startX);//绝对为负数的

                //负向右,正向左,toScrollX代表现在的坐标
                int toScrollX = (int) (getScrollX() - distanceX);//其实就是以前的坐标-距离

                if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                } else if (toScrollX < 0) {
                    toScrollX = 0;
                }

                scrollTo(toScrollX, getScrollY());//此时的getScrollY()为0

                //记录按下的坐标
                startX = event.getX();
                startY = event.getY();

                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX-downX);
                float DY = Math.abs(endY-downY);
                if(DX > DY&&DX>DensityUtil.dip2px(context,8)){
                    //水平方向滑动
                    //响应侧滑
                    //反拦截-事件给SlideLayout
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"onTouchEvent()方法的ACTION_UP执行了");
                int scrollX = getScrollX();
                if (scrollX < menuWidth/2) {
                    //关闭策划栏
                    closeMenu();
                } else {
                    //打开策划栏
                    openMenu();
                }

                break;
        }

        return true;
    }

    /**
     * 关闭策划栏
     */
    public void closeMenu() {
        int distanceX = 0-getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();//强制重绘,自动调用computeScroll()和onDraw()
        if (onStateChangeListenter!=null) {
            onStateChangeListenter.onClose(this);
        }
    }


    /**
     * 打开策划栏
     */
    private void openMenu() {
        int distanceX = menuWidth-getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();//强制重绘,自动调用computeScroll()和onDraw()
        if (onStateChangeListenter!=null) {
            onStateChangeListenter.onOpen(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //还没完成的情况下
        if (scroller.computeScrollOffset()) {
            //获得当前x坐标,不断移动
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();//递归调用
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //super.onInterceptTouchEvent(ev);
        boolean isIntercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的坐标
                downX = startX = event.getX();
                Log.i(TAG,"onInterceptTouchEvent()方法的ACTION_DOWN执行了");
                if (onStateChangeListenter!=null) {
                    onStateChangeListenter.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"onInterceptTouchEvent()方法的ACTION_MOVE执行了");
                //记录按下的坐标
                float endX = event.getX();

                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX - downX);
                if (DX > DensityUtil.dip2px(context, 8)) {
                    isIntercept = true;
                }

                //记录按下的坐标
                downX = startX = event.getX();

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"onInterceptTouchEvent()方法的ACTION_UP执行了");
                break;
        }


        return isIntercept;
    }


    /**
     * 监听SlideLayout状态的改变
     */
    public interface OnStateChangeListenter{
        void onClose(SlideLayout layout);
        void onDown(SlideLayout layout);
        void onOpen(SlideLayout layout);
    }

    private  OnStateChangeListenter onStateChangeListenter;

    /**
     * 设置SlideLayout状态的监听
     * @param onStateChangeListenter
     */
    public void setOnStateChangeListenter(OnStateChangeListenter onStateChangeListenter) {
        this.onStateChangeListenter = onStateChangeListenter;
    }
}
