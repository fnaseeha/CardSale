package com.lk.lankabell.android.activity.tsr.activity;

import java.io.File;
import java.net.HttpURLConnection;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.beans.InitialData;
import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.sync.SysncServices;
import com.lk.lankabell.android.activity.tsr.tracking.utils.DateTimeFormatings;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;
import com.lk.lankabell.android.activity.tsr.R;

@SuppressLint("NewApi")
public class LoginPage extends Activity {

    double version = 1.19;


    // When you send new application, remember to update version here

    private DatabaseHandler dbh;
    private EditText pword = null;
    private EditText uname = null;
    InputMethodManager mgr = null;

    SharedPreferences prefs = null;
    boolean firstrun = false;

    protected Dialog mSpashDialog;

    private static String SOAP_ACTION1 = "http://mainService/LoginAuthentication";
    private static String SOAP_ACTION20 = "http://mainService/loginBlock";

    private static String NAMESPACE = "http://mainService";

    private static String METHOD_NAME1 = "LoginAuthentication";
    private static String METHOD_NAME20 = "loginBlock";

    private static String URL = WsdlReader.getServiceUrl(false, null);
    boolean network_enable = false;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double Latitude = 0;
    double Longitude = 0;
    protected LocationManager locationManager;
    SoapObject result;
    String userName;
    String password;
    private TextView error;
    SoapSerializationEnvelope envelope;
    InitialData initialData;
    String date;

    String dbQuery8;
    String dbQuery8String;
    String[] statusarray8;
    String[] statusarrayData8;

    SoapObject resultSync;
    int WRITE_EXTERNAL_STORAGE_PERMISSSION_REQUEST_CODE = 100;
    ArrayList<String> AllSerialNumbers;
    HttpURLConnection urlc;
    int count = 0;
    int timeInterval = 4000;
    int timeInterval2 = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // Remove notification bar
        dbh = new DatabaseHandler(getApplicationContext());
        setContentView(R.layout.loginpage);
        AllSerialNumbers = Utils.getSerialNumbers(this);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        version = Double.parseDouble(pInfo.versionName);


        final TextView myTitleText = findViewById(R.id.myTitle);
        if (myTitleText != null) {
            myTitleText.setText("Login");
        }

        final TextView currentversion = findViewById(R.id.appversion);
        currentversion.setText("V - " + version);

        firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {
            URL = WsdlReader.getServiceUrl();
        } else {
            URL = WsdlReader.getServiceUrl(true, this);
        }
        try {
            if (dbh != null) {
                dbh.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        pword = findViewById(R.id.txtPassword);

        error = findViewById(R.id.lblErrorMessage);

        AskWritePermission();
        // TSRApplication tsrApp = (TSRApplication)getApplication();
        // tsrApp.startBeginReceiver();

        // checkInternet();
        System.out.println("* package " + getPackageName());
        Intent b = getIntent();
        String username = b.getStringExtra("username");
        String pword = b.getStringExtra("pword");
        String sim = b.getStringExtra("SimNo");
        String logout = dbh.GetLogout(username);
        System.out.println("* SHARED_LOGOUT " + logout);
        if (username != null && pword != null & sim != null) {
            try {
                CONSTANTS.FinalSimSerialNumber = sim;
                CheckLogin(username, pword, sim);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    public void onClickLogin(View view) throws ParseException {
        AllSerialNumbers = Utils.getSerialNumbers(this);
        System.out.println("* onClickLogin AllSerialNumbers " + AllSerialNumbers.size());

        checkInternet();

        dbh = new DatabaseHandler(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = sdf.format(System.currentTimeMillis());

        error.setText("");
        final String telephone =  Utils.getSimSerialNumber(this);
       // final String telephone = AllSerialNumbers.size() > 0 ? AllSerialNumbers.get(0) : "";

        System.out.println("* telephone " + telephone);
        pword = findViewById(R.id.txtPassword);
        password = pword.getText().toString();
        uname = findViewById(R.id.txtUserName);
        userName = uname.getText().toString();
        dbh.saveVersion(version);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (userName.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            error.setText("Enter your Username & Password");
        } else if (telephone.equalsIgnoreCase("")) {
            error.setText("Sim Card Number is empty");
        } else if (!isGPSEnabled) {
            Toast.makeText(getApplicationContext(), "Please on the GPS on your Mobile.", Toast.LENGTH_LONG).show();
        } else {
            if (firstrun) {
                network_enable = isOnline();
                if (network_enable) {

                    initialData = new InitialData();
                    System.out.println("* initialData  tele "+telephone+" username "+userName+" password "+password);
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                    request.addProperty("strInputUserMobile", telephone);
                    request.addProperty("strInputUserName", userName);
                    request.addProperty("strInputUserPassword", password);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);
                    envelope.dotNet = true;

                    try {

                        (new GetAync(this)).execute("");

                        Timer _timer  = new Timer();

                        _timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // use runOnUiThread(Runnable action)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("* timer 1 EXEC "+telephone);
                                        try {
                                            getResult(telephone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }, timeInterval);


                    } catch (Exception e) {
//						if (android.os.Build.VERSION.SDK_INT > 9) {
//							StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//							StrictMode.setThreadPolicy(policy);
//						}
                        Toast.makeText(getApplicationContext(), "Login failed.. Please Try Again. " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Check your Connection", Toast.LENGTH_LONG).show();
                }
            } else {
                network_enable = isOnline();
                if (network_enable) {
                    //boolean service_enable = isServiceUp();
                    String pwdByUserName = dbh.GetPasswordByUserName(userName);
                    if (pwdByUserName.equalsIgnoreCase(password)) {
                        String userNameByMobile = dbh.GetNameByMobileNo(telephone);
                        if (userNameByMobile.equals("")) {
                            //error.setText("Invalid Mobile Number");
                            TryAgainLowerCase(telephone.toLowerCase(), dbh);

                        } else {
                            int status = synchVersion(telephone,userName,password);
                            if (status == 0) {
                                OfflineLogin(userName, password, telephone, error);
                            } else {
                                String dateString = "2014-03-13 00:00:00.0";
                                dateString = dbh.getServerDate();
                                Log.d("Get server date :", dateString);
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                                Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));
                                Date date1 = formatter.parse(dateString);

                                DateTime phoneDate = new DateTime(dateToday);
                                DateTime serverDate = new DateTime(date1);
                                Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                                Log.d("Get Phone date :", phoneDate.toString());
                                int diffN = Math.abs(diff);
                                if ((diffN < 30)) {
                                    Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("Check the server ", "02 run - Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                                    new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date - Online login (ST- " + dateString + " )").setPositiveButton("Ok", null).show();
                                }
                            }
                        }
                    } else {
                        error.setText("User name or Password invalid");
                        mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
                    }
                } else {
                    // =========================================DATE SYNCH WITH
                    // SERVER DATE=====================================
                    String dateString = "2014-03-13 00:00:00.0";
                    dateString = dbh.getServerDate();

                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis())); // new
                    // Date();
                    // Date date1 = formatter.parse(dateString);
                    Date date1 = DateTimeFormatings.getDateTimeWeb(dateString);

                    DateTime phoneDate = new DateTime(dateToday);
                    DateTime serverDate = new DateTime(date1);

                    // =========================================DATE SYNCH WITH
                    // SERVER DATE=====================================

                    String pwdByUserName = dbh.GetPasswordByUserName(userName);
                    if (pwdByUserName.equalsIgnoreCase(password)) {
                        String userNameByMobile = dbh.GetNameByMobileNo(telephone);
                        if (userNameByMobile.equals("")) {
                            error.setText("Invalid Mobile Number");
                        } else if (!phoneDate.isAfter(serverDate)) {
                            Log.d("Check the server", "Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                            new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date  - Offline login (LST- " + dateString + " )").setPositiveButton("Ok", null).show();
                        } else {
                            if (userNameByMobile.equalsIgnoreCase(userName)) {
                                Toast toast = Toast.makeText(LoginPage.this, "Welcome! ", Toast.LENGTH_SHORT); //pop-up
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                                Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                                startActivity(intent);
                                finish();


                            } else {
                                error.setText("Phone Number is incorrect.");
                                mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
                            }
                        }
                    } else {
                        error.setText("User name or Password invalid");
                        mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
                    }// ------
                }
            }
        }  // ----- end here

    }

    public void getResult(final String telephone) {
        if (result != null ) {//&& !result.getProperty(0).toString().equals("0")
            System.out.println("* result not null "+result.getProperty(0));
            String result_value = result.getProperty(0).toString();
            int result_valus = Integer.parseInt(result_value);
            boolean deletedb = getApplicationContext().deleteDatabase("TSRDBNEW");
            Log.d("Database is ", deletedb + "");
            dbh = new DatabaseHandler(getApplicationContext());

            if (true) {

                CONSTANTS.FinalSimSerialNumber = telephone;
                showSplashScreen(this);

                dbh.InsertTableData();// updated times insert to
                // table
                File path = getApplicationContext().getDatabasePath("TSRDBNEW");
                Log.d("DataBasde Path is :", path.getAbsolutePath());
                //========================LOGIN  ==========
                // BLOCK START
                System.out.println("* initialData not null first ");
                new BlockSync(this, telephone).execute("");

                Timer _timer  = new Timer();

                _timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // use runOnUiThread(Runnable action)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("* timer 2 EXEC");
                                try {
                                    getInitialData(telephone);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    System.out.println("* userName "+userName+" password "+password+" sim "+telephone);
                                    Toast.makeText(LoginPage.this,"Username password not match with simSerial Number",Toast.LENGTH_LONG).show();
                                    removeSplash();
                                    //remove f
                                    // make simple f
                                    // check
                                }
                            }
                        });
                    }
                }, timeInterval2);

               /* for (int i = 0; i < 205; i++) {
                    for (int j = 0; j < 500; j++) {
                        System.out.println(" if BlockSync " + i + j);
                    }
                }*/

