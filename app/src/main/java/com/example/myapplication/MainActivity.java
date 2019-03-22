package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.forceupdate.Force;
import com.example.forceupdate.ForceUpdate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForceUpdate.get()
                .urlLoad("https://github.com/duchoangitt/ForceUpdate/raw/master/app/src/main/assets/config.json")
                .useCustomDialog(false)
                .listen(new ForceUpdate.OnListen() {
                    @Override
                    public void onSuccess(Force force) {
                        Toast.makeText(MainActivity.this,"onSuccess", Toast.LENGTH_SHORT).show();
                    }
                })
                .check(this);


    }
}
