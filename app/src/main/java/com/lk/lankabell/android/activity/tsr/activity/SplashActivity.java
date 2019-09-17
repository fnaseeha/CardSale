package com.lk.lankabell.android.activity.tsr.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;

public class SplashActivity extends AppCompatActivity {
    String fromPackage, username, pword, SimNo, CollectorCode, attendenceIn, attendenceOut,empNo;
    Intent intent;
    private DatabaseHandler dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbh = new DatabaseHandler(getApplicationContext());
        intent = new Intent(this, LoginPage.class);


        Bundle b = getIntent().getExtras();

        if (b != null) {
            fromPackage = b.getString("package");
            username = b.getString("username");
            pword = b.getString("pword");
            SimNo = b.getString("SimNo");
         //   CollectorCode = b.getString("CollectorCode");
            attendenceIn = b.getString("attendenceIn");
            attendenceOut = b.getString("attendenceOut");

            System.out.println(username);
            if(dbh.getUserDetails() != null){
                dbh.UpdateLoginData(SimNo, username, pword,4);
            }else{
                dbh.SaveLoginData(username, pword, SimNo);
            }

            intent.putExtra("username",username);
            intent.putExtra("pword",pword);
            intent.putExtra("SimNo",SimNo);
            startActivity(intent);


        } else {
            Toast.makeText(SplashActivity.this,"Error While getting username from Core App",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }
}
