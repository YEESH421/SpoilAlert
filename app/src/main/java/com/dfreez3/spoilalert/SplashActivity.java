package com.dfreez3.spoilalert;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        Handler splashDelay = new Handler();

        splashDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Fridge = new Intent(SplashActivity.this,
                        FridgeActivity.class);
                Fridge.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Fridge.putExtra("EXIT", true);
                //overridePendingTransition(R.transition.fadein, R.transition.fadeout);
                startActivity(Fridge);
            }
        }, 1000);
    }
}
