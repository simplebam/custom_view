package com.example.myviewpager;

import android.os.SystemClock;

/**
 * Created by yueyue on 2017/5/16.
 */

public class MyScroller {


    /**
     * 起始位置
     */
    private float startX;
    private float startY;
    /**
     * 移动的距离
     */
    private int distanceX;
    private int distanceY;


    /**
     * 移动是否完成了
     */
    private boolean isFinish;

    //开始时间
    private long startTime;

    //移动的总时间
    private long totalTime = 500;

    //当前的位置
    private float currentX;

    //返回当前的位置
    public float getCurrentX() {
        return currentX;
    }

    public void startScroll(float startX, float startY, int distanceX, int distanceY) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.isFinish = false;
        this.startTime = SystemClock.uptimeMillis();

    }


    /**
     * 速度
     * 求移动一小段的距离
     * 求移动一小段对应的坐标
     * 求移动一小段对应的时间
     * true:正在移动
     * false:移动结束
     *
     * @return
     */
    public boolean computeScrollOffset() {

        if (isFinish) {
            return false;
        }

        //记录结束时间
        long endTime = SystemClock.uptimeMillis();

        //移动的时间
        long passTime = endTime - startTime;


        if (passTime < totalTime) {
            //移动的速度
            //  float velocity = distanceX / totalTime;
            //移动这个一小段对应的距离
            float distanceSamllX = passTime* distanceX/totalTime;

            currentX = startX + distanceSamllX;

        } else {
            isFinish = true;
            currentX = distanceX + startX;
        }


        return true;
    }
}
