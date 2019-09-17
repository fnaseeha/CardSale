package com.lk.lankabell.android.activity.tsr.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

public class SplashActivity extends AppCompatActivity {
    String fromPackage, username, pword, SimNo, CollectorCode, attendenceIn, attendenceOut,empNo,logout;
    Intent intent;
    private DatabaseHandler dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbh = new DatabaseHandler(getApplicationContext());
        intent = new Intent(this, LoginPage.class);
        String logout = SharedPrefManager.getLocalSharedPreference(SplashActivity.this, CONSTANTS.SHARED_LOGOUT);
       System.out.println("*Shara logout "+logout);
       System.out.println("* global "+CONSTANTS.SHARED_LOGOUT);
     /*   if(logout.equals("1")){
            finish();
            System.exit(0);

//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        }*/

        Bundle b = getIntent().getExtras();

        System.out.println("* bundle "+ b.getString("package"));
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
           // startActivity(intent);
        }

    }
}
