package com.example.adstitle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    /**
     * 记录先前的位置
     */
    private int prePosition = 0;
    /**
     * 是否拖拽状态
     */
    private boolean isDragging=false;

    //装载图片
    private ArrayList<ImageView> imageViews;

    // 图片资源ID
    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e};

    // 图片标题集合
    private final String[] imageDescriptions = {
            "尚硅谷波河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            //移除所有的消息,防止内存泄漏
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imageViews = new ArrayList<>();

        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            imageViews.add(imageView);

            //设置小圆点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置第一个小圆点默认选中
            if (i == 0) {
                point.setEnabled(true);
            } else {
                int pointDis = DensityUtil.dip2px(this, 8.0f);
                params.leftMargin = pointDis;
                point.setEnabled(false);

            }

            //向布局里面添加point
            point.setLayoutParams(params);
            ll_point_group.addView(point);

        }


        //设置为中间的位置,且需要设置为图片的倍数
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();
        viewpager.setCurrentItem(item);
        tv_title.setText(imageDescriptions[0]);

        viewpager.setAdapter(new MyPagerAdapter());

        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置延迟4秒之后再发送
        mHandler.sendEmptyMessageDelayed(AUTO_CHANGE, 4000);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
    }

    private static final int AUTO_CHANGE = 0x0001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_CHANGE:
                    int item = viewpager.getCurrentItem() + 1;
                    viewpager.setCurrentItem(item);
                    break;
            }
            //设置延迟4秒之后再发送
            mHandler.sendEmptyMessageDelayed(AUTO_CHANGE, 4000);

        }
    };

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当页面滚动的时候
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //当滑动到当前页面的时候
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % imageViews.size();
            //设置文本对应的信息
            tv_title.setText(imageDescriptions[realPosition]);

            //把小圆点设置为红色
            ll_point_group.getChildAt(realPosition).setEnabled(true);
            //把小圆点设置会灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);

            prePosition = realPosition;


        }

        //但页面滑动状态被改变的时候
        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                //空闲状态下
                isDragging=false;
                //设置延迟4秒之后再发送
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(AUTO_CHANGE, 4000);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                //滚动状态下
            } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                //拖拽状态下
                isDragging=true;
                //移除消息队列中的消息
                mHandler.removeCallbacksAndMessages(null);
            }

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //return imageViews.size();
            return Integer.MAX_VALUE;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //因为position不求%的话可能会超出imageIds的数组长度
            ImageView imageView = imageViews.get(position % imageViews.size());
            //这一步尤其重要,需要往容器添加imageView,以便destroyItem方法使用
            container.addView(imageView);

            //按照常理,我们很应该在ImageView这里添加监听,但这样会导致小bug的,所以不在这里设置监听
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://手指按下
                            Log.e(TAG, "onTouch==手指按下");
                            mHandler.removeCallbacksAndMessages(null);
                            break;

                        case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                            break;
                        case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                            Log.e(TAG, "onTouch==事件取消");
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                        case MotionEvent.ACTION_UP://手指离开
                            Log.e(TAG, "onTouch==手指离开");
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 4000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG,"点击事件");
                    int position = (int) v.getTag()%imageViews.size();
                    Toast.makeText(MainActivity.this, "text=="+imageDescriptions[position],
                            Toast.LENGTH_SHORT).show();

                }
            });

            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //销毁界面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //   super.destroyItem(container, position, object);
        }
    }
}
