package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.forceupdate.ForceUpdate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForceUpdate.get
                .setUrl("https://api.myjson.com/bins/17jome")
                .syn();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdate.get.setActivity(this);
    }
}
