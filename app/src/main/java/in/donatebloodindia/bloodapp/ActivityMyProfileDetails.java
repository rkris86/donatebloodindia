package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityMyProfileDetails extends Activity
{

    String SessionId;
    public static final String TAG = "ActivityMyProfileDetails";
    ProgressDialog pd;
    Context contxt;
    public String result_output; // store output of onPostExecute function
    protected void onCreate( Bundle savedInstanceState)
    {

        result_output="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile_details_listview);
        SessionId = getIntent().getExtras().getString("sessionid");
        contxt = this;

        String url_string  = "http://donatebloodindia.in/api/getReports.php?SessionId="+SessionId;
        Log.d("Myprofile", "url_string " + url_string);
        setTitle("Blood Donation Details");

        // Find out if there are any results, if yes in post execute display the results
        new getMyDetails().execute(url_string);

    }

    private  class getMyDetails extends AsyncTask<String, Void, String>
    {

        String[] array_Id = null ,  bloodbank = null,  city = null ,  location = null, date = null , platelets=null , hemoglobin =null;
        protected void onPreExecute()
        {
            pd = new ProgressDialog(contxt);
            pd.setMessage("Getting details");
            pd.setCancelable(true);
            pd.show();
        }

        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            try
            {
                char buffer[] = new char[1500] ;
                URL url =  new URL(params[0]);
                InputStream input_stream = null;

                Log.d("Myprofile", "searchin for details");
                HttpURLConnection connect =  (HttpURLConnection) url.openConnection();
                connect.connect();
                input_stream = connect.getInputStream();
                Reader rd = new InputStreamReader(input_stream,"UTF-8");
                rd.read(buffer);
                return String.valueOf(buffer).trim();

            }
            catch (Exception e)
            {
                Log.d("Exceptioin MyProfileDetails", "Exceptioin MyProfileDetails");
                e.printStackTrace();
            }
            return null;
        }
        protected  void onPostExecute(String json_response)
        {

            pd.dismiss();
            result_output=json_response;

            //Check character limit  the json_respnse string
            if(json_response.length()<= 60 || ( json_response.contains("failed") && json_response.contains("status")))
            {

                AlertDialog.Builder ad_dialog = new AlertDialog.Builder(contxt);
                ad_dialog.setMessage("We have yet to recieve your blood donation details, try next time");
                ad_dialog.setPositiveButton("Ok", new AlertDialog.OnClickListener()
                {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        // On pressing ok, Earlier screen is called
                        finish();
                    }
                });
                ad_dialog.show();
            }
            else
            {

                Log.d("Myprofiledetails", "onPostExecute");
                //Read JSON response
                try
                {
                    JSONObject json_obj1 = new JSONObject(json_response);
                    JSONArray json_arr1 = new JSONArray(json_obj1.getString("Reports"));
                    JSONObject json_obj2;

                    array_Id = new String[json_arr1.length()];
                    bloodbank = new String[json_arr1.length()];
                    city = new String[json_arr1.length()];
                    location = new String[json_arr1.length()];
                    date = new String[json_arr1.length()];
                    platelets = new String[json_arr1.length()];
                    hemoglobin = new String[json_arr1.length()];

                    for( int t=0; t< json_arr1.length(); t++)
                    {
                        json_obj2 = json_arr1.getJSONObject(t);
                        array_Id[t]= json_obj2.getString("Id");
                        bloodbank[t]= json_obj2.getString("BloodBank / Hospital");
                        city[t] =  json_obj2.getString("City");
                        location[t] = json_obj2.getString("Location");
                        date[t] = json_obj2.getString("Date");
                        platelets[t]= json_obj2.getString("Platelets");
                        hemoglobin[t]= json_obj2.getString("Hemoglobin");

                        Log.d("city_myprofile", city[t]);
                        Log.d("location_myprofile", location[t]);

                    }

                }
                catch ( Exception e)
                {
                    Log.d("Exception in Mydetail page", "Exception in Mydetai page");
                    e.printStackTrace();
                }

                ListView lv = (ListView) findViewById(R.id.activity_myprofile_details_listview_id);

                //LayoutInflater li = getLayoutInflater()
                //lv.addHeaderView(v);

                MyProfileListViewDisplay  myprofile_adapter = new MyProfileListViewDisplay(getApplicationContext(),bloodbank,city,location,date, platelets, hemoglobin);
                lv.setAdapter(myprofile_adapter);

            }// END OF ELSE STATEMENT

        }
    }
}
