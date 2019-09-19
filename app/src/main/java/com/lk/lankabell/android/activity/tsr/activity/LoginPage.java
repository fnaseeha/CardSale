package com.lk.lankabell.android.activity.tsr.activity;

import java.io.File;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            CheckLogin(username, pword, sim);
        }

    }

    @SuppressLint("SimpleDateFormat")
    public void onClickLogin(View view) throws ParseException {
        AllSerialNumbers = Utils.getSerialNumbers(this);
        System.out.println("* onClickLogin AllSerialNumbers " + AllSerialNumbers.size());

        checkInternet();

        dbh = new DatabaseHandler(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        date = sdf.format(System.currentTimeMillis());

        error.setText("");
        //String telephone =  Utils.getSimSerialNumber(this);
        String telephone = AllSerialNumbers.size() > 0 ? AllSerialNumbers.get(0) : "";

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

                    boolean deletedb = getApplicationContext().deleteDatabase("TSRDBNEW");
                    Log.d("Database is ", deletedb + "");
                    dbh = new DatabaseHandler(getApplicationContext());
                    initialData = new InitialData();
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                    request.addProperty("strInputUserMobile", telephone);
                    request.addProperty("strInputUserName", userName);
                    request.addProperty("strInputUserPassword", password);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);
                    envelope.dotNet = true;
                    count++;
                    try {

                        (new GetAync(this)).execute("");

                        for (int i = 0; i < 100; i++) {
                            for (int j = 0; j < 50; j++) {
                                System.out.println(" if " + i + j);
                            }
                        }

                        if (result != null) {

                            String result_value = result.getProperty(0).toString();
                            int result_valus = Integer.parseInt(result_value);

                            if (true) {

                                CONSTANTS.FinalSimSerialNumber = telephone;
                                showSplashScreen(this);

                                dbh.InsertTableData();// updated times insert to
                                // table
                                File path = getApplicationContext().getDatabasePath("TSRDBNEW");
                                Log.d("DataBasde Path is :", path.getAbsolutePath());
                                //========================LOGIN  ==========
                                // BLOCK START
                                new BlockSync(this,telephone).execute("");

                                for (int i = 0; i < 100; i++) {
                                    for (int j = 0; j < 1500; j++) {
                                        System.out.println(" if " + i + j);
                                    }
                                }

                               /* Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("* initialData delayed "+initialData);
                                    }
                                }, 2000);*/
//
                                // /======================================================================================/LOGIN
                                // BLOCK END

                                if (true) {

                                    System.out.println("* initialData "+initialData);
                                    try {
                                        String dbQuery3 = initialData.getLoginData();
                                        System.out.println("* dbQuery3 "+dbQuery3);

                                        if(!dbQuery3.equals("[]")){
                                            String[] statusarray3 = dbQuery3.split(",");
                                            dbh.SaveLoginData(statusarray3[0].replaceAll("\\[|\\]|\\<", ""),
                                                    statusarray3[2],
                                                    statusarray3[3].replaceAll("\\[|\\]|\\>", ""),
                                                    statusarray3[1]);

                                        }

                                         sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                                        date = sdf.format(System.currentTimeMillis());
                                        System.out.println("* date "+date);
                                        dbh.SetLoginDetailsUpdatedDate(date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (true) {

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
                                            System.out.println("* date1 "+date);
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
                                                System.out.println("* date2 "+date);
                                                dbh.SetCardTypesUpdatedDate(date);
                                            }
                                            if (!initialData.getMerchantData().equals("[]")) {
                                                String dbQuery7 = initialData.getMerchantData();
                                                String dbQuery7String = dbQuery7.replaceAll("\\[|\\]", "");
                                                String[] statusarray7 = dbQuery7String.split(",");
                                                String[] statusarrayData7 = dbQuery7String.split("<");
                                                for (int i = 1; i < statusarrayData7.length; i++) {
                                                    String[] datavaluesStrings = statusarrayData7[i].split(">");
                                                    String[] datavalues = datavaluesStrings[0].split("#");
                                                    // if(!(datavalues[4].equals("null"))
                                                    // &&
                                                    // (!datavalues[5].equals("null"))){

                                                    Long merchantId = Long.valueOf(datavalues[0]);
                                                    String name = datavalues[1];
                                                    String address = datavalues[2];
                                                    String lat = datavalues[4];
                                                    String lon = datavalues[5];
                                                    String registeredOn = datavalues[7];
                                                    String mobileNo = datavalues[8];
                                                    String city = datavalues[10];
                                                    int IsActive = Integer.parseInt(datavalues[9]);
                                                    String RegisteredByEpf = datavalues[6];
                                                    int isRegistered = Integer.parseInt(datavalues[3]);

                                                    int isAssigned = Integer.parseInt(datavalues[11]);
                                                    int isEdited = Integer.parseInt(datavalues[12]);
                                                    String reloadNo = datavalues[13];

                                                    dbh.SaveMerchantDetails(merchantId, name, address, lat, lon, registeredOn, mobileNo, city, IsActive, RegisteredByEpf, isRegistered, isAssigned, isEdited, reloadNo);
                                                    // }
                                                }
                                                System.out.println("* date3 "+date);
                                                dbh.SetMerchantMasterUpdatedDate(date);

                                            }

                                            if (!initialData.getCardBulkSerialData().equals("[]")) {
                                                String dbQuery8 = initialData.getCardBulkSerialData();
                                                String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                                                String[] statusarray8 = dbQuery8String.split(",");
                                                String[] statusarrayData8 = dbQuery8String.split("<");
                                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                                                String dateValue = sdf1.format(System.currentTimeMillis());

                                                if (!statusarray8[0].equalsIgnoreCase("")) {
                                                    if (statusarray8[1].length() > 1) {
                                                        for (int i = 1; i < statusarrayData8.length; i++) {
                                                            String[] datavaluesStrings = statusarrayData8[i].split(">");
                                                            String[] datavalues = datavaluesStrings[0].split(",");
                                                            if (dbh.CheckCardBulkRecordIsExists(datavalues[3], Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")), datavalues[0], datavalues[1], datavalues[2]) == false) {
                                                                dbh.SaveBulkIds(datavalues[3], Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")), datavalues[0], datavalues[1], datavalues[2], dateValue, Double.parseDouble(datavalues[6]), Integer.parseInt(datavalues[7]), Integer.parseInt(datavalues[8]));
                                                            }
                                                            if (i == 1) {
                                                                if (statusarrayData8.length > 1) {
                                                                    dbh.SetBulkIdUpdatedDate(datavalues[5]);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (true) {
                                                if (initialData.getCardDenominationData() != null) {
                                                    String dbQuery8 = initialData.getCardDenominationData();
                                                    String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                                                    String[] statusarray8 = dbQuery8String.split(",");
                                                    String[] statusarrayData8 = dbQuery8String.split("<");

                                                    for (int i = 1; i < statusarrayData8.length; i++) {
                                                        String[] datavaluesStrings = statusarrayData8[i].split(">");
                                                        String[] datavalues = datavaluesStrings[0].split(",");
                                                        dbh.SaveCardDenominations(datavalues[1], datavalues[0], Double.parseDouble(datavalues[0]), Double.parseDouble(datavalues[0]));
                                                    }
                                                    System.out.println("* date30 "+date);
                                                    dbh.SetCardDenominationUpdatedDate(date);
                                                }
                                                if (initialData.getMerchantInventoryData() != null) {
                                                    String result_Value = initialData.getMerchantInventoryData();
                                                    if (result_Value.isEmpty() || result_Value == null || result_Value.equals("[]")) {
                                                    } else {
                                                        String res1 = result_Value.replaceAll("\\[|\\]", "");
                                                        String[] ByComma = res1.split(",");
                                                        String responeValueString = ByComma[0];

                                                        for (int i = 0; i < ByComma.length; i++) {
                                                            String AllBlockData = ByComma[i].replaceAll("\\<|\\>", "");
                                                            String[] dataElements = AllBlockData.split("#");

                                                            if (dataElements[0].length() != 1) {
                                                                String merchantid = dataElements[0].trim();
                                                                String cardType = dataElements[1];
                                                                String Denomination = dataElements[2];
                                                                String StocksInha = dataElements[3];
                                                                String rolevel = dataElements[4];
                                                                String date1 = dataElements[5];
                                                                String activationCount = dataElements[6];
                                                                String saleCount = dataElements[7];
                                                                if (i == 1) {
                                                                    System.out.println("* date31 "+date);
                                                                    dbh.SetMerchantInventorydatedDate(date1);
                                                                }
                                                                dbh.SaveMerchantInventoryTableAtLogin(Long.parseLong(merchantid), cardType, Integer.parseInt(Denomination), Integer.parseInt(StocksInha), date1, Integer.parseInt(rolevel), Integer.parseInt(activationCount), Integer.parseInt(saleCount));
                                                            }
                                                        }
                                                    }
                                                }

                                                if (!initialData.getNextSerialData().equals("[]")) {
                                                    String dbQuery = initialData.getNextSerialData();
                                                    Log.d("&&&&&&&&&& ", dbQuery);
                                                    String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                                    String[] statusarray61 = dbQueryString.split(",");
                                                    String[] statusarrayData61 = dbQueryString.split("<");
                                                    for (int i = 1; i < statusarrayData61.length; i++) {
                                                        String[] datavaluesStrings = statusarrayData61[i].split(">");
                                                        String[] datavalues = datavaluesStrings[0].split(",");
                                                        dbh.SaveSerials(Integer.valueOf(datavalues[0]), datavalues[1], Integer.valueOf(datavalues[2]), Integer.valueOf(datavalues[3]), Integer.valueOf(datavalues[4]), Integer.valueOf(datavalues[5]), Integer.valueOf(datavalues[6]), Integer.valueOf(datavalues[7]));
                                                    }
                                                }
                                                // Log.d("Come to the my method 01 : ",
                                                // "My method 001 ");
                                                // UpdateOrCheckTSRSystemprofile();
                                                /*
                                                 * if (true) { String
                                                 * result_Value =
                                                 * initialData.getSalesInvoiceID
                                                 * (); if (result_Value != null)
                                                 * { int result_Value_num =
                                                 * Integer
                                                 * .valueOf(result_Value.replace
                                                 * ("[", "").replace("]", ""));
                                                 *
                                                 * if (result_Value_num == 1) {
                                                 * //User user1 =
                                                 * dbh.getUserDetails();
                                                 * //String first_sales_id =
                                                 * Integer
                                                 * .toString(user1.getId()
                                                 * ).concat("1");
                                                 * dbh.SaveLastInvoiceNo
                                                 * (1000000000); } else { String
                                                 * value = ""+result_Value_num;
                                                 * String invoiceid =
                                                 * value.substring(0, 9);
                                                 *
                                                 *
                                                 * dbh.SaveLastInvoiceNo(Integer.
                                                 * parseInt(invoiceid)); } } }
                                                 *
                                                 * SetMerchantId();
                                                 */
                                                if (true) {
                                                    String dbQuery = initialData.getServerDate();
                                                    String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                                    String[] statusarrayData61 = dbQueryString.split("<");

                                                    for (int i = 1; i < statusarrayData61.length; i++) {

                                                        String[] datavaluesStrings = statusarrayData61[i].split(">");
                                                        String[] datavalues = datavaluesStrings[0].split(",");
                                                        String serverDateStr = datavalues[0];
                                                        //String server_time = serverDateStr.replace(".0", "");
                                                        // SimpleDateFormat
                                                        // formatter= new
                                                        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                                        // Date serverDate = new
                                                        // Date(formatter.format(serverDateStr));//
                                                        // formatter.parse(serverDateStr);
                                                        dbh.SaveServerDate(serverDateStr);// serverDate.toString());//new
                                                        // Date()
                                                    }
                                                }

                                                String dateString = dbh.getServerDate();
                                                System.out.println("* dateString n 1 "+dateString);
                                                java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

                                                Date dateToday = new Date();
                                                Log.d("dates", "Server date : " + date1 + " Phone Date : " + dateToday);
                                                // System.out.println(output);

                                                DateTime phoneDate = new DateTime(dateToday);
                                                DateTime serverDate = new DateTime(date1);
                                                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
                                                Toast toast = Toast.makeText(LoginPage.this, "Initial data synched! Version : " + version + " ", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                toast.show();

                                                Log.d("Come to the my m : ", "My method 001 ");
                                                new updateSync().execute("");
//                                                for (int i = 0; i < 100; i++) {
//                                                    for (int j = 0; j < 100; j++) {
//                                                        System.out.println(" if " + i + j);
//                                                    }
//                                                }
                                                //	UpdateOrCheckTSRSystemprofile();

                                                Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                                                int diffN = Math.abs(diff);
                                                if ((diffN < 30)) {
//													Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
//													startActivity(intent);
//													finish();
                                                    if (CONSTANTS.IS_USING_ORGINAL_URL) {
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
                                        }

                                    }

                                }

                                dbh.UpdateLoginData(result.getProperty(0).toString(), userName, password);
                            } else if (result_valus == 0) {
                                Toast.makeText(getApplicationContext(), "Invalid username & Password or Invalid Mobile Number", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                        }
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
                    //boolean service_enable = isServiceUp();
                    String pwdByUserName = dbh.GetPasswordByUserName(userName);
                    if (pwdByUserName.equalsIgnoreCase(password)) {
                        String userNameByMobile = dbh.GetNameByMobileNo(telephone);
                        if (userNameByMobile.equals("")) {
                            //error.setText("Invalid Mobile Number");
                            TryAgainLowerCase(telephone.toLowerCase(), dbh);

                        } else {
                            int status = synchVersion();
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
            int status = synchVersion();
            if (status == 0) {
                OfflineLogin(userName, password, telephone, error);
            } else {
                String dateString = "2014-03-13 00:00:00.0";
                dateString = this.dbh.getServerDate();
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
            int status = synchVersion();
            if (status == 0) {
                OfflineLogin(userName, password, telephone, error);
            } else {
                String dateString = "2014-03-13 00:00:00.0";
                dateString = this.dbh.getServerDate();
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

    private void CheckLogin(String username, String pword, String sim) {
        System.out.println("* Check Login firstrun "+firstrun+" username "+username+" pword "+pword+" sim "+sim);
        String telephone = sim;
        String userName = username;
        String password = pword;
        if (firstrun) {
            network_enable = isOnline();
            if (network_enable) {

                boolean deletedb = getApplicationContext().deleteDatabase("TSRDBNEW");
                Log.d("Database is ", deletedb + "");
                dbh = new DatabaseHandler(getApplicationContext());
                initialData = new InitialData();
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                request.addProperty("strInputUserMobile", telephone);
                request.addProperty("strInputUserName", userName);
                request.addProperty("strInputUserPassword", password);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                count++;
                try {

                    (new GetAync(this)).execute("");

                    for (int i = 0; i < 100; i++) {
                        for (int j = 0; j < 100; j++) {
                            System.out.println(" iff " + i + j);
                        }
                    }

                    if (result != null) {

                        String result_value = result.getProperty(0).toString();
                        int result_valus = Integer.parseInt(result_value);
                        if (true) {
                            CONSTANTS.FinalSimSerialNumber = telephone;
                            showSplashScreen(this);

                            dbh.InsertTableData();// updated times insert to
                            // table
                            File path = getApplicationContext().getDatabasePath("TSRDBNEW");
                            Log.d("DataBasde Path is :", path.getAbsolutePath());
                            //========================LOGIN  ==========
                            // BLOCK START
                            new BlockSync(this,telephone).execute("");
                            for (int i = 0; i < 100; i++) {
                                for (int j = 0; j < 500; j++) {
                                    System.out.println(" if " + i + j);
                                }
                            }
//
                            // /======================================================================================/LOGIN
                            // BLOCK END

                            if (true) {

                                try {
                                    System.out.println("* initialData "+initialData);
                                    String dbQuery3 = initialData.getLoginData();
                                    System.out.println("* dbQuery31 "+dbQuery3);

                                    if(!dbQuery3.equals("[]")){
                                        String[] statusarray3 = dbQuery3.split(",");
                                        dbh.SaveLoginData(statusarray3[0].replaceAll("\\[|\\]|\\<", ""), statusarray3[2], statusarray3[3].replaceAll("\\[|\\]|\\>", ""), statusarray3[1]);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                                        date = sdf.format(System.currentTimeMillis());
                                        System.out.println("* datec 1 "+date);
                                        dbh.SetLoginDetailsUpdatedDate(date);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (true) {

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
                                        System.out.println("* datec 2 "+date);
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
                                            System.out.println("* datec 3 "+date);
                                            dbh.SetCardTypesUpdatedDate(date);
                                        }
                                        if (!initialData.getMerchantData().equals("[]")) {
                                            String dbQuery7 = initialData.getMerchantData();
                                            String dbQuery7String = dbQuery7.replaceAll("\\[|\\]", "");
                                            String[] statusarray7 = dbQuery7String.split(",");
                                            String[] statusarrayData7 = dbQuery7String.split("<");
                                            for (int i = 1; i < statusarrayData7.length; i++) {
                                                String[] datavaluesStrings = statusarrayData7[i].split(">");
                                                String[] datavalues = datavaluesStrings[0].split("#");
                                                // if(!(datavalues[4].equals("null"))
                                                // &&
                                                // (!datavalues[5].equals("null"))){

                                                Long merchantId = Long.valueOf(datavalues[0]);
                                                String name = datavalues[1];
                                                String address = datavalues[2];
                                                String lat = datavalues[4];
                                                String lon = datavalues[5];
                                                String registeredOn = datavalues[7];
                                                String mobileNo = datavalues[8];
                                                String city = datavalues[10];
                                                int IsActive = Integer.parseInt(datavalues[9]);
                                                String RegisteredByEpf = datavalues[6];
                                                int isRegistered = Integer.parseInt(datavalues[3]);

                                                int isAssigned = Integer.parseInt(datavalues[11]);
                                                int isEdited = Integer.parseInt(datavalues[12]);
                                                String reloadNo = datavalues[13];

                                                dbh.SaveMerchantDetails(merchantId, name, address, lat, lon, registeredOn, mobileNo, city, IsActive, RegisteredByEpf, isRegistered, isAssigned, isEdited, reloadNo);
                                                // }
                                            }
                                            System.out.println("* datec 4 "+date);
                                            dbh.SetMerchantMasterUpdatedDate(date);

                                        }

                                        if (!initialData.getCardBulkSerialData().equals("[]")) {
                                            String dbQuery8 = initialData.getCardBulkSerialData();
                                            String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                                            String[] statusarray8 = dbQuery8String.split(",");
                                            String[] statusarrayData8 = dbQuery8String.split("<");
                                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                                            String dateValue = sdf1.format(System.currentTimeMillis());

                                            if (!statusarray8[0].equalsIgnoreCase("")) {
                                                if (statusarray8[1].length() > 1) {
                                                    for (int i = 1; i < statusarrayData8.length; i++) {
                                                        String[] datavaluesStrings = statusarrayData8[i].split(">");
                                                        String[] datavalues = datavaluesStrings[0].split(",");
                                                        if (dbh.CheckCardBulkRecordIsExists(datavalues[3],
                                                                Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")),
                                                                datavalues[0],
                                                                datavalues[1],
                                                                datavalues[2]) == false) {
                                                            dbh.SaveBulkIds(datavalues[3], Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")), datavalues[0], datavalues[1], datavalues[2], dateValue, Double.parseDouble(datavalues[6]), Integer.parseInt(datavalues[7]), Integer.parseInt(datavalues[8]));
                                                        }
                                                        if (i == 1) {
                                                            if (statusarrayData8.length > 1) {
                                                                dbh.SetBulkIdUpdatedDate(datavalues[5]);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (true) {
                                            if (!initialData.getCardDenominationData().equals("[]")) {
                                                String dbQuery8 = initialData.getCardDenominationData();
                                                String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                                                String[] statusarray8 = dbQuery8String.split(",");
                                                String[] statusarrayData8 = dbQuery8String.split("<");

                                                for (int i = 1; i < statusarrayData8.length; i++) {
                                                    String[] datavaluesStrings = statusarrayData8[i].split(">");
                                                    String[] datavalues = datavaluesStrings[0].split(",");
                                                    dbh.SaveCardDenominations(datavalues[1], datavalues[0], Double.parseDouble(datavalues[0]), Double.parseDouble(datavalues[0]));
                                                }
                                                System.out.println("* datec 5 "+date);
                                                dbh.SetCardDenominationUpdatedDate(date);
                                            }
                                            if (!initialData.getMerchantInventoryData().equals("[]")) {
                                                String result_Value = initialData.getMerchantInventoryData();
                                                if (result_Value.isEmpty() || result_Value == null || result_Value.equals("[]")) {
                                                } else {
                                                    String res1 = result_Value.replaceAll("\\[|\\]", "");
                                                    String[] ByComma = res1.split(",");
                                                    String responeValueString = ByComma[0];

                                                    for (int i = 0; i < ByComma.length; i++) {
                                                        String AllBlockData = ByComma[i].replaceAll("\\<|\\>", "");
                                                        String[] dataElements = AllBlockData.split("#");

                                                        if (dataElements[0].length() != 1) {
                                                            String merchantid = dataElements[0].trim();
                                                            String cardType = dataElements[1];
                                                            String Denomination = dataElements[2];
                                                            String StocksInha = dataElements[3];
                                                            String rolevel = dataElements[4];
                                                            String date1 = dataElements[5];
                                                            String activationCount = dataElements[6];
                                                            String saleCount = dataElements[7];
                                                            if (i == 1) {
                                                                dbh.SetMerchantInventorydatedDate(date1);
                                                            }
                                                            dbh.SaveMerchantInventoryTableAtLogin(Long.parseLong(merchantid), cardType, Integer.parseInt(Denomination), Integer.parseInt(StocksInha), date1, Integer.parseInt(rolevel), Integer.parseInt(activationCount), Integer.parseInt(saleCount));
                                                        }
                                                    }
                                                }
                                            }

                                            if (!initialData.getNextSerialData().equals("[]")) {
                                                String dbQuery = initialData.getNextSerialData();
                                                Log.d("&&&&&&&&&& ", dbQuery);
                                                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                                String[] statusarray61 = dbQueryString.split(",");
                                                String[] statusarrayData61 = dbQueryString.split("<");
                                                for (int i = 1; i < statusarrayData61.length; i++) {
                                                    String[] datavaluesStrings = statusarrayData61[i].split(">");
                                                    String[] datavalues = datavaluesStrings[0].split(",");
                                                    dbh.SaveSerials(Integer.valueOf(datavalues[0]), datavalues[1], Integer.valueOf(datavalues[2]), Integer.valueOf(datavalues[3]), Integer.valueOf(datavalues[4]), Integer.valueOf(datavalues[5]), Integer.valueOf(datavalues[6]), Integer.valueOf(datavalues[7]));
                                                }
                                            }
                                            // Log.d("Come to the my method 01 : ",
                                            // "My method 001 ");
                                            // UpdateOrCheckTSRSystemprofile();
                                            /*
                                             * if (true) { String
                                             * result_Value =
                                             * initialData.getSalesInvoiceID
                                             * (); if (result_Value != null)
                                             * { int result_Value_num =
                                             * Integer
                                             * .valueOf(result_Value.replace
                                             * ("[", "").replace("]", ""));
                                             *
                                             * if (result_Value_num == 1) {
                                             * //User user1 =
                                             * dbh.getUserDetails();
                                             * //String first_sales_id =
                                             * Integer
                                             * .toString(user1.getId()
                                             * ).concat("1");
                                             * dbh.SaveLastInvoiceNo
                                             * (1000000000); } else { String
                                             * value = ""+result_Value_num;
                                             * String invoiceid =
                                             * value.substring(0, 9);
                                             *
                                             *
                                             * dbh.SaveLastInvoiceNo(Integer.
                                             * parseInt(invoiceid)); } } }
                                             *
                                             * SetMerchantId();
                                             */
                                            if (!initialData.getServerDate().equals("[]")) {
                                                String dbQuery = initialData.getServerDate();
                                                System.out.println("* dbQuery "+dbQuery);
                                                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                                String[] statusarrayData61 = dbQueryString.split("<");

                                                for (int i = 1; i < statusarrayData61.length; i++) {

                                                    String[] datavaluesStrings = statusarrayData61[i].split(">");
                                                    String[] datavalues = datavaluesStrings[0].split(",");
                                                    String serverDateStr = datavalues[0];
                                                  //  String server_time = serverDateStr.replace(".0", "");
                                                    // SimpleDateFormat
                                                    // formatter= new
                                                    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                                    // Date serverDate = new
                                                    // Date(formatter.format(serverDateStr));//
                                                    // formatter.parse(serverDateStr);
                                                    dbh.SaveServerDate(serverDateStr);// serverDate.toString());//new
                                                    // Date()
                                                }
                                            }

                                            String dateString = dbh.getServerDate();
                                            System.out.println("* dateString n 2 "+dateString);
                                            Date date1 = null;
                                            try {
                                                 date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString.replace(".0", ""));
                                            }
                                            Date dateToday = new Date();
                                            Log.d("dates", "Server date : " + date1 + " Phone Date : " + dateToday);
                                            // System.out.println(output);

                                            DateTime phoneDate = new DateTime(dateToday);
                                            DateTime serverDate = new DateTime(date1);
                                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
                                            Toast toast = Toast.makeText(LoginPage.this, "Initial data synched! Version : " + version + " ", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                            toast.show();

                                            Log.d("Come to the my m : ", "My method 001 ");
                                            new updateSync().execute("");
                                            for (int i = 0; i < 100; i++) {
                                                for (int j = 0; j < 100; j++) {
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
                                    }

                                }

                            }

                            dbh.UpdateLoginData(result.getProperty(0).toString(), userName, password);
                        } else if (result_valus == 0) {
                            Toast.makeText(getApplicationContext(), "Invalid username & Password or Invalid Mobile Number", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                    }
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
        }  else {
            network_enable = isOnline();
            if (network_enable) {
                //boolean service_enable = isServiceUp();
                String pwdByUserName = dbh.GetPasswordByUserName(username);
                if (pwdByUserName.equalsIgnoreCase(pword)) {
                    String userNameByMobile = dbh.GetNameByMobileNo(sim);
                    if (!userNameByMobile.equals("")) {
                        try {
                            int status = synchVersion();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        //retry
                    }
                }
            } else {
                // =========================================DATE SYNCH WITH
                // SERVER DATE=====================================
                String dateString = "2014-03-13 00:00:00.0";
                dateString = dbh.getServerDate();

                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date dateToday = null; // new
                try {
                    dateToday = formatter.parse(formatter.format(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                } else if (userNameByMobile.equalsIgnoreCase(userName) && pwdByUserName.equalsIgnoreCase(pword)) {
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
                System.out.println("* resultSync "+resultSync);
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

            System.out.println("Location Register details ok");

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

    private void UpdateOrCheckTSRSystemprofile() {
        User user = dbh.getUserDetails();
        String epfNo = dbh.GetEmpNoByPhoneNo(user.getmobileNo());
        Log.d("Come to the", "My method 00222 ");
        new SysncServices().TSRSystemprofile(getApplicationContext(), user.getmobileNo(), user.getUserName(), user.getPassword(), epfNo, "LoginInishial");
    }

    private static String SOAP_ACTION21 = "http://mainService/synchServerDate";
    private static String METHOD_NAME21 = "synchServerDate";

    private Integer synchVersion() throws ParseException {
        Integer returnVal = 0;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME21);
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        User user = dbh.getUserDetails();
        if (true) {
            request.addProperty("strInputUserMobile", Utils.getSimSerialNumber(this));
            request.addProperty("strInputUserName", user.getUserName());
            request.addProperty("strInputUserPassword", user.getPassword());
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
        try {
            Intent b = getIntent();
            String username = b.getStringExtra("username");
            String pword = b.getStringExtra("pword");
            String sim = b.getStringExtra("SimNo");
            System.out.println("* SHARED_LOGOUT " + SharedPrefManager.getLocalSharedPreference(LoginPage.this, CONSTANTS.SHARED_LOGOUT));
            if (username != null && pword != null & sim != null) {
                CheckLogin(username, pword, sim);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if ( mSpashDialog!=null && mSpashDialog.isShowing() )
        {
            mSpashDialog.cancel();
        }
    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mSpashDialog!=null && mSpashDialog.isShowing())
        {
            mSpashDialog.cancel();
        }
    }
    protected void showSplashScreen(Context context) {
        mSpashDialog = new Dialog(context, R.style.SplashScreen);
        mSpashDialog.setContentView(R.layout.splashscreen);
        mSpashDialog.setCancelable(false);
        if(mSpashDialog != null ){
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


        @Override
        protected String doInBackground(String... strings) {
            UpdateOrCheckTSRSystemprofile();
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

        public BlockSync(Context context,String simSerial) {
            this.context = context;
            this.simSerial = simSerial;
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request20 = new SoapObject(NAMESPACE, METHOD_NAME20);
            request20.addProperty("strInputUserMobile", simSerial);
            request20.addProperty("strInputUserName", userName);
            request20.addProperty("strInputUserPassword", password);
            SoapSerializationEnvelope envelope20 = new SoapSerializationEnvelope(SoapEnvelope.VER12);
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
                    System.out.println("* initialData initialData  "+initialData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (true) {

                System.out.println("* initialData "+initialData);
                try {
                    String dbQuery3 = initialData.getLoginData();
                    System.out.println("* dbQuery3 "+dbQuery3);

                    if(!dbQuery3.equals("[]")){
                        String[] statusarray3 = dbQuery3.split(",");
                        dbh.SaveLoginData(statusarray3[0].replaceAll("\\[|\\]|\\<", ""),
                                statusarray3[2],
                                statusarray3[3].replaceAll("\\[|\\]|\\>", ""),
                                statusarray3[1]);

                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                    date = sdf.format(System.currentTimeMillis());
                    System.out.println("* date "+date);
                    dbh.SetLoginDetailsUpdatedDate(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (true) {

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
                        System.out.println("* date1 "+date);
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
                            System.out.println("* date2 "+date);
                            dbh.SetCardTypesUpdatedDate(date);
                        }
                        if (!initialData.getMerchantData().equals("[]")) {
                            String dbQuery7 = initialData.getMerchantData();
                            String dbQuery7String = dbQuery7.replaceAll("\\[|\\]", "");
                            String[] statusarray7 = dbQuery7String.split(",");
                            String[] statusarrayData7 = dbQuery7String.split("<");
                            for (int i = 1; i < statusarrayData7.length; i++) {
                                String[] datavaluesStrings = statusarrayData7[i].split(">");
                                String[] datavalues = datavaluesStrings[0].split("#");
                                // if(!(datavalues[4].equals("null"))
                                // &&
                                // (!datavalues[5].equals("null"))){

                                Long merchantId = Long.valueOf(datavalues[0]);
                                String name = datavalues[1];
                                String address = datavalues[2];
                                String lat = datavalues[4];
                                String lon = datavalues[5];
                                String registeredOn = datavalues[7];
                                String mobileNo = datavalues[8];
                                String city = datavalues[10];
                                int IsActive = Integer.parseInt(datavalues[9]);
                                String RegisteredByEpf = datavalues[6];
                                int isRegistered = Integer.parseInt(datavalues[3]);

                                int isAssigned = Integer.parseInt(datavalues[11]);
                                int isEdited = Integer.parseInt(datavalues[12]);
                                String reloadNo = datavalues[13];

                                dbh.SaveMerchantDetails(merchantId, name, address, lat, lon, registeredOn, mobileNo, city, IsActive, RegisteredByEpf, isRegistered, isAssigned, isEdited, reloadNo);
                                // }
                            }
                            System.out.println("* date3 "+date);
                            dbh.SetMerchantMasterUpdatedDate(date);

                        }

                        if (!initialData.getCardBulkSerialData().equals("[]")) {
                            String dbQuery8 = initialData.getCardBulkSerialData();
                            String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                            String[] statusarray8 = dbQuery8String.split(",");
                            String[] statusarrayData8 = dbQuery8String.split("<");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                            String dateValue = sdf1.format(System.currentTimeMillis());

                            if (!statusarray8[0].equalsIgnoreCase("")) {
                                if (statusarray8[1].length() > 1) {
                                    for (int i = 1; i < statusarrayData8.length; i++) {
                                        String[] datavaluesStrings = statusarrayData8[i].split(">");
                                        String[] datavalues = datavaluesStrings[0].split(",");
                                        if (dbh.CheckCardBulkRecordIsExists(datavalues[3], Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")), datavalues[0], datavalues[1], datavalues[2]) == false) {
                                            dbh.SaveBulkIds(datavalues[3], Integer.parseInt(datavalues[4].replaceAll("\\]|\\>", "")), datavalues[0], datavalues[1], datavalues[2], dateValue, Double.parseDouble(datavalues[6]), Integer.parseInt(datavalues[7]), Integer.parseInt(datavalues[8]));
                                        }
                                        if (i == 1) {
                                            if (statusarrayData8.length > 1) {
                                                dbh.SetBulkIdUpdatedDate(datavalues[5]);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (true) {
                            if (initialData.getCardDenominationData() != null) {
                                String dbQuery8 = initialData.getCardDenominationData();
                                String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                                String[] statusarray8 = dbQuery8String.split(",");
                                String[] statusarrayData8 = dbQuery8String.split("<");

                                for (int i = 1; i < statusarrayData8.length; i++) {
                                    String[] datavaluesStrings = statusarrayData8[i].split(">");
                                    String[] datavalues = datavaluesStrings[0].split(",");
                                    dbh.SaveCardDenominations(datavalues[1], datavalues[0], Double.parseDouble(datavalues[0]), Double.parseDouble(datavalues[0]));
                                }
                                System.out.println("* date30 "+date);
                                dbh.SetCardDenominationUpdatedDate(date);
                            }
                            if (initialData.getMerchantInventoryData() != null) {
                                String result_Value = initialData.getMerchantInventoryData();
                                if (result_Value.isEmpty() || result_Value == null || result_Value.equals("[]")) {
                                } else {
                                    String res1 = result_Value.replaceAll("\\[|\\]", "");
                                    String[] ByComma = res1.split(",");
                                    String responeValueString = ByComma[0];

                                    for (int i = 0; i < ByComma.length; i++) {
                                        String AllBlockData = ByComma[i].replaceAll("\\<|\\>", "");
                                        String[] dataElements = AllBlockData.split("#");

                                        if (dataElements[0].length() != 1) {
                                            String merchantid = dataElements[0].trim();
                                            String cardType = dataElements[1];
                                            String Denomination = dataElements[2];
                                            String StocksInha = dataElements[3];
                                            String rolevel = dataElements[4];
                                            String date1 = dataElements[5];
                                            String activationCount = dataElements[6];
                                            String saleCount = dataElements[7];
                                            if (i == 1) {
                                                System.out.println("* date31 "+date);
                                                dbh.SetMerchantInventorydatedDate(date1);
                                            }
                                            dbh.SaveMerchantInventoryTableAtLogin(Long.parseLong(merchantid), cardType, Integer.parseInt(Denomination), Integer.parseInt(StocksInha), date1, Integer.parseInt(rolevel), Integer.parseInt(activationCount), Integer.parseInt(saleCount));
                                        }
                                    }
                                }
                            }

                            if (!initialData.getNextSerialData().equals("[]")) {
                                String dbQuery = initialData.getNextSerialData();
                                Log.d("&&&&&&&&&& ", dbQuery);
                                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                String[] statusarray61 = dbQueryString.split(",");
                                String[] statusarrayData61 = dbQueryString.split("<");
                                for (int i = 1; i < statusarrayData61.length; i++) {
                                    String[] datavaluesStrings = statusarrayData61[i].split(">");
                                    String[] datavalues = datavaluesStrings[0].split(",");
                                    dbh.SaveSerials(Integer.valueOf(datavalues[0]), datavalues[1], Integer.valueOf(datavalues[2]), Integer.valueOf(datavalues[3]), Integer.valueOf(datavalues[4]), Integer.valueOf(datavalues[5]), Integer.valueOf(datavalues[6]), Integer.valueOf(datavalues[7]));
                                }
                            }

                            if (true) {
                                String dbQuery = initialData.getServerDate();
                                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                                String[] statusarrayData61 = dbQueryString.split("<");

                                for (int i = 1; i < statusarrayData61.length; i++) {

                                    String[] datavaluesStrings = statusarrayData61[i].split(">");
                                    String[] datavalues = datavaluesStrings[0].split(",");
                                    String serverDateStr = datavalues[0];
                                    //String server_time = serverDateStr.replace(".0", "");
                                    // SimpleDateFormat
                                    // formatter= new
                                    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    // Date serverDate = new
                                    // Date(formatter.format(serverDateStr));//
                                    // formatter.parse(serverDateStr);
                                    dbh.SaveServerDate(serverDateStr);// serverDate.toString());//new
                                    // Date()
                                }
                            }

                            String dateString = dbh.getServerDate();
                            System.out.println("* dateString n 1 "+dateString);
                            Date date1 = null;
                            try {
                                 date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Date dateToday = new Date();
                            Log.d("dates", "Server date : " + date1 + " Phone Date : " + dateToday);
                            // System.out.println(output);

                            DateTime phoneDate = new DateTime(dateToday);
                            DateTime serverDate = new DateTime(date1);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
                            Toast toast = Toast.makeText(LoginPage.this, "Initial data synched! Version : " + version + " ", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                            Log.d("Come to the my m : ", "My method 001 ");
                            new updateSync().execute("");
//                                                for (int i = 0; i < 100; i++) {
//                                                    for (int j = 0; j < 100; j++) {
//                                                        System.out.println(" if " + i + j);
//                                                    }
//                                                }
                            //	UpdateOrCheckTSRSystemprofile();

                            Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                            int diffN = Math.abs(diff);
                            if ((diffN < 30)) {
//													Intent intent = new Intent(LoginPage.this, SelectorActivity.class);
//													startActivity(intent);
//													finish();
                                if (CONSTANTS.IS_USING_ORGINAL_URL) {
                                    startHomeActivity();
                                } else {
                                    (new SystemProfileAsyc(context)).execute("");
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
                    }

                }

            }

            dbh.UpdateLoginData(result.toString(), userName, password);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
