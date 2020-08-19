package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Intent o = new Intent(this, loginActivity.class);
        //////////////////////////////////////////////////////////////
        try{
            TimeUnit.SECONDS.sleep( 2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(o);
        finish();
    }
}
