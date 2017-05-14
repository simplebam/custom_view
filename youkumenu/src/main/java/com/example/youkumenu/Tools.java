package com.example.youkumenu;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

/**
 * 作用：显示和隐藏指定控件
 * Created by yueyue on 2017/5/13.
 */
public class Tools {

    /**
     * 显示动画
     * @param viewGroup 群组
     * @param startOffset 延迟时间
     */
    public static void showView1(ViewGroup viewGroup,int startOffset) {
        //添加旋转动画
        RotateAnimation ra = new RotateAnimation(180, 360,
                viewGroup.getWidth() / 2, viewGroup.getHeight());
        //设置动画的相关性质
        ra.setDuration(500);
        ra.setFillAfter(true);
        ra.setStartOffset(startOffset);//延迟动画时间
        //开启动画
        viewGroup.startAnimation(ra);

        //如果下面作用在ViewGroup身上,它的子View是不受下面影响的
        //遍历孩子,使得它不再具备被点击事件,View动画不改变坐标
        for (int i=0;i<viewGroup.getChildCount();i++) {
            View childView = viewGroup.getChildAt(i);
            childView.setEnabled(true);
        }

       // ObjectAnimator.ofFloat()

    }

    /**
     * 隐藏动画
     * @param viewGroup  群组
     * @param startOffset 延迟时间
     */
    public static void hideView1(ViewGroup viewGroup, int startOffset) {
        //添加旋转动画
        RotateAnimation ra = new RotateAnimation(0, 180,
                viewGroup.getWidth() / 2, viewGroup.getHeight());
        //设置动画的相关性质
        ra.setDuration(500);
        ra.setFillAfter(true);
        ra.setStartOffset(startOffset);//延迟动画时间
        //开启动画
        viewGroup.startAnimation(ra);

        //如果下面作用在ViewGroup身上,它的子View是不受下面影响的
        //遍历孩子,使得它不再具备被点击事件,View动画不改变坐标
        for (int i=0;i<viewGroup.getChildCount();i++) {
            View childView = viewGroup.getChildAt(i);
            childView.setEnabled(false);
        }
    }


    /**
     * 利用属性动画进行隐藏
     * @param viewGroup  群组
     * @param startOffset 延迟时间
     */
    public static void hideView(ViewGroup viewGroup, int startOffset) {

        ObjectAnimator oa1 = ObjectAnimator.ofFloat(viewGroup, "rotation", 0, 180);
        oa1.setDuration(500);
        oa1.setStartDelay(startOffset);
        //设置旋转的中心点,默认旋转点在控件的左上角
        viewGroup.setPivotX(viewGroup.getWidth()/2);
        viewGroup.setPivotY(viewGroup.getHeight());

        oa1.start();

    }

    /**
     * 利用属性动画进行显示
     * @param viewGroup  群组
     * @param startOffset 延迟时间
     */
    public static void showView(ViewGroup viewGroup,int startOffset) {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(viewGroup, "rotation", 180, 360);
        oa1.setDuration(500);
        oa1.setStartDelay(startOffset);
        //设置旋转的中心点,默认旋转点在控件的左上角
        viewGroup.setPivotX(viewGroup.getWidth()/2);
        viewGroup.setPivotY(viewGroup.getHeight());
        oa1.start();

    }
}
