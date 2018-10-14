package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.parse.ParseUser;
import com.interviewtest.R;

/**
 * Created by UsmanKhan on 9/12/18.
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        new Thread() {
            public void run() {

                try {
                    sleep(1500);
                    ParseUser pUser = ParseUser.getCurrentUser();
                    if(pUser==null){
                        Intent intent = new Intent(SplashScreen.this, LoginSignUpScreen.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e) {
                    e.getMessage();
                }
            }
        }.start();



    }
}
