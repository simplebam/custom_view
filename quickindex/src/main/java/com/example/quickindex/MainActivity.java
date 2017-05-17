package com.example.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =MainActivity.class.getSimpleName() ;
    private ListView lv_main;
    private TextView tv_word;
    private IndexView iv_words;
    private Handler mHandler = new Handler();
    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initListener();
    }


    /**
     * 初始化UI
     */
    private void initView() {
        lv_main = (ListView) findViewById(R.id.lv_main);
        tv_word = (TextView) findViewById(R.id.tv_word);
        iv_words = (IndexView) findViewById(R.id.iv_words);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {

        iv_words.setOnIndexChangeListener(new IndexView.OnIndexChangeListener() {
            @Override
            public void onIndexChange(String word) {


                //更新字母
                updateWord(word);
                //跳到到当前的字母
                updateListView(word);//A~Z

            }
        });

        lv_main.setAdapter(new MyPaggerAdapter());

    }


    /**
     * 更新listView中的列表
     * @param word
     */
    private void updateListView(String word) {
        for (int i=0;i<persons.size();i++) {
            if (persons.get(i).getPinyin().startsWith(word)) {
                lv_main.setSelection(i);
                //搜索到第一个就停止,如果不停止,那么就是定位到最后一个,导致符合该字母的前n-1都不可以见到
                return;
            }
        }
    }
    private void updateListView1(String word) {
        for (int i=0;i<persons.size();i++) {
            String listWord = persons.get(i).getPinyin().substring(0, 1);
            if (word.equals(listWord)) {
                lv_main.setSelection(i);
                //搜索到第一个就停止,如果不停止,那么就是定位到最后一个,导致符合该字母的前n-1都不可以见到
                return;
            }
        }
    }

    /**
     * 更新tv_word显示的字母
     * @param word
     */
    private void updateWord(String word) {
        tv_word.setVisibility(View.VISIBLE);
        tv_word.setText(word);
        //使用前最好清除一下消息机制
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_word.setVisibility(View.GONE);
            }
        }, 3000);
    }


    private class MyPaggerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Person getItem(int position) {
            return persons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ///获得当前条目的首字母的首拼音
            String word = getItem(position).getPinyin().substring(0, 1);
            Log.i(TAG,"word:"+word);
            if (!TextUtils.isEmpty(word)) {
                if (position==0) {
                    viewHolder.tv_word.setVisibility(View.VISIBLE);
                    viewHolder.tv_word.setText(word);
                } else {
                    //获得前一个条目的首字母的首拼音
                    String preWord = persons.get(position - 1).getPinyin().substring(0, 1);
                    if (word.equals(preWord)) {
                        viewHolder.tv_word.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_word.setText(word);
                        viewHolder.tv_word.setVisibility(View.VISIBLE);
                    }


                }
            }

            viewHolder.tv_name.setText(getItem(position).getName());

            return convertView;
        }


    }

    static class ViewHolder {
        public TextView tv_word;
        public TextView tv_name;
    }

    /**
     * 初始化数据
     */
    private void initData() {

        persons = new ArrayList<>();
        persons.add(new Person("张晓飞"));
        persons.add(new Person("杨光福"));
        persons.add(new Person("胡继群"));
        persons.add(new Person("刘畅"));

        persons.add(new Person("钟泽兴"));
        persons.add(new Person("尹革新"));
        persons.add(new Person("安传鑫"));
        persons.add(new Person("张骞壬"));

        persons.add(new Person("温松"));
        persons.add(new Person("李凤秋"));
        persons.add(new Person("刘甫"));
        persons.add(new Person("娄全超"));
        persons.add(new Person("张猛"));

        persons.add(new Person("王英杰"));
        persons.add(new Person("李振南"));
        persons.add(new Person("孙仁政"));
        persons.add(new Person("唐春雷"));
        persons.add(new Person("牛鹏伟"));
        persons.add(new Person("姜宇航"));

        persons.add(new Person("刘挺"));
        persons.add(new Person("张洪瑞"));
        persons.add(new Person("张建忠"));
        persons.add(new Person("侯亚帅"));
        persons.add(new Person("刘帅"));

        persons.add(new Person("乔竞飞"));
        persons.add(new Person("徐雨健"));
        persons.add(new Person("吴亮"));
        persons.add(new Person("王兆霖"));

        persons.add(new Person("阿三"));
        persons.add(new Person("李博俊"));


        //排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

    }
}
