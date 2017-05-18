package com.example.slidingmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv_main;
    private List<MyBean> myBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        //找到我们需要的组件
        lv_main = (ListView) findViewById(R.id.lv_main);

        //数据准备
        initData();

        lv_main.setAdapter(new MyDataAdapter());
    }

    private void initData() {
        myBeans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            myBeans.add(new MyBean("content" + i));
        }

    }


    private class MyDataAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return myBeans.size();
        }

        @Override
        public MyBean getItem(int position) {
            return myBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_main, null);
                viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHolder.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.item_content.setText(getItem(position).getName());

            viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, getItem(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myBeans.remove(position);
                    //刷新ListView
                    notifyDataSetChanged();
                }
            });

            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnStateChangeListenter(new MyOnStateChangeListenter());


            return convertView;
        }
    }

    static class ViewHolder {
        public TextView item_content;
        public TextView item_menu;
    }

    private SlideLayout mSlideLayout;

    class MyOnStateChangeListenter implements SlideLayout.OnStateChangeListenter {

        @Override
        public void onClose(SlideLayout layout) {
            if (mSlideLayout == layout) {
                mSlideLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (mSlideLayout != null && mSlideLayout != layout) {
                mSlideLayout.closeMenu();
            }

        }

        @Override
        public void onOpen(SlideLayout layout) {
            mSlideLayout = layout;
        }
    }
}
