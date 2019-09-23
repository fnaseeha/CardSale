package com.lk.lankabell.android.activity.tsr.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import java.text.SimpleDateFormat;

public class SplashActivity extends AppCompatActivity {
    String fromPackage, username, pword, SimNo, CollectorCode, attendenceIn, attendenceOut, empNo, logout;
    Intent intent;
    private DatabaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbh = new DatabaseHandler(getApplicationContext());

        Bundle b = getIntent().getExtras();

        System.out.println("* bundle " + b.getString("package"));
        if (b != null) {

            fromPackage = b.getString("package");
            username = b.getString("username");
            pword = b.getString("pword");
            SimNo = b.getString("SimNo");
            attendenceIn = b.getString("attendenceIn");
            attendenceOut = b.getString("attendenceOut");

            String logout = dbh.GetLogout(username);
            System.out.println("* LOGOUT Splash " + logout);

            if (dbh.getUserDetails() != null && logout.equals("N")) {

                System.out.println("* Splash SelectorActivity");
                intent = new Intent(this, SelectorActivity.class);
                dbh.UpdateLoginData(SimNo, username, pword, 4);
                intent.putExtra("username", username);
                intent.putExtra("pword", pword);
                intent.putExtra("SimNo", SimNo);
                startActivity(intent);

            } else if (logout.equals("Y")) {
                System.out.println("* Splash exit");
                if (username.equals("") && dbh.getUserDetails() != null) {
                    username = dbh.getUserDetails().getUserName();
                }
                System.out.println("*Splash success");
                dbh.updateLogout(username);
                finish();
                System.exit(0);
            } else {
                System.out.println("* Splash LoginPage");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(System.currentTimeMillis());
                intent = new Intent(this, LoginPage.class);
                dbh.SaveLoginDataSp(username, pword, SimNo, date);
                intent.putExtra("username", username);
                intent.putExtra("pword", pword);
                intent.putExtra("SimNo", SimNo);
                startActivity(intent);
            }

        } else {
            Toast.makeText(SplashActivity.this, "Error While getting username from Core App", Toast.LENGTH_LONG).show();
        }

    }
}
