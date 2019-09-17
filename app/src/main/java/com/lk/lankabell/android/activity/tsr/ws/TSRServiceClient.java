package com.lk.lankabell.android.activity.tsr.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;



public class TSRServiceClient {

	private final String NAMESPACE = "http://tsr.lankabell.lk.com";
    private final String URL = "http://axis2.lankabellnet.com/services/TSRService?wsdl";
    private final String SOAP_ACTION = "http://axis2.lankabellnet.com/TSRService";
	//private final String SOAP_ACTION = "http://localhost:8080/TSR_WebServices/";
    //private final String URL = "http://localhost:8080/TSR_WebServices/services/TSR_WEB_SERVICE?wsdl";
    private static final String TAG = TSRServiceClient.class.getSimpleName();
    
    public boolean saveGeoInfo(int empId,int geoId,String latitude,String longitude){
  
    	 boolean response = false;
    	 
    	 SoapObject request = new SoapObject(NAMESPACE, "saveGeoInfo");
    	 
    	 PropertyInfo empIdProp = new PropertyInfo();
         empIdProp.setName("empId");
         empIdProp.setValue(empId);
         empIdProp.setType(int.class);
         request.addProperty(empIdProp);
         
         PropertyInfo geoIdProp = new PropertyInfo();
         geoIdProp.setName("geoId");
         geoIdProp.setValue(geoId);
         geoIdProp.setType(int.class);
         request.addProperty(geoIdProp);
         
         PropertyInfo grabTimeProp = new PropertyInfo();
         grabTimeProp.setName("grabTime");
         grabTimeProp.setValue("");
         grabTimeProp.setType(String.class);
         request.addProperty(grabTimeProp);
         
         PropertyInfo latitudeProp = new PropertyInfo();
         latitudeProp.setName("latitude");
         latitudeProp.setValue(latitude);
         latitudeProp.setType(String.class);
         request.addProperty(latitudeProp);
         
         PropertyInfo longitudeProp = new PropertyInfo();
         longitudeProp.setName("longitude");
         longitudeProp.setValue(longitude);
         longitudeProp.setType(String.class);
         request.addProperty(longitudeProp);
         
      //   String myProxy=android.net.Proxy.getDefaultHost() ;
      //   int myPort=android.net.Proxy.getDefaultPort();

       //  Log.i(TAG,"Proxy="+ myProxy+":"+myPort);
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myProxy, myPort));
         
         
         ///BELOW LINES WORKS BEFORE
         /*if(myProxy!=null){
             Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myProxy, myPort));
             connection = (HttpURLConnection) new URL(url).openConnection(proxy);
         }
         else
         {
             connection = (HttpURLConnection) new URL(url).openConnection();
         }*/
        
         
         
         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
         envelope.setOutputSoapObject(request);
         HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        // HttpTransportSE androidHttpTransport = new HttpTransportSE(proxy,URL);
        
         try {
        	 
              androidHttpTransport.call(SOAP_ACTION, envelope);
              SoapPrimitive soapResponse = (SoapPrimitive) envelope.getResponse();
              response = Boolean.parseBoolean(soapResponse.toString());
              
              Log.i(TAG,"saveGeoInfo="+ soapResponse.toString()+response);
              
              return response;
              
               
           } catch (Exception e) {
        	   response = false;
        	   Log.i(TAG,"saveGeoInfo Error="+ e.getLocalizedMessage());
           }
         
    	return response;
    }
    
    public boolean saveLocation(int locId,String location,int geoId){
    	  
   	 boolean response = false;
   	 
   	 SoapObject request = new SoapObject(NAMESPACE, "saveLocation");
   	 
   	 	PropertyInfo empIdProp = new PropertyInfo();
        empIdProp.setName("locId");
        empIdProp.setValue(locId);
        empIdProp.setType(int.class);
        request.addProperty(empIdProp);
        
        PropertyInfo geoIdProp = new PropertyInfo();
        geoIdProp.setName("location");
        geoIdProp.setValue(location);
        geoIdProp.setType(String.class);
        request.addProperty(geoIdProp);
        
        PropertyInfo grabTimeProp = new PropertyInfo();
        grabTimeProp.setName("geoId");
        grabTimeProp.setValue(geoId);
        grabTimeProp.setType(int.class);
        request.addProperty(grabTimeProp);
        
    
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
             androidHttpTransport.call(SOAP_ACTION, envelope);
             SoapPrimitive soapResponse = (SoapPrimitive) envelope.getResponse();
             return Boolean.parseBoolean(soapResponse.toString());
             
              
          } catch (Exception e) {
       	   response = false;
          }
        
   	return response;
   }
    
    public boolean saveRemarks(int remarkId,String remarks,int locId){
  	  
      	 boolean response = false;
      	 
      	 SoapObject request = new SoapObject(NAMESPACE, "saveRemarks");
      	 
      	 PropertyInfo empIdProp = new PropertyInfo();
           empIdProp.setName("remarkId");
           empIdProp.setValue(remarkId);
           empIdProp.setType(int.class);
           request.addProperty(empIdProp);
           
           PropertyInfo geoIdProp = new PropertyInfo();
           geoIdProp.setName("remarks");
           geoIdProp.setValue(remarks);
           geoIdProp.setType(String.class);
           request.addProperty(geoIdProp);
           
           PropertyInfo grabTimeProp = new PropertyInfo();
           grabTimeProp.setName("locId");
           grabTimeProp.setValue(locId);
           grabTimeProp.setType(int.class);
           request.addProperty(grabTimeProp);
           
       
           SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           envelope.setOutputSoapObject(request);
           HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
           try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive soapResponse = (SoapPrimitive) envelope.getResponse();
                return Boolean.parseBoolean(soapResponse.toString());
                
                 
             } catch (Exception e) {
          	   response = false;
             }
           
      	return response;
      }
    
    public boolean saveSales(int salesId,long serialStart,long serialEnd,int locId){
    	  
     	 boolean response = false;
     	 
     	 SoapObject request = new SoapObject(NAMESPACE, "saveSales");
     	 
     	 PropertyInfo empIdProp = new PropertyInfo();
          empIdProp.setName("salesId");
          empIdProp.setValue(salesId);
          empIdProp.setType(int.class);
          request.addProperty(empIdProp);
          
          PropertyInfo geoIdProp = new PropertyInfo();
          geoIdProp.setName("serialStart");
          geoIdProp.setValue(serialStart);
          geoIdProp.setType(long.class);
          request.addProperty(geoIdProp);
          
          PropertyInfo grabTimeProp = new PropertyInfo();
          grabTimeProp.setName("serialEnd");
          grabTimeProp.setValue(serialEnd);
          grabTimeProp.setType(long.class);
          request.addProperty(grabTimeProp);
          
          PropertyInfo longitudeProp = new PropertyInfo();
          longitudeProp.setName("locId");
          longitudeProp.setValue(locId);
          longitudeProp.setType(int.class);
          request.addProperty(longitudeProp);
      
          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
          envelope.setOutputSoapObject(request);
          HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
          try {
               androidHttpTransport.call(SOAP_ACTION, envelope);
               SoapPrimitive soapResponse = (SoapPrimitive) envelope.getResponse();
               return Boolean.parseBoolean(soapResponse.toString());
               
                
            } catch (Exception e) {
         	   response = false;
            }
          
     	return response;
     }
    
    public String getLoginInfo(int empId,String password){
    	
    	 SoapObject request = new SoapObject(NAMESPACE, "validateLogin");
    	 
    	 PropertyInfo empIdProp = new PropertyInfo();
         empIdProp.setName("empId");
         empIdProp.setValue(empId);
         empIdProp.setType(int.class);
         request.addProperty(empIdProp);
         
         PropertyInfo geoIdProp = new PropertyInfo();
         geoIdProp.setName("password");
         geoIdProp.setValue(password);
         geoIdProp.setType(String.class);
         request.addProperty(geoIdProp);
         
         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
         envelope.setOutputSoapObject(request);
         HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
         
         
         try {
              androidHttpTransport.call(SOAP_ACTION, envelope);
              SoapPrimitive soapResponse = (SoapPrimitive) envelope.getResponse();
              return soapResponse.toString();
              
               
           } catch (Exception e) {
        	   return null;
           }
         
    
    }
    
}
