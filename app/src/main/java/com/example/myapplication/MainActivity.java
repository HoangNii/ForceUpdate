package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.forceupdate.Force;
import com.example.forceupdate.ForceUpdate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForceUpdate.get()
                .urlLoad("")
                .useCustomDialog(false)
                .listen(new ForceUpdate.OnListen() {
                    @Override
                    public void onSuccess(Force force) {

                    }
                })
                .check(this);


    }
}