//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        System.out.println("* initialData delayed "+initialData);
//                                    }
//                                }, 300);
////
                // /======================================================================================/LOGIN
                // BLOCK END


            } else if (result_valus == 0) {
                Toast.makeText(getApplicationContext(), "Invalid username & Password or Invalid Mobile Number", Toast.LENGTH_LONG).show();
            }
        } else {
            //RetryWithSimSerial;
            Toast.makeText(getApplicationContext(), "Invalid username & Password or Invalid Mobile Number", Toast.LENGTH_LONG).show();
            //retry(telephone.toLowerCase())
        }
    }

    public void getInitialData(String telephone) throws ParseException {
        if (true) {

            System.out.println("* initialData " + initialData);
            try {
                String dbQuery3 = initialData.getLoginData();
                System.out.println("* dbQuery3 " + dbQuery3);

                if (!dbQuery3.equals("[]")||!dbQuery3.equals("null")||!dbQuery3.equals(null)) {
                    String[] statusarray3 = dbQuery3.split(",");
                    dbh.SaveLoginData(statusarray3[0].replaceAll("\\[|\\]|\\<", ""),
                            statusarray3[2],
                            statusarray3[3].replaceAll("\\[|\\]|\\>", ""),
                            statusarray3[1]);
                }

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                date = sdf1.format(System.currentTimeMillis());
                System.out.println("* date " + date);
                dbh.SetLoginDetailsUpdatedDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!initialData.getCityData().equals("[]")) {
                    String dbQuery5 = initialData.getCityData();
                    String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
                    String[] statusarray5 = Value5.split(",");
                    String[] statusarrayData5 = Value5.split("<");
                    for (int i = 1; i < statusarrayData5.length; i++) {
                        String[] datavaluesStrings = statusarrayData5[i].split(">");
                        String[] datavalues = datavaluesStrings[0].split(",");
                        dbh.SaveCityData(datavalues[0], datavalues[1], datavalues[2], datavalues[3], datavalues[4], datavalues[5], datavalues[6], datavalues[7]);
                    }
                    System.out.println("* date1 " + date);
                    dbh.SetCityPostalCodesUpdatedDate(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!initialData.getCardTypeData().equals("[]")) {
                    String dbQuery6 = initialData.getCardTypeData();
                    String dbQuery6String = dbQuery6.replaceAll("\\[|\\]", "");
                    String[] statusarray6 = dbQuery6String.split(",");
                    String[] statusarrayData6 = dbQuery6String.split("<");
                    for (int i = 1; i < statusarrayData6.length; i++) {
                        String[] datavaluesStrings = statusarrayData6[i].split(">");
                        String[] datavalues = datavaluesStrings[0].split(",");
                        dbh.SaveCardTypes(datavalues[0], datavalues[1], datavalues[2], datavalues[3]);
                    }
                 //   System.out.println("* date2 " + date);
                    dbh.SetCardTypesUpdatedDate(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        if (true) {
            try {
                String dbQuery = initialData.getServerDate();
                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                String[] statusarrayData61 = dbQueryString.split("<");

                for (int i = 1; i < statusarrayData61.length; i++) {

                    String[] datavaluesStrings = statusarrayData61[i].split(">");
                    String[] datavalues = datavaluesStrings[0].split(",");
                    String serverDateStr = datavalues[0];
                    System.out.println("* server Date "+serverDateStr);
                    // SimpleDateFormat
                    // formatter= new
                    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // Date serverDate = new
                    // Date(formatter.format(serverDateStr));//
                    // formatter.parse(serverDateStr);
                    dbh.SaveServerDate(serverDateStr);// serverDate.toString());//new
                    // Date()

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // int status = synchVersion(telephone,userName,password);

        dbh.UpdateLoginData(result.getProperty(0).toString(), userName, password);
        String dateString ="";
        try {
             dateString = dbh.getServerDate();
            System.out.println("* dateString n 1 " + dateString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

        Date dateToday = new Date();
        Log.d("dates", "Server date : " + date1 + " Phone Date : " + dateToday);
        // System.out.println(output);

        DateTime phoneDate = new DateTime(dateToday);
        DateTime serverDate = new DateTime(date1);

        Toast toast = Toast.makeText(LoginPage.this, "Initial data synched! Version : " + version + " ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        Log.d("Come to the my m : ", "My method 001 ");
        new updateSync(telephone).execute("");
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println(" if " + i + j);
            }
        }
        //	UpdateOrCheckTSRSystemprofile();

        Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
        int diffN = Math.abs(diff);
        if ((diffN < 30)) {
//													Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
//													startActivity(intent);
//													finish();
            if (CONSTANTS.IS_USING_ORGINAL_URL) {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
                startHomeActivity();
            } else {
                (new SystemProfileAsyc(this)).execute("");
            }

        } else {
            Log.d("Check System time 001 :", "1 Run : Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
            new AlertDialog.Builder(LoginPage.this)
                    .setTitle("Date info")
                    .setMessage("Phone Date different from Server Date - Fist login (ST- " + dateString + " )")
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }


    private void TryAgainLowerCase(String telephone, DatabaseHandler dbh1) throws ParseException {
        System.out.println("* telephone " + telephone);
        String userNameByMobile = dbh1.GetNameByMobileNo(telephone);
        if (userNameByMobile.equals("")) {
            //  error.setText("Invalid Mobile Number");
            String letter = telephone.substring(telephone.length() - 1);

            if (letter.equals("f")) {
                String[] arrOfStr = telephone.split("f", 2);
                SkipTelephoneF(arrOfStr[0], dbh1);
            } else {
                error.setText("Invalid Mobile Number");
            }


        } else {
            int status = synchVersion(telephone,userName,password);
            if (status == 0) {
                OfflineLogin(userName, password, telephone, error);
            } else {
                String dateString = "2014-03-13 00:00:00.0";
                dateString = dbh1.getServerDate();
                Log.d("Get server date :", dateString);
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));
                Date date1 = formatter.parse(dateString);

                DateTime phoneDate = new DateTime(dateToday);
                DateTime serverDate = new DateTime(date1);
                Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                Log.d("Get Phone date :", phoneDate.toString());
                int diffN = Math.abs(diff);
                if ((diffN < 30)) {
                    Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("Check the server ", "02 run - Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                    new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date - Online login (ST- " + dateString + " )").setPositiveButton("Ok", null).show();
                }
            }
        }
    }

    private void SkipTelephoneF(String telephone, DatabaseHandler dbh1) throws ParseException {
        System.out.println("* telephone " + telephone);
        String userNameByMobile = dbh1.GetNameByMobileNo(telephone);
        if (userNameByMobile.equals("")) {
            error.setText("Invalid Mobile Number");
        } else {
            int status = synchVersion(telephone,userName,password);
            if (status == 0) {
                OfflineLogin(userName, password, telephone, error);
            } else {
                String dateString = "2014-03-13 00:00:00.0";
                dateString = dbh1.getServerDate();
                Log.d("Get server date :", dateString);
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));
                Date date1 = formatter.parse(dateString);

                DateTime phoneDate = new DateTime(dateToday);
                DateTime serverDate = new DateTime(date1);
                Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                Log.d("Get Phone date :", phoneDate.toString());
                int diffN = Math.abs(diff);
                if ((diffN < 30)) {
                    Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("Check the server ", "02 run - Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                    new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date - Online login (ST- " + dateString + " )").setPositiveButton("Ok", null).show();
                }
            }
        }
    }

    private void CheckLogin(String username, String pword, final String sim) throws ParseException {
        System.out.println("* Check Login firstrun " + firstrun + " username " + username + " pword " + pword + " sim " + sim);
        if (firstrun) {
            network_enable = isOnline();
            if (network_enable) {

                initialData = new InitialData();
                System.out.println("* tele "+sim+" username "+username+" pword "+pword);
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                request.addProperty("strInputUserMobile", sim);
                request.addProperty("strInputUserName", username);
                request.addProperty("strInputUserPassword", pword);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;

                try {

                    (new GetAync(this)).execute("");

//                    int count=0;
//                    for (int i = 0; i < 200; i++) {
//                        for (int j = 0; j < 250; j++) {
//                            count+=i+j;
//                        }
//                    }
                    Timer _timer  = new Timer();

                    _timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // use runOnUiThread(Runnable action)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("* timer 1 EXEC");
                                    try {
                                        getResult(sim);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }, timeInterval);


                } catch (Exception e) {
//						if (android.os.Build.VERSION.SDK_INT > 9) {
//							StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//							StrictMode.setThreadPolicy(policy);
//						}
                    Toast.makeText(getApplicationContext(), "Login failed.. Please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Check your Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            network_enable = isOnline();
            if (network_enable) {
                //username, String pword, final String sim
                //boolean service_enable = isServiceUp();
                String pwdByUserName = dbh.GetPasswordByUserName(username);
                if (pwdByUserName.equalsIgnoreCase(pword)) {
                    String userNameByMobile = dbh.GetNameByMobileNo(sim);
                    if (userNameByMobile.equals("")) {
                        //error.setText("Invalid Mobile Number");
                        TryAgainLowerCase(sim.toLowerCase(), dbh);

                    } else {
                        int status = synchVersion(sim,username,pword);
                        if (status == 0) {
                            OfflineLogin(username, pword, sim, error);
                        } else {
                            String dateString = "2014-03-13 00:00:00.0";
                            dateString = dbh.getServerDate();
                            Log.d("Get server date :", dateString);
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                            Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));
                            Date date1 = formatter.parse(dateString);

                            DateTime phoneDate = new DateTime(dateToday);
                            DateTime serverDate = new DateTime(date1);
                            Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                            Log.d("Get Phone date :", phoneDate.toString());
                            int diffN = Math.abs(diff);
                            if ((diffN < 30)) {
                                Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("Check the server ", "02 run - Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                                new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date - Online login (ST- " + dateString + " )").setPositiveButton("Ok", null).show();
                            }
                        }
                    }
                } else {
                    error.setText("User name or Password invalid");
                  //  mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
                }
            } else {
                // =========================================DATE SYNCH WITH
                // SERVER DATE=====================================
                String dateString = "2014-03-13 00:00:00.0";
                dateString = dbh.getServerDate();

                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));

                // Date();
                // Date date1 = formatter.parse(dateString);
                Date date1 = DateTimeFormatings.getDateTimeWeb(dateString);

                DateTime phoneDate = new DateTime(dateToday);
                DateTime serverDate = new DateTime(date1);

                // =========================================DATE SYNCH WITH
                // SERVER DATE=====================================

                String pwdByUserName = dbh.GetPasswordByUserName(username);
                String userNameByMobile = dbh.GetNameByMobileNo(sim);
                if (!phoneDate.isAfter(serverDate)) {
                    Log.d("Check the server", "Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                    new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date  - Offline login (LST- " + dateString + " )").setPositiveButton("Ok", null).show();
                } else if (userNameByMobile.equalsIgnoreCase(username) && pwdByUserName.equalsIgnoreCase(pword)) {
                    Toast toast = Toast.makeText(LoginPage.this, "Welcome! ", Toast.LENGTH_SHORT); //pop-up
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

    }//check Login

    private void AskWritePermissionAgain() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Nowx
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSSION_REQUEST_CODE);

        } else {
            // permission has been granted, continue as usual
        }
    }

    private void AskWritePermission() {
        //WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(LoginPage.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginPage.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AskWritePermissionAgain();
                //new ToastManager(LoginPage.this).error("SMS Won't send to Coordinator!");
            } else {
                ActivityCompat.requestPermissions(LoginPage.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_PERMISSSION_REQUEST_CODE);

            }
        }
    }

    private void checkInternet() {
        if (!Utils.isInternetAvailable()) {
            error.setText("Can not connect to the internet. Please check");
        }
    }


    // private String getMyPhoneNumber()
    //{
    // TelephonyManager mTelephonyMgr;
    // mTelephonyMgr = (TelephonyManager)
    // getSystemService(Context.TELEPHONY_SERVICE);
    //
    // String getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
    // String getSimNumber = mTelephonyMgr.getLine1Number();
    // return getSimSerialNumber;
    // }

    public void onClickClose(View view) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogBuilder = factory.inflate(R.layout.custom_msg_dialog, null);
        final AlertDialog dialogView = new AlertDialog.Builder(this).create();
        dialogView.setView(dialogBuilder);
        TextView title = dialogBuilder.findViewById(R.id.title);
        title.setText("Do you want to exit?");
        dialogBuilder.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
            }
        });
        dialogBuilder.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });
        dialogView.show();
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogView.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), dialogView.getWindow().getAttributes().height);

    }

    public static void disableButton(Button button) {
        button.setEnabled(false);
        Log.w("button", "DISABLED");
    }

    public static void enableButton(Button button) {
        button.setEnabled(false);
        Log.w("button", "ENABLED");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        return;
    }


    private class SyncV extends AsyncTask<String, String, String> {

        private Context context;

        public SyncV(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url = WsdlReader.getServiceUrl(true, context);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                androidHttpTransport.call(SOAP_ACTION21, envelope);
                resultSync = (SoapObject) envelope.bodyIn;
                System.out.println("* resultSync " + resultSync);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private class GetAync extends AsyncTask<String, String, String> {


        private HttpTransportSE androidHttpTransport;
        private Context context;

        public GetAync(Context context) {
            super();
            this.context = context;
            androidHttpTransport = new HttpTransportSE(URL);
        }


        @Override
        protected String doInBackground(String... params) {
            try {

                androidHttpTransport.call(SOAP_ACTION1, envelope);
                // String result = "1461";
                result = (SoapObject) envelope.bodyIn;


                //   System.out.println("* result " + result!=null?result.toString():"");
//
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {

            System.out.println(" * GetAync result "+result);

        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }

    }

    //
    private void OfflineLogin(String userName, String password, String telephone, TextView error) throws ParseException {
        // =========================================DATE SYNCH WITH SERVER
        // DATE=====================================

        String dateString = "2014-03-13 00:00:00.0";
        dateString = dbh.getServerDate();

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date dateToday = formatter.parse(formatter.format(System.currentTimeMillis())); // new
        // Date();
//		Date date1 = formatter.parse(dateString);
        Date date1 = DateTimeFormatings.getDateTimeWeb(dateString);

        DateTime phoneDate = new DateTime(dateToday);
        DateTime serverDate = new DateTime(date1);

        // =========================================DATE SYNCH WITH SERVER
        // DATE=====================================

        String pwdByUserName = dbh.GetPasswordByUserName(userName);
        if (pwdByUserName.equalsIgnoreCase(password)) {
            String userNameByMobile = dbh.GetNameByMobileNo(telephone);
            if (userNameByMobile.equals("")) {
                error.setText("Invalid Mobile Number");
            } else if (!phoneDate.isAfter(serverDate)) {
                Log.d("Check the server", "Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
                new AlertDialog.Builder(LoginPage.this).setTitle("Date info").setMessage("Phone Date different from Server Date  - Offline login (LST- " + dateString + " )").setPositiveButton("Ok", null).show();
            } else {
                if (userNameByMobile.equalsIgnoreCase(userName)) {
                    Toast toast = Toast.makeText(LoginPage.this, "Welcome! ", Toast.LENGTH_SHORT); // pop-up
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    error.setText("Phone Number is incorrect.");
                    mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
                }
            }
        } else {
            error.setText("User name or Password invalid");
            mgr.hideSoftInputFromWindow(pword.getWindowToken(), 0);
        }// -------
    }

    private void UpdateOrCheckTSRSystemprofile(String sim) {
        User user = dbh.getUserDetails();
        String epfNo = dbh.GetEmpNoByPhoneNo(sim);
        Log.d("Come to the", "My method 00222 ");
        new SysncServices().TSRSystemprofile(getApplicationContext(), user.getmobileNo(), user.getUserName(), user.getPassword(), epfNo, "LoginInishial");
    }

    private static String SOAP_ACTION21 = "http://mainService/synchServerDate";
    private static String METHOD_NAME21 = "synchServerDate";

    private Integer synchVersion(String sim,String userName,String password) throws ParseException {
        Integer returnVal = 0;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME21);
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        User user = dbh.getUserDetails();
        if (true) {
            request.addProperty("strInputUserMobile", sim);
            request.addProperty("strInputUserName", userName);
            request.addProperty("strInputUserPassword", password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                new SyncV(this).execute("");
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 100; j++) {
                        System.out.println("ij " + i + j);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                returnVal = 0;
            }
//				String url = WsdlReader.getServiceUrl(true, this);
//				HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
//				androidHttpTransport.call(SOAP_ACTION21, envelope);
//				resultSync = (SoapObject) envelope.bodyIn;
            if (resultSync != null) {
                String result_Value = resultSync.getProperty(0).toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.d("***********", "*****************************************************" + result_Value);
                Date serverDate = formatter.parse(result_Value);
                dbh.SaveServerDate(serverDate.toString());
                returnVal = 1;
            } else {
                returnVal = 0;
            }

        }
        return returnVal;
    }

    public String getPassWordByMobileNo(String mobileNo) {
        /*** Get user password by mobile ***/
        // SQLiteDatabase db1 = openOrCreateDatabase("TSRDB", MODE_PRIVATE,
        // null);
        SQLiteDatabase db1 = openOrCreateDatabase("TSRDBNEW", MODE_PRIVATE, null);
        String pwd = "";
        Cursor cursor = db1.rawQuery("SELECT PASSWORD FROM TSR_LOGIN WHERE MOBILE_NO ='" + mobileNo + "' ", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            pwd = cursor.getString(0);
        }

        cursor.close();
        db1.close();

        return pwd;
    }

    /**
     * @param name
     * @return password
     * <p>
     * Getting the password by UserName function.
     */
    public String getPassWordByName(String name) {
        /*** Get user password by user name ***/
        SQLiteDatabase db1 = openOrCreateDatabase("TSRDB", MODE_PRIVATE, null);
        String pwd = "";
        Cursor cursor = db1.rawQuery("SELECT PASSWORD FROM TSR_LOGIN WHERE USER_NAME ='" + name + "' ", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            pwd = cursor.getString(0);
        }

        cursor.close();
        db1.close();

        return pwd;
    }

    /**
     * @return connected or not. Checking whether the Internet connection is
     * available or not.
     */
    public boolean isOnline() { // check network connection is available
        boolean connected = false;
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            Log.d("YES INTERNET", "YES INTERNET");
            connected = true;
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_LONG).show();
            connected = false;
        }
        return connected;
    }

    public class ServiceUpBK extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {

                java.net.URL url = new java.net.URL("http://119.235.1.88:8080/axis2/services/TSR_WEB_SERVICE?wsdl");
                urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(5000); // Timeout 5 seconds.
                urlc.connect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }


    }

    public boolean isServiceUp() { // check network Service is available
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                new ServiceUpBK().execute("");
                for (int k = 0; k < 100; k++) {
                    for (int l = 0; l < 100; l++) {
                        System.out.println("kl " + k + l);
                    }
                }
                if (urlc.getResponseCode() == 200) { // Successful response.
                    return true;
                } else {
                    Log.d("NO INTERNET", "NO INTERNET");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            Intent b = getIntent();
//            String username = b.getStringExtra("username");
//            String pword = b.getStringExtra("pword");
//            String sim = b.getStringExtra("SimNo");
//            System.out.println("* SHARED_LOGOUT " + SharedPrefManager.getLocalSharedPreference(LoginPage.this, CONSTANTS.SHARED_LOGOUT));
//            if (username != null && pword != null & sim != null) {
//                CheckLogin(username, pword, sim);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    @Deprecated
    public Object onRetainNonConfigurationInstance() {
        StateServer data = new StateServer();
        if (mSpashDialog != null) {
            data.showSplashScreen = true;
            removeSplash();
        }
        return data;
    }

    /**
     * Show Splash Screen function.
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mSpashDialog != null && mSpashDialog.isShowing()) {
            mSpashDialog.cancel();
        }
    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpashDialog != null && mSpashDialog.isShowing()) {
            mSpashDialog.cancel();
        }
    }

    protected void showSplashScreen(Context context) {
        mSpashDialog = new Dialog(context, R.style.SplashScreen);
        mSpashDialog.setContentView(R.layout.splashscreen);
        mSpashDialog.setCancelable(false);
        if (mSpashDialog != null) {
            mSpashDialog.show();
        }

//		for(int i=0;i<100;i++){
//			for (int j=0;j<100;j++){
//				System.out.println(" if "+i +j);
//			}
//		}
        //	removeSplash();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                removeSplash();
                mSpashDialog = null;
            }
        }, 5000);
    }

    /**
     * Remove the splash screen function
     */
    protected void removeSplash() {
        try {
            if (mSpashDialog != null) {
                mSpashDialog.dismiss();
                //  mSpashDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StateServer {
        public boolean showSplashScreen = false;
    }

    private class SystemProfileAsyc extends AsyncTask<String, Void, String> {

        private Context context;
        private HTTPServiceCalls http;
        private ProgressDialog pg;

        public SystemProfileAsyc(Context context) {
            super();
            this.context = context;
            http = new HTTPServiceCalls();
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pg = ProgressDialog.show(this.context, "Please wait", "Updating Server url");
        }


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            DatabaseHandler dbh = new DatabaseHandler(this.context);
            try {
                String epf = dbh.GetEmpNoByPhoneNo(Utils.getSimSerialNumber(this.context));
                if (!epf.equals("")) {
                    return http.makeRequest(CONSTANTS.GET_SERVICE_URL(epf));
                }
            } catch (Exception e) {
                Utils.writeToErrLogFileWithTime(e);
                e.printStackTrace();
            } finally {
                dbh.close();
            }

            return "{\"response\":{\"status\":0,\"data\":null,\"msg\":\"Error while requesting data\"}}";
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            pg.cancel();
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            DatabaseHandler dbh = new DatabaseHandler(this.context);
            pg.cancel();
            super.onPostExecute(result);
            try {
                JSONParser parser = new JSONParser();
                Object parsed = parser.parse(result);
                JSONObject objJSonAtd = (JSONObject) parsed;

                JSONObject jResponse = (JSONObject) objJSonAtd.get("response");

                int status = ((Number) jResponse.get("status")).intValue();

                if (status == 1) {
                    String url = (String) jResponse.get("data");
                    if (!url.trim().equals("")) {
                        if (dbh.updateServiceUrl(url)) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
                            startHomeActivity();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.writeToErrLogFileWithTime(e);
            } finally {
                dbh.close();
            }
        }

    }

    public void startHomeActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
        startActivity(intent);
        finish();
    }

    private class updateSync extends AsyncTask<String, String, String> {

        String simNo;

        public updateSync(String phone) {
            simNo = phone;
        }

        @Override
        protected String doInBackground(String... strings) {
            UpdateOrCheckTSRSystemprofile(simNo);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private class BlockSync extends AsyncTask<String, String, String> {
        private Context context;
        private String simSerial;

        public BlockSync(Context context, String simSerial) {
            this.context = context;
            this.simSerial = simSerial;
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request20 = new SoapObject(NAMESPACE, METHOD_NAME20);
            request20.addProperty("strInputUserMobile", simSerial);
            request20.addProperty("strInputUserName", userName);
            request20.addProperty("strInputUserPassword", password);
            SoapSerializationEnvelope envelope20 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope20.setOutputSoapObject(request20);
            envelope20.dotNet = true;
            String result21 = "";
            SoapObject result20 = null;
            try {
                HttpTransportSE androidHttpTransport20 = new HttpTransportSE(URL);
                androidHttpTransport20.call(SOAP_ACTION20, envelope20);
                if (envelope20.bodyIn instanceof SoapFault) {
                    String result20String = ((SoapFault) envelope20.bodyIn).faultstring;
                    result21 = result20String;
                    System.out.println(result21);
                } else {
                    SoapObject result20String = (SoapObject) envelope20.bodyIn;
                    result20 = result20String;
                }
                if (result20 != null) {
                    String dbQuery20 = result20.getProperty(0).toString();
                    Gson gson = new Gson();

                    initialData = gson.fromJson(dbQuery20, InitialData.class);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("* POST EXECUTED  " + result);
/*

            if (!initialData.getCityData().equals("[]")) {
                String dbQuery5 = initialData.getCityData();
                String Value5 = dbQuery5.replaceAll("\\[|\\]", "");
                String[] statusarray5 = Value5.split(",");
                String[] statusarrayData5 = Value5.split("<");
                for (int i = 1; i < statusarrayData5.length; i++) {
                    String[] datavaluesStrings = statusarrayData5[i].split(">");
                    String[] datavalues = datavaluesStrings[0].split(",");
                    dbh.SaveCityData(datavalues[0], datavalues[1], datavalues[2], datavalues[3], datavalues[4], datavalues[5], datavalues[6], datavalues[7]);
                }
                System.out.println("* date1 " + date);
                dbh.SetCityPostalCodesUpdatedDate(date);
            }
            if (true) {
                if (!initialData.getCardTypeData().equals("[]")) {
                    String dbQuery6 = initialData.getCardTypeData();
                    String dbQuery6String = dbQuery6.replaceAll("\\[|\\]", "");
                    String[] statusarray6 = dbQuery6String.split(",");
                    String[] statusarrayData6 = dbQuery6String.split("<");
                    for (int i = 1; i < statusarrayData6.length; i++) {
                        String[] datavaluesStrings = statusarrayData6[i].split(">");
                        String[] datavalues = datavaluesStrings[0].split(",");
                        dbh.SaveCardTypes(datavalues[0], datavalues[1], datavalues[2], datavalues[3]);
                    }
                    System.out.println("* date2 " + date);
                    dbh.SetCardTypesUpdatedDate(date);
                }

            }


            try {
                String dbQuery3 = initialData.getLoginData();
                System.out.println("* dbQuery3 initialData.getLoginData() second " + dbQuery3);

            } catch (Exception e) {
                e.printStackTrace();
            }

            String dbQuery = initialData.getServerDate();
            String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
            String[] statusarrayData61 = dbQueryString.split("<");

            for (int i = 1; i < statusarrayData61.length; i++) {

                String[] datavaluesStrings = statusarrayData61[i].split(">");
                String[] datavalues = datavaluesStrings[0].split(",");
                String serverDateStr = datavalues[0];

                System.out.println("* sync BlockSync "+serverDateStr);
                dbh.SaveServerDate(serverDateStr);// serverDate.toString());//new
            }

            new updateSync(Utils.getSimSerialNumber).execute("");
*/

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
