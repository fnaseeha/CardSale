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
    String fromPackage, username, pword, SimNo, CollectorCode, attendenceIn, attendenceOut,empNo,logout;
    Intent intent;
    private DatabaseHandler dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbh = new DatabaseHandler(getApplicationContext());



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
          //  empNo = b.getString("empNo");
         //   CollectorCode = b.getString("CollectorCode");
            attendenceIn = b.getString("attendenceIn");
            attendenceOut = b.getString("attendenceOut");


            String logout = dbh.GetLogout(username);
            System.out.println("* LOGOUT Splash " + logout);

            if(dbh.getUserDetails() != null && logout.equals("N")){

                intent = new Intent(this, SelectorActivity.class);
                dbh.UpdateLoginData(SimNo, username, pword,4);
                intent.putExtra("username",username);
                intent.putExtra("pword",pword);
                intent.putExtra("SimNo",SimNo);
                startActivity(intent);

            }else if(logout.equals("Y")){
                if(username.equals("") && dbh.getUserDetails() != null){
                    username = dbh.getUserDetails().getUserName();
                }
                System.out.println("*Splash success");
                dbh.updateLogout(username);
                finish();
                System.exit(0);
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
//                String date = sdf.format(System.currentTimeMillis());
//                intent = new Intent(this, LoginPage.class);
//                dbh.SaveLoginDataSp(username, pword, SimNo,date);
////                intent.putExtra("username",username);
////                intent.putExtra("pword",pword);
////                intent.putExtra("SimNo",SimNo);
//                startActivity(intent);
            }else{

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                String date = sdf.format(System.currentTimeMillis());
                intent = new Intent(this, LoginPage.class);
                dbh.SaveLoginDataSp(username, pword, SimNo,date);
                intent.putExtra("username",username);
                intent.putExtra("pword",pword);
                intent.putExtra("SimNo",SimNo);
                startActivity(intent);

            }




        } else {

            Toast.makeText(SplashActivity.this,"Error While getting username from Core App",Toast.LENGTH_LONG).show();
           // startActivity(intent);
        }

    }
}
