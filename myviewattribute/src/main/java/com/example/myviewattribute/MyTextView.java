package com.example.myviewattribute;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by yueyue on 2017/5/15.
 */

public class MyTextView extends View {
    private static final String TAG = MyTextView.class.getSimpleName();
    private final int myAge;
    private final String myName;
    private final int mySex;

    private Paint paint;
    private final Bitmap bitmap;


    /**
     * 一般xml加载这张图片的时候都会使用这种构造方法
     *
     * @param context 上下文环境
     * @param attrs   属性集合
     */
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿


        //获取属性三种方式
        //1.用命名空间取获取
        String age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_age");
        String name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_name");
        String bg = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_bg");
//        System.out.println("age=="+age+",name=="+name+",bg==="+bg);

        //2.遍历属性集合
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            System.out.println(attrs.getAttributeName(i) + "=====" + attrs.getAttributeValue(i));
        }

        //3.使用系统工具，获取属性
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
//        for (int i = 0; i < typedArray.getIndexCount(); i++) {
//            int index = typedArray.getIndex(i);
//
//            switch (index) {
//                case R.styleable.MyTextView_my_age:
//                    int my_Age = typedArray.getInt(index, 0);
//                    break;
//                case R.styleable.MyTextView_my_name:
//                    String my_Name = typedArray.getString(index);
//                    break;
//                case R.styleable.MyTextView_my_bg:
//                    Drawable drawable = typedArray.getDrawable(index);
//                    BitmapDrawable drawable1 = (BitmapDrawable) drawable;
//                    Bitmap my_Bg = drawable1.getBitmap();
//                    break;
//            }
//        }

        //3.使用系统工具，获取属性
        //在attrs属性集合中找到在atrrs文件中的MyTextView属性,并且存在typedArray中
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        //遍历,获得自己定义的属性
        myAge = typedArray.getInt(R.styleable.MyTextView_my_age, 0);
        myName = typedArray.getString(R.styleable.MyTextView_my_name);
        //默认是男孩子
        mySex = typedArray.getInt(R.styleable.MyTextView_my_sex, 0);
        Drawable myDrawable = typedArray.getDrawable(R.styleable.MyTextView_my_bg);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) myDrawable;
        bitmap = bitmapDrawable.getBitmap();

        //用户之后需要回收
        typedArray.recycle();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "myName:" + myName + "   myAge:" + myAge);//MyTextView: myName:杨过
        canvas.drawText("myName:" + myName + "   myAge:" + myAge, 50, 50, paint);
        canvas.drawText("mySex:" + mySex, 250, 300, paint);
        canvas.drawBitmap(bitmap, 60, 60, paint);

    }
}
