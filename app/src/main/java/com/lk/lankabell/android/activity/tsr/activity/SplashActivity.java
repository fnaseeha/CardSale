package com.lk.lankabell.android.activity.tsr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lk.lankabell.android.activity.tsr.R;
import com.lk.lankabell.android.activity.tsr.beans.InitialData;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.sync.SysncServices;
import com.lk.lankabell.android.activity.tsr.tracking.utils.Utils;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;
import com.lk.lankabell.android.activity.tsr.util.HTTPServiceCalls;
import com.lk.lankabell.android.activity.tsr.util.WsdlReader;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    String fromPackage, username, pword, SimNo, CollectorCode, attendenceIn, attendenceOut, empNo, logout;
    Intent intent;
    private DatabaseHandler dbh;
    private static String NAMESPACE = "http://mainService";
    private static String METHOD_NAME1 = "LoginAuthentication";
    InitialData initialData;
    SoapSerializationEnvelope envelope;
    int timeInterval = 4000;
    int timeInterval2 = 14000;

    boolean network_enable = false;
    SoapObject result;
    protected Dialog mSpashDialog;
    private static String SOAP_ACTION1 = "http://mainService/LoginAuthentication";
    private static String SOAP_ACTION20 = "http://mainService/loginBlock";

    private static String METHOD_NAME20 = "loginBlock";
    String URL = WsdlReader.getServiceUrl();
    String date;
    double version = 1.19;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbh = new DatabaseHandler(getApplicationContext());

        Bundle b = getIntent().getExtras();
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo("com.lk.lankabell.android.activity.tsr", 0);
            System.out.println("* pInfo.versionName "+pInfo.versionName);
            version = Double.parseDouble(pInfo.versionName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("* bundle " + b.getString("package"));
        if (b != null) {
            dbh.saveVersion(version);
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
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                String date = sdf.format(System.currentTimeMillis());
                intent = new Intent(this, LoginPage.class);
                dbh.SaveLoginDataSp(username, pword, SimNo, date);
                intent.putExtra("username", username);
                intent.putExtra("pword", pword);
                intent.putExtra("SimNo", SimNo);
                boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
                System.out.println("* firstrun "+firstrun);

                if (firstrun) {
                    network_enable = isOnline();
                    if (network_enable) {

                        initialData = new InitialData();
                        System.out.println("* initialData  tele "+SimNo+" username "+username+" password "+pword);
                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                        request.addProperty("strInputUserMobile", SimNo);
                        request.addProperty("strInputUserName", username);
                        request.addProperty("strInputUserPassword", pword);
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
                                            System.out.println("* timer 1 EXEC "+SimNo);
                                            try {
                                                getResult(SimNo,username,pword);
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
                }else{

                    startActivity(intent);
                }

            }

        } else {
            Toast.makeText(SplashActivity.this, "Error While getting username from Core App", Toast.LENGTH_LONG).show();
        }

    }

    public void getResult(final String telephone, final String userName, final String password) {
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
                new BlockSync(this, telephone,userName,password).execute("");

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
                                    getInitialData(telephone,userName,password);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    System.out.println("* userName "+userName+" password "+password+" sim "+telephone);
                                    Toast.makeText(SplashActivity.this,"Username password not match with simSerial Number",Toast.LENGTH_LONG).show();
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

    public void getInitialData(final String telephone, final String userName, final String password) throws ParseException {
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

            try{
                if (true) {
                    String dbQuery7 = initialData.getMerchantData();
                    String dbQuery7String = dbQuery7.replaceAll("\\[|\\]", "");
                    String[] statusarray7 = dbQuery7String.split(",");
                    String[] statusarrayData7 = dbQuery7String.split("<");
                    for (int i = 1; i < statusarrayData7.length; i++) {
                        String[] datavaluesStrings = statusarrayData7[i].toString().split(">");
                        String[] datavalues = datavaluesStrings[0].split("#");
                        // if(!(datavalues[4].equals("null"))
                        // &&
                        // (!datavalues[5].equals("null"))){

                        Long merchantId = Long.valueOf(datavalues[0].toString());
                        String name = datavalues[1].toString();
                        String address = datavalues[2].toString();
                        String lat = datavalues[4].toString();
                        String lon = datavalues[5].toString();
                        String registeredOn = datavalues[7].toString();
                        String mobileNo = datavalues[8].toString();
                        String city = datavalues[10].toString();
                        Integer IsActive = Integer.parseInt(datavalues[9].toString());
                        String RegisteredByEpf = datavalues[6].toString();
                        Integer isRegistered = Integer.parseInt(datavalues[3].toString());

                        Integer isAssigned = Integer.parseInt(datavalues[11].toString());
                        Integer isEdited = Integer.parseInt(datavalues[12].toString());
                        String reloadNo = datavalues[13].toString();

                        dbh.SaveMerchantDetails(merchantId, name, address, lat, lon, registeredOn, mobileNo, city, IsActive, RegisteredByEpf, isRegistered, isAssigned, isEdited, reloadNo);
                        // }
                    }
                    dbh.SetMerchantMasterUpdatedDate(date);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try{

                if (true) {
                    String dbQuery8 = initialData.getCardBulkSerialData();
                    String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                    String[] statusarray8 = dbQuery8String.split(",");
                    String[] statusarrayData8 = dbQuery8String.split("<");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
                    String dateValue = sdf1.format(System.currentTimeMillis());

                    if (!statusarray8[0].equalsIgnoreCase("")) {
                        if (statusarray8[1].length() > 1) {
                            for (int i = 1; i < statusarrayData8.length; i++) {
                                String[] datavaluesStrings = statusarrayData8[i].toString().split(">");
                                String[] datavalues = datavaluesStrings[0].split(",");
                                if (dbh.CheckCardBulkRecordIsExists(datavalues[3].toString(), Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")), datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString()) == false) {
                                    dbh.SaveBulkIds(datavalues[3].toString(), Integer.parseInt(datavalues[4].toString().replaceAll("\\]|\\>", "")), datavalues[0].toString(), datavalues[1].toString(), datavalues[2].toString(), dateValue, Double.parseDouble(datavalues[6].toString()), Integer.parseInt(datavalues[7].toString()), Integer.parseInt(datavalues[8].toString()));
                                }
                                if (i == 1) {
                                    if (statusarrayData8.length > 1) {
                                        dbh.SetBulkIdUpdatedDate(datavalues[5].toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                String dbQuery8 = initialData.getCardDenominationData();
                String dbQuery8String = dbQuery8.replaceAll("\\[|\\]", "");
                String[] statusarray8 = dbQuery8String.split(",");
                String[] statusarrayData8 = dbQuery8String.split("<");

                for (int i = 1; i < statusarrayData8.length; i++) {
                    String[] datavaluesStrings = statusarrayData8[i].toString().split(">");
                    String[] datavalues = datavaluesStrings[0].split(",");
                    dbh.SaveCardDenominations(datavalues[1].toString(), datavalues[0].toString(), Double.parseDouble(datavalues[0].toString()), Double.parseDouble(datavalues[0].toString()));
                }
                dbh.SetCardDenominationUpdatedDate(date);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                if (true) {
                    String result_Value = initialData.getMerchantInventoryData();
                    if (result_Value.isEmpty() || result_Value == null || result_Value.equals("[]")) {
                    } else {
                        String res1 = result_Value.replaceAll("\\[|\\]", "");
                        String[] ByComma = res1.split(",");
                        String responeValueString = ByComma[0].toString();

                        for (int i = 0; i < ByComma.length; i++) {
                            String AllBlockData = ByComma[i].replaceAll("\\<|\\>", "");
                            String[] dataElements = AllBlockData.split("#");

                            if (dataElements[0].length() != 1) {
                                String merchantid = dataElements[0].toString().trim();
                                String cardType = dataElements[1].toString();
                                String Denomination = dataElements[2].toString();
                                String StocksInha = dataElements[3].toString();
                                String rolevel = dataElements[4].toString();
                                String date1 = dataElements[5].toString();
                                String activationCount = dataElements[6].toString();
                                String saleCount = dataElements[7].toString();
                                if (i == 1) {
                                    dbh.SetMerchantInventorydatedDate(date1);
                                }
                                dbh.SaveMerchantInventoryTableAtLogin(Long.parseLong(merchantid), cardType, Integer.parseInt(Denomination), Integer.parseInt(StocksInha), date1, Integer.parseInt(rolevel), Integer.parseInt(activationCount), Integer.parseInt(saleCount));
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                String dbQuery = initialData.getNextSerialData();
                Log.d("&&&&&&&&&& ", dbQuery);
                String dbQueryString = dbQuery.replaceAll("\\[|\\]", "");
                String[] statusarray61 = dbQueryString.split(",");
                String[] statusarrayData61 = dbQueryString.split("<");
                for (int i = 1; i < statusarrayData61.length; i++) {
                    String[] datavaluesStrings = statusarrayData61[i].toString().split(">");
                    String[] datavalues = datavaluesStrings[0].split(",");
                    dbh.SaveSerials(Integer.valueOf(datavalues[0].toString()), datavalues[1].toString(), Integer.valueOf(datavalues[2].toString()), Integer.valueOf(datavalues[3].toString()), Integer.valueOf(datavalues[4].toString()), Integer.valueOf(datavalues[5].toString()), Integer.valueOf(datavalues[6].toString()), Integer.valueOf(datavalues[7].toString()));
                }
            }catch (Exception e){
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

        final DateTime phoneDate = new DateTime(dateToday);
        final DateTime serverDate = new DateTime(date1);

        Toast toast = Toast.makeText(this, "Initial data synced!  ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        new updateSync(telephone).execute("");
        Log.d("Come to the my m : ", "My method 001 ");
        Timer _timer  = new Timer();

        final String finalDateString = dateString;
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // use runOnUiThread(Runnable action)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("* timer 2 EXEC");
                        try {

                            FinalDecision(finalDateString, phoneDate, serverDate,telephone);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("* userName "+userName+" password "+password+" sim "+telephone);
                            Toast.makeText(SplashActivity.this,"Username password not match with simSerial Number",Toast.LENGTH_LONG).show();
                            removeSplash();
                            //remove f
                            // make simple f
                            // check
                        }
                    }
                });
            }
        }, 1000);

       /* new updateSync(telephone).execute("");
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println(" if " + i + j);
            }
        }*/
      	//UpdateOrCheckTSRSystemprofile(telephone);


    }

    public void FinalDecision(String dateString, DateTime phoneDate, DateTime serverDate, String telephone) {


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
                (new SystemProfileAsyc(this,telephone)).execute("");
            }

        } else {
            Log.d("Check System time 001 :", "1 Run : Phone Date and Time is :" + phoneDate.toString() + " | Server Date and Time is :" + serverDate.toString());
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle("Date info")
                    .setMessage("Phone Date different from Server Date - Fist login (ST- " + dateString + " )")
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    private class SystemProfileAsyc extends AsyncTask<String, Void, String> {

        private Context context;
        private HTTPServiceCalls http;
        private ProgressDialog pg;
        String sim;

        public SystemProfileAsyc(Context context,String sim) {
            super();
            this.context = context;
            http = new HTTPServiceCalls();
            this.sim = sim;
            System.out.println("* SystemProfileAsyc");
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
                String epf = dbh.GetEmpNoByPhoneNo(sim);
                System.out.println("* epf "+epf);
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

                System.out.println("* status "+status);
                if (status == 1) {
                    String url = (String) jResponse.get("data");
                    if (!url.trim().equals("")) {
                        if (dbh.updateServiceUrl(url)) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
                            startHomeActivity();
                        }else{
                            startLoginActivity();
                        }

                    }else{
                        startLoginActivity();
                    }
                }else{
                    startLoginActivity();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.writeToErrLogFileWithTime(e);
            } finally {
                dbh.close();
            }
        }

    }

    private void startLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    public void startHomeActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(SplashActivity.this, SelectorActivity.class);
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

    private void UpdateOrCheckTSRSystemprofile(String sim) {
        User user = dbh.getUserDetails();
        String epfNo = dbh.GetEmpNoByPhoneNo(sim);
        Log.d("Come to the", "My method 00222 ");
        new SysncServices().TSRSystemprofile(getApplicationContext(), user.getmobileNo(), user.getUserName(), user.getPassword(), epfNo, "LoginInishial");
    }

    private class BlockSync extends AsyncTask<String, String, String> {
        private Context context;
        private String simSerial;
        private String userName;
        private String password;

        public BlockSync(Context context, String simSerial, String userName, String password ) {
            this.context = context;
            this.simSerial = simSerial;
            this.userName = userName;
            this.password = password;
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

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
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
        }, timeInterval2);
    }
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
}
