package com.example.dropdownbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText et_input;
    private ImageView iv_down_arrow;
    private ArrayList<String> msgs;

    private PopupWindow mPopupWindow;
    private ListView listView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        msgs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            msgs.add(i + "--aaaaaaaaaaaaaa--" + i);
        }
    }

    /**
     * 初始化UI
     */
    private void initView() {
        et_input = (EditText) findViewById(R.id.et_input);
        iv_down_arrow = (ImageView) findViewById(R.id.iv_down_arrow);

        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.listview_background);//设置背景颜色

        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);

        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(MainActivity.this);
                    //设置popupWindow的宽高
                    mPopupWindow.setWidth(et_input.getWidth());
                    int popHeight = DensityUtil.dip2px(MainActivity.this, 180);
                    mPopupWindow.setHeight(popHeight);
                    Toast.makeText(MainActivity.this,"height=="+popHeight,Toast.LENGTH_SHORT).show();

                    mPopupWindow.setContentView(listView);
                    //设置为可见
                    mPopupWindow.setFocusable(true);
                }
                //Log.i(TAG,"et_input调用了click监听接口");
                mPopupWindow.showAsDropDown(et_input, 0, 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选中条目,把条目的数据显示在et_input
                String msg = msgs.get(position);
                et_input.setText(msg);
                et_input.setSelection(msg.length());//设置光标的位置

                if (mPopupWindow!=null && mPopupWindow.isShowing()) {
                    //隐藏该mPopupWindow
                    mPopupWindow.dismiss();
                }
            }
        });


    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public String getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            final String msg = msgs.get(position);
            viewHolder.tv_msg.setText(msg);

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //从集合中删除数据
                    msgs.remove(msg);
                    //更新listView显示的数据
                    mAdapter.notifyDataSetChanged();
                }
            });


            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_msg;
        ImageView iv_delete;
    }
}
