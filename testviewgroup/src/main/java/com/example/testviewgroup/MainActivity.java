package com.example.testviewgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewGroup rl_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();





    }

    private void initView() {
        rl_test = (ViewGroup) findViewById(R.id.rl_test);

        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.test);
        rl_test.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我被点击了", Toast.LENGTH_SHORT).show();
            }
        });



//        View view = View.inflate(this, R.layout.test, null);
//        rl_test.addView(view,0);

    }
}
