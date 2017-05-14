package com.example.youkumenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 利用系统控件组合形式实现优酷菜单的显示以及隐藏的效果
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private RelativeLayout level3;
    private ImageView icon_menu;
    private RelativeLayout level2;
    private ImageView icon_home;
    private RelativeLayout level1;

    /**
     * 是否显示第三圆环
     * true:显示
     * false隐藏
     */
    private boolean isShowLevel3 = true;

    /**
     * 是否显示第二圆环
     * true:显示
     * false隐藏
     */
    private boolean isShowLevel2 = true;


    /**
     * 是否显示第一圆环
     * true:显示
     * false隐藏
     */
    private boolean isShowLevel1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    /**
     * 初始化我们需要的组件
     */
    private void initView() {
        level3 = (RelativeLayout) findViewById(R.id.level3);
        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        icon_home = (ImageView) findViewById(R.id.icon_home);
        level1 = (RelativeLayout) findViewById(R.id.level1);

        //添加监听
        icon_menu.setOnClickListener(this);
        icon_home.setOnClickListener(this);
    }

    /**
     * 事件监听的处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_home:
                //如果二跟三都是显示在屏幕上了,果已经显示在屏幕上,那么应该隐藏
                if (isShowLevel2) {
                    Tools.hideView(level2, 0);
                    isShowLevel2 = false;

                    if (isShowLevel3) {
                        //隐藏三级菜单
                        isShowLevel3 = false;
                        Tools.hideView(level3, 200);
                    }

                } else {
                    Tools.showView(level2,0);
                    isShowLevel2 = true;
                }
                break;
            case R.id.icon_menu:
                if (isShowLevel3) {
                    //如果已经显示在屏幕上,那么应该隐藏
                    Tools.hideView(level3, 0);
                    isShowLevel3 = false;
                } else {
                    Tools.showView(level3,0);
                    //Log.i(TAG, "Level3应该显示");
                    isShowLevel3 = true;
                }
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown方法执行了"+keyCode+"   "+event.getKeyCode());
        if (keyCode==KeyEvent.KEYCODE_MENU || keyCode==KeyEvent.KEYCODE_UNKNOWN)  {
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //在安卓4.0之后,中间键不再是Home跟Menu键,4.0之后中间那个键是不知名的(所以无法检测),第三键返回的是0
        if (keyCode==KeyEvent.KEYCODE_MENU || keyCode==KeyEvent.KEYCODE_UNKNOWN)  {
            Log.i(TAG,"onKeyUp方法执行了"+keyCode+"   "+event.getKeyCode());
            if (isShowLevel1) {
                Tools.hideView(level1,0);
                isShowLevel1=false;
                if (isShowLevel2) {
                    Tools.hideView(level2,200);
                    isShowLevel2=false;
                    if (isShowLevel3) {
                        Tools.hideView(level3,400);
                        isShowLevel3=false;
                    }

                }

            } else {
                Tools.showView(level1,0);
                isShowLevel1=true;
                if (!isShowLevel2) {
                    Tools.showView(level2,200);
                    isShowLevel2=true;
                }
            }

        }
        return super.onKeyUp(keyCode, event);
    }
}
