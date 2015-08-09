package in.donatebloodindia.bloodapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// This class used to send Registration id to back to server.

public class Recievenotifications extends Activity
{
    private Context con_text;
    public String appversion;
    public String gcmid;
    public ProgressDialog pd ;
    public final static String SENDER_ID= "734457567020";
    public String regid;
    public final static String TAG= "Recievenotifications";
    protected void onCreate ( Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Enabling Notifications on your account");
        pd.show();

        regid="";
        con_text=getApplicationContext();

        //checking GooglePlay is installed or not
        checkPlayServices();

        //Finding out the current app version, if app version is different, gcm needs to be generated again
        appversion = getAppVersion(con_text);

        // Find if there is any GCM Registered id already present in the phone using shared preferences

        SharedPreferences shared_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
        if(shared_pref.getString("gcm_registrationid", "").isEmpty())
        {
            // GCM Is not registered or empty.
            Log.d("Message: " , "GCM Not registered");
            // Generate new GCM ID
            generateGCMid();
        }
        else
        {
            // GCM Id is present.
            // In future. This needs relook . Registration of GCM id is compulsory

            generateGCMid();
//            Log.d("gcmstring", shared_pref.getString("gcm_registrationid", "IssueinGCMRegistration"));
//
//            // Check whether the current App version is the latest version.
//            String AppCurrentVersion =shared_pref.getString("AppCurrentVersion", "");
//            if(AppCurrentVersion.equalsIgnoreCase(getAppVersion(this.con_text)))
//            {
//                // GCM is fine.. No GCM need not be updated
//                Log.d("Message: " , "GCM is fine..no gcm is updated");
//                pd.dismiss();
//            }
//            else
//            {
//                // Since the App version is different, new registration id needs to be generated
//                generateGCMid();
//            }

        }

    }

    public boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    public String getAppVersion(Context c)
    {
        PackageInfo pm;
        try {
            pm = c.getPackageManager().getPackageInfo(c.getPackageName(),0);
            return pm.versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateGCMid()
    {

        // Generate & store GCM server in BloodApp database
        // Connect to the Blood App server using PHP

        SharedPreferences shared_preferences = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
        String phone    = shared_preferences.getString("userPhone", "");
        String password = shared_preferences.getString("userPassword", "");
        String username = shared_preferences.getString("userName", "");

        Log.d("notification message_shared preference", phone +" "+ password+ " "+ username);

        String param[] = {"1.0",username,regid, phone};

        //sending the GCM registration id
        // store the output in shared preferences and in the database
        new runInBackground().execute(param);

    }

    private class runInBackground extends AsyncTask<String, Void , String>
    {

        protected String doInBackground(String... params)
        {

            GoogleCloudMessaging gcm =null;
            Log.d("about to do some serious" , params[1]);
            if (gcm == null)
            {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            try
            {
                regid = gcm.register(SENDER_ID);

            } catch (Exception e)
            {
                e.printStackTrace();
            }


            // updating the gcm id in to the database
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(4);
            nvp.add(new BasicNameValuePair("appversion", params[0]));
            nvp.add(new BasicNameValuePair("username", params[1]));
            nvp.add(new BasicNameValuePair("DeviceId", regid));
            nvp.add(new BasicNameValuePair("Phone", params[3]));


            Log.d(TAG ,params[0] + "  " +params[1] + " " +  regid + " "+params[3]  );

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.donatebloodindia.in/api/update_deviceid.php");
            HttpResponse response;
            String s="", res="";
            try
            {
                httppost.setEntity(new UrlEncodedFormEntity(nvp));
                response =  httpclient.execute(httppost);
                InputStream is = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                while ((s = buffer.readLine()) != null)
                {
                    res += s;
                }
                Log.d("notification", res);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            // storing GCM id in the shared preferences of local file system
            SharedPreferences shrd_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd_pref.edit();
            editor.putString("gcm_registrationid", regid);
            editor.putString("AppCurrentVersion",  params[0]);
            editor.commit();
            return res;

        }

        protected void  onPostExecute(String result)
        {
            //Store GCM Id in the shared preferences
            // Encode Json of result from doInBackground function
            json_response(result);

        }
        private void json_response( String result1)
        {
            try
            {
                JSONObject json_obj = new JSONObject(result1);
                JSONObject json_obj1 = json_obj.getJSONObject("DeviceId Update");

                String final_result = json_obj1.getString("status");
                Log.d("finalresult",final_result);
                if(final_result== null || final_result.isEmpty() )
                {
                    Toast.makeText(con_text, "There seems some issue with server", Toast.LENGTH_LONG).show();
                }
                else if( final_result.equalsIgnoreCase("failure"))
                {
                    Toast.makeText(con_text, "Thank you for registering. You will receive notifications " , Toast.LENGTH_LONG).show();
                }
                else if( final_result.equalsIgnoreCase("success"))
                {
                    Toast.makeText(con_text, "Now you will receive notifications on this Smartphone " , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(con_text, "Oops there seems to be some datbase issue, please update the App" , Toast.LENGTH_LONG).show();
                }
                finish();


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } // end of json_response function
    }// endo of runinbackground asynctask functon




}
