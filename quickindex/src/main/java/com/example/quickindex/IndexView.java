package com.example.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yueyue on 2017/5/17.
 * * 作用：快速索引
 * 绘制快速索引的字母
 * 1.把26个字母放入数组
 * 2.在onMeasure计算每条的高itemHeight和宽itemWidth,
 * 3.在onDraw和wordWidth,wordHeight,wordX,wordY
 * <p/>
 * 手指按下文字变色
 * 1.重写onTouchEvent(),返回true,在down/move的过程中计算
 * int touchIndex = Y / itemHeight; 强制绘制
 * <p/>
 * 2.在onDraw()方法对于的下标设置画笔变色
 * <p/>
 * 3.在up的时候
 * touchIndex  = -1；
 * 强制绘制
 */

public class IndexView extends View {

    /**
     * 每条的宽和高
     */
    private int itemWidth;
    private int itemHeight;


    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private Paint paint;


    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setColor(Color.WHITE);//画笔颜色默认为白色
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体字
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        itemWidth=getWidth();
//        itemHeight=getHeight()/words.length;
// 跟下面两个方法的区别:http://www.cnblogs.com/summerpxy/p/4983600.html

        //获得宽度
        itemWidth = getMeasuredWidth();
        //获得item的高度
        itemHeight = getMeasuredHeight() / words.length;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < words.length; i++) {
            //选中的为灰色,未选中的为白色
            if (touchIndex==i) {
                paint.setColor(Color.GRAY);
            } else {
                paint.setColor(Color.WHITE);

            }


            //获得每一个字
            String word = words[i];//A
            //设置一个矩形,框住字体
            Rect rect = new Rect();
            //画笔
            //0,1的取一个字母
            //将字体边界给rect
            paint.getTextBounds(word, 0, 1, rect);


            //字母的高和宽
            int wordWidth = rect.width();
            int wordHeight = rect.height();

            //获得当前字母的坐标
            int wordX = itemWidth / 2 - wordWidth / 2;
            int wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;


            canvas.drawText(words[i], wordX, wordY, paint);

        }

    }

    //当前的索引
    private int touchIndex=-1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchY = event.getY();
                int index= (int) (touchY / itemHeight);//字母索引

                if (index!=touchIndex) {
                    touchIndex=index;
                    invalidate();//强制重绘

                    if (onIndexChangeListener!=null) {
                        //设置到当前的字面
                        onIndexChangeListener.onIndexChange(words[index]);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                touchIndex=-1;
                invalidate();//强制重绘,所有的字母设置为白色
                break;
        }

        return true;
    }


    /**
     * 字母下标索引变化的监听器
     */
    public interface OnIndexChangeListener{

        /**
         * 当字母下标位置发生变化的时候回调
         * @param word 字母（A~Z）
         */
        void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    /**
     * 设置字母下标索引变化的监听
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }

}
