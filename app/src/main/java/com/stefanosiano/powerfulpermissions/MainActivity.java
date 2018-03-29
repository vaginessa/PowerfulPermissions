package com.stefanosiano.powerfulpermissions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Perms
    private void asd(){
        if(!Permissions.check()) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }
}
