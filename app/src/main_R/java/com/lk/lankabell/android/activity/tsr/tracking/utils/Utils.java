package com.lk.lankabell.android.activity.tsr.tracking.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lk.lankabell.android.activity.tsr.models.SharedPrefManager;
import com.lk.lankabell.android.activity.tsr.sqlite.DatabaseHandler;
import com.lk.lankabell.android.activity.tsr.sqlite.User;
import com.lk.lankabell.android.activity.tsr.util.CONSTANTS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

public class Utils implements ActivityCompat.OnRequestPermissionsResultCallback {


	public static String getSimSerialNumber = "";
	public static TelephonyManager mTelephonyMgr;
//	public final int GET_SIM_SERIAl_PERMISSSION_REQUEST_CODE = 200;

	public static boolean isNullOrEmpty(String string) {
		// TODO Auto-generated method stub
		return string.equalsIgnoreCase("");
	}

	public static void ShowMsg(Context context, int resourceId) {
		ShowMsg(context, context.getResources().getString(resourceId));
	}

	public static void ShowMsg(Context context, String string) {
		// TODO Auto-generated method stub
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}

	public static LayoutAnimationController getListViewAnimationController() {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		return controller;
	}

	public static void DebugLog(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void ErrorLog(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static String getPhoneMobileNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);


		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.

		}
		String number = tm.getLine1Number();
		return number;
	}

	public static int getCurrentAppVersionCode(Context context) {

		int version = 0;
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;

	}

	public static String getCurrentAppVersionName(Context context) {
		String version = "";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	public static boolean isGPSEnabled(Context context) {
		try {
			LocationManager locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			return locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static void writeLoactionSyncReport(String msg) {
		try {
			File newFolder = new File(
					Environment.getExternalStorageDirectory(), "LankaBellSales");
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				File file = new File(newFolder, String.format(
						"%s_loc_sync.txt", DateTimeFormatings.getDateForLogs()));

				FileWriter fw = new FileWriter(file, true);
				fw.append(msg);
				fw.flush();
				fw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
	}

	public static void writeToLogFileWithTime(String msg) {
		writeToLogFile(String.format("at : %s ---- %s",
				DateTimeFormatings.getDateTimeSec(new Date()), msg));
	}

	public static void writeToLogFile(String msg) {
		createOrUpdateFile("logs", msg);
	}

	public static void writeToLocationLogFile(String msg) {
		createOrUpdateFile("locs", msg);
	}

	public static void createOrUpdateFile(String file_name, String msg) {
		try {
			File newFolder = new File(
					Environment.getExternalStorageDirectory(), "LankaBellSales");

			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				msg += "\n";

				File file = new File(newFolder, String.format("%s_%s.txt",
						DateTimeFormatings.getDateForLogs(), file_name));
				FileWriter fw = new FileWriter(file, true);
				fw.append(msg);
				fw.flush();
				fw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
	}

	public static String getFilesNames(String file_name) {
		try {
			File newFolder = new File(
					Environment.getExternalStorageDirectory(), "LankaBellSales");
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				File file = new File(newFolder, String.format("%s_%s.txt",
						DateTimeFormatings.getDateForLogs(), file_name));
				return file.getPath();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}

		return "";
	}

	public static String getErrFileName() {
		return getFilesNames("err");
	}

	public static String getSimSerialNumber(Context context) {

		String simserialNumber = "";
		if(!CONSTANTS.FinalSimSerialNumber.equals("")){
			simserialNumber = CONSTANTS.FinalSimSerialNumber;
		}else {
			//check db
			DatabaseHandler dbh = new DatabaseHandler(context);
			User user = dbh.getUserDetails();
			if(user!= null){
				simserialNumber = user.getmobileNo();
			}else{
				//else get from android
				simserialNumber = getSerialNumbers(context).size()>0 ? getSerialNumbers(context).get(0):"";
			}

		}
		System.out.println(" * Final Sim serial "+simserialNumber);
		return simserialNumber;
	}

	//	@SuppressLint("HardwareIds")
//	public static String getSimSerialNumber(Context context) {
//		//return "89940102166881325622"; //live 4107 / 9127426
//	//	return "8994029702903652299F"; //live 1039
//
//		mTelephonyMgr = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//
//
//		// String serial = "";
//		// serial = "8994034060115581886f";
//		// serial = "8994038240713654220f";
//		// serial = "8994038240713654440f";
//		// serial = "8994038240713654330";
//		int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
//		if (Build.VERSION.SDK_INT >= 23) {
//			if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//				ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 200);
//			} else {
//				//TODO
//				getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
//
//			}
//		} else {
//			getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
//
//		}
//
//
////2453 94
//		//   getSimSerialNumber = "89940102166881325622"; //131796
//		//  getSimSerialNumber = "89940102166881325624"; //3267
//		// if (getSimSerialNumber == null) {
//		//getSimSerialNumber = "8994035020514027062"; //3361
//		// getSimSerialNumber = "8994038240713654220 ";
////         getSimSerialNumber = "8994038030314143073f"; //Upul
//		//getSimSerialNumber = "8994034061116853811f"; //1699
////         getSimSerialNumber = "8994035150914853617f"; //Motier 104336
////        getSimSerialNumber = "8994038030314889808"; //Motier 104336
//		//getSimSerialNumber = "8994038240713654220f";// QA phone
//		//getSimSerialNumber = "8994038240713654238f";// 1039 phone
//		//	 getSimSerialNumber = "8994034060115581886f";//username=a pw=a
////		getSimSerialNumber = "8994038240713656233f";// WICK phone
//		// String getSimSerialNumber = "8994035260310788463f";//niroshanR
//		// }
//		// if (getSimSerialNumber.trim().equals("")) {
//		// getSimSerialNumber = serial;
//		// return serial;
//		// }
//		// // String getSimNumber = mTelephonyMgr.getLine1Number();
//		// if (getSimSerialNumber.contains("89940101113304949761")) {
////		 getSimSerialNumber = "8994034060115581902f";
//		// }
//
//		// getSimSerialNumber = "8994038240713654440"; // 2154
//		// getSimSerialNumber = "8994038240713654220f";
//		// chathura
//		// getSimSerialNumber = "8994034060115581886f";
//		// Wickrama
//		// getSimSerialNumber = "8994038240713656233f";
//		// david
//		// getSimSerialNumber = "8994038240713655953";
//
//		// if(!getSimSerialNumber.contains("f")){
//		// getSimSerialNumber += "f";
//		// }
//		// Kalpia - 1461
//		// getSimSerialNumber = "8994035230212657074";
//		// Dasanayake - 2474
//		// getSimSerialNumber = "8994038240713652737";
//		// Vidura - 1209
//		// getSimSerialNumber = "8994034060115582223f";
//		// Janaka - 1500
//		// getSimSerialNumber = "8994034060115582231f";
//		// Karunaraja - 4107
//		// getSimSerialNumber = "8994035230212657231";
//
//		// kumara - 3699
//		// getSimSerialNumber = "8994038240713655896";
//		// mangula - 9125206
//		// getSimSerialNumber = "8994034060115582090f";
//		// Karunaraja - 4107
//		// getSimSerialNumber = "8994035230212657231";
//		// Rohan Predip - 76
//		// getSimSerialNumber = "8994034060115581951f";
//		// Rohan Predip - 68
//		// getSimSerialNumber = "8994034060115581969f";
//		// - 9119499
//		// getSimSerialNumber = "8994034011014955986f";
//		// - 124358
//		// getSimSerialNumber = "8994038240713654212f";
//		// - 327
//		// getSimSerialNumber = "8994034011014955960f";
//		// - 2026
//		// getSimSerialNumber = "8994034060115581647f";
//		// getSimSerialNumber = "8994034060115582181f";
//		// getSimSerialNumber = "8994034060115581530f";
//		// - 92092
//		// getSimSerialNumber = "8994034060115582199f";
//		// - 2482
//		// getSimSerialNumber = "8994034060115581555f";
//		// kumara - 9121052
//		// getSimSerialNumber = "8994034060115581712f";
//
////        if(getSimSerialNumber == null){
////            getSimSerialNumber = "";
////        }
//		return getSimSerialNumber;
//	}
	public static ArrayList<String> getSerialNumbers(Context context) {
		SubscriptionManager manager = null;
		ArrayList<String> SerialNumbers = new ArrayList<>();


		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);


		if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

			try {

				if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
				}
				manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
				//assert manager != null;
				if (manager != null) {
					System.out.println("* manager not null");
					List<SubscriptionInfo> infoList = manager.getActiveSubscriptionInfoList();
					System.out.println("* info list " + infoList.size());

					for (int i = 0; i < infoList.size(); i++) {

						SubscriptionInfo info = infoList.get(i);
						// int simSlotIndex = info.getSimSlotIndex();
						String serialNumber = info.getIccId();
						/*telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY*/

						SerialNumbers.add(serialNumber);
					//	System.out.println("* serial number : " + serialNumber);

					}
					//SerialNumbers.clear();
					//	SerialNumbers.add("8994034060115581639f");
					//SerialNumbers.add("8994084111800545576"); //4107
               /* if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                } else {
                    //TODO
                    getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
                    RunDemo(context);
                }*/
				} else {
					System.out.println("* manager is null");
					//   getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
					//  SerialNumbers.add(getSimSerialNumber);
				}
			} catch (Exception e) {
				e.printStackTrace();
//                getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
//                SerialNumbers.add(getSimSerialNumber);
			}
		} else {


			getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
			SerialNumbers.add(getSimSerialNumber);
//
//			SerialNumbers.clear();
//		//	SerialNumbers.add("8994034060115581639f");
//			SerialNumbers.add("8994084111800545576"); //4107

			// SerialNumbers.add("8994029702649458050"); //131796

			//   SerialNumbers.add("8994036031116012502"); //129141 LIVE USER


//                         "8994035110313941827F"); //115 live
//                         "8994029702958154753f"); //9127426
//                          "8994084111800545576f"); //1039 Test
//                          "8994036031116012658F"); //1039
//                          "8994084111800545576");
//                          "8994035110313941819f");//246
//                          "8994084111800545576"); //319
//                        "8994084111800545576");//test use 131796
		}
//		ArrayList<String> SerialNumbers = new ArrayList<>();
//		SerialNumbers.add("8994084111800545576"); //4107
		return SerialNumbers;
	}

	public static boolean isInternetAvailable() {
		try {
			InetAddress ipAddr = InetAddress.getByName("google.com"); // You can
			// replace
			// it
			// with
			// your
			// name

			if (ipAddr.equals("")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}

	}

	public static void writeToErrLogFileWithTime(Exception e) {
		try {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			createOrUpdateFile("err", errors.toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static boolean isTrackingOn(Context context) {
		int sec = SharedPrefManager.getGPSUpdateTime(context);
		if (sec < 1) {
			return false;
		} else {
			DatabaseHandler dbTSR = new DatabaseHandler(context);
			return dbTSR.isAttendanceInMarked(null);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 200:
				System.out.println("*onRequestPermissionsResult");
				if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

					try{
					getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
		}
	}

	// public static boolean isUrlValied(Context context, String uRL) {
	// try {
	// URL url = new URL(uRL);
	// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	// int code = connection.getResponseCode();
	//
	// if (code == 200) {
	// return true;
	// } else {
	// ShowMsg(context, "Server is offline.");
	// }
	//
	//
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// ShowMsg(context, "Server is offline. Error in url");
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// ShowMsg(context, "Server is offline. Can not connect");
	// e.printStackTrace();
	// }
	//
	// return false;
	//
	// }

}
