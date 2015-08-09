package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterLoaderActivity extends Activity {

    String city,sub_city,mobile,gender;
    public static String TAG = "RegisterLoaderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_loader);

        Toast.makeText(getApplicationContext(), "Registering Please Wait",Toast.LENGTH_LONG).show();

        //Receive data from login screen for registration
        String first_name  = this.getIntent().getStringExtra("first_name");
        String last_name  = this.getIntent().getStringExtra("last_name");
        mobile  = this.getIntent().getStringExtra("mobile");
        String blood_group  = this.getIntent().getStringExtra("bloodgroup");
        city  = this.getIntent().getStringExtra("city");
        sub_city  = this.getIntent().getStringExtra("sub_city");
        gender = this.getIntent().getStringExtra("gender");

        Log.d(TAG,"gender "+ gender);
        Log.d(TAG,"bloodgroup "+ blood_group);

        //convert bloodgroup in to numeric strings
        blood_group=find_bloodnumber(blood_group);

        //Toast.makeText(getApplicationContext(), sub_city,  Toast.LENGTH_LONG).show();
        //String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);



        // This function is used to identify the id's of city and sub_city
        int ids[] = find_id( city,  sub_city );
        //Log.d("RegisterAcitivity", String.valueOf(ids[0])+String.valueOf(ids[1]));


        String finalstring = "http://donatebloodindia.in/api/donor-register.php?Name=";
        finalstring = finalstring+ first_name+"&"+"Email=dummy@dummy.com&Password="+mobile+"&Phone="+mobile+"&BloodGroup="+blood_group+"&City="+ids[0]+"&DOB=1989-08-15"+"&Location="+ids[1]+"&Gender="+gender+"&DeviceId=APA91bGh1B6kl3w7imoWLpqfsnWq90HUXXhbTug3yPu_ts93IFZoxS5vSblFW5WXbC0t9zVNSQVK0J6Cf7eK55xUJFpqijOC-BNLmfU4lJfUjfr06DZSPP0ppxKhyee22r7zGqx6T5BOOMf4vUCPH-xK1Fn1yjEq7A";
        //String test_string ="http://donatebloodindia.in/api/donor-register.php?Name=tttttaa&Email=elak@oclocksolutions.com&Password=5553456&Phone=3333333336&BloodGroup=1&City=jayankondam&DOB=1989-08-15&Location=Tamilnadu&Gender=Male&DeviceId=APA91bGh1B6kl3w7imoWLpqfsnWq90HUXXhbTug3yPu_ts93IFZoxS5vSblFW5WXbC0t9zVNSQVK0J6Cf7eK55xUJFpqijOC-BNLmfU4lJfUjfr06DZSPP0ppxKhyee22r7zGqx6T5BOOMf4vUCPH-xK1Fn1yjEq7A";
        Log.d(TAG, finalstring);
        new executeHttpConnection().execute(finalstring);

    }

    private String find_bloodnumber( String bloodgroup)
    {
        if ( bloodgroup.contains("O+ve"))
             return  "1";
        else if ( bloodgroup.contains("O-ve"))
            return  "2";
        else if ( bloodgroup.contains("A+ve"))
            return  "3";
        else if ( bloodgroup.contains("A-ve"))
            return  "4";
        else if ( bloodgroup.contains("B+ve"))
            return  "5";
        else if ( bloodgroup.contains("B-ve"))
            return  "6";
        else if ( bloodgroup.contains("AB+ve"))
            return  "7";
        else if ( bloodgroup.contains("AB-ve"))
            return  "8";

        return "";
    }

    private int[] find_id(String city2, String sub_city2)
    {

        // This function is used to identify the id's of city and sub_city
        int id_return[] ={0,0};
        String cities_id[]  = {"Mumbai","Hyderabad", "Bangalore","Delhi","Chennai",};
        String subcities_id[] = {
                "South Mumbai","Western Suburbs","Central suburbs","Thane and Beyond","Navi Mumbai", "Dahisar and Beyond" ,"",
                "West Hyderabad" , "North Hyderabad"  ,"East Hyderabad" ,   "Central Hyderabad" ,  "Central Hyderabad","Outer Hyderabad",
                "South Bangalore" ,"North Bangalore", "West Bangalore" ,  "Central Bangalore",  "East Bangalore", "Outer Bangalore",
                "Central Delhi" , "East Delhi" ,     "North Delhi",  "South Delhi","New Delhi", "West Delhi" ,"Gurgaon"   ,
                "South Chennai","North Chennai","West Chennai","East Chennai","Central Chennai","Outer Chennai"
        };


        for (int i =0; i< cities_id.length; i++)
        {
            if(city2.equalsIgnoreCase(cities_id[i]))
            {	id_return[0] = ++i;
                Log.d(TAG,"Identifying City iD's "+ String.valueOf(i));
                break;
            }

        }
        //Log.d("RegisterActivity","In middle function " + subcities_id[0]);
        int k =0;
        for ( k= 0; k < subcities_id.length ; k++)
        {
            //Log.d("RegisterActivity","In second loop function " + subcities_id[0] + subcities_id.length);

            if(sub_city2.equalsIgnoreCase(String.valueOf(subcities_id[k])))
            {
                id_return[1] = ++k;
                Log.d(TAG,"Identifying SubCity iD's "+String.valueOf(k));
                break;
            }

        }
        //Log.d("RegisterActivity","In find function 444 ");
        return id_return;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_loader, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        else if(id == R.id.action_search)
        {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_register_loader, container,
                    false);
            return rootView;
        }
    }


    private class executeHttpConnection extends AsyncTask<String, Void, String>
    {
        LinearLayout  Llayout= (LinearLayout) findViewById(R.id.register_spinnerlayout);
        ProgressBar pb = new ProgressBar(getApplicationContext());

        public ProgressDialog progress1  ;

        //Establish HTTP connections in the Background
        protected String doInBackground(String... view)
        {

            Log.e("krishna","executeHttpConnection");

            String response = null;
            try {
                response = establish_connection(view[0]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Log.d(TAG , "Exception : establish connection failed");

            }
            if(response == null)
            {
                if ( response.isEmpty())
                    Log.d("krishna", "insertion failed");

                return "failure";
            }

            return response;
        }

        //Establish connection & execute registration
        private String establish_connection(String test_string) throws IOException
        {
            String response = null;
            InputStream is = null;
            Log.e("krishna", "In establish_connection");
            try {

                URL url = new URL(test_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                //get the inputstream
                is = conn.getInputStream();

                //read the inputstream
                Reader read = null;
                char[] buffer= new char[150];
                read = new InputStreamReader(is, "UTF-8");
                read.read(buffer);

                Log.d("krishna", buffer.toString().toString());
                return new String(buffer);

            }
            catch(Exception e)
            {
                System.out.print(e.getMessage());
            }
            finally
            {
                if(is!=null)
                {
                    is.close();
                }
            }
            return null;

        }

        //Establish Loader cursor icon here
        protected void onPreExecute()
        {
            Log.d("krishna","In preExecute");
            Llayout.addView(pb);
            progress1 = new ProgressDialog(getApplicationContext());
            progress1.setMessage("Registering");
            //progress1.show();

        }
        protected void onPostExecute(String view)
        {

            Log.d("krishna","In postExecute" + view.trim());
            pb.setVisibility(View.GONE);
            //progress1.dismiss();

            //Do JSOn Parsing
            JSONObject jsonobj;
            try {
                jsonobj = new JSONObject(view.trim());

                JSONObject description=  jsonobj.getJSONObject("Donor Registeration");
                String result = description.getString("status");

                if(result.trim().equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(), "You have successfully been registered", Toast.LENGTH_LONG).show();

                    // Now show all the Announcements of the city the user has registered
                    // Store his login information in the device.


                    SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("applogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor  editor = sh_pref.edit();
                    editor.putInt("applogincredentials", 1);
                    editor.putString("username", mobile);
                    editor.putString("password", mobile);
                    editor.commit();

                    // GCM Id and store in the shared preferences files
                    // only one gcm id will be store in the shared preferences
                    // if any user registers, gcm id  will be stored. if any user logs in
                    // and and gcm id different that that of loggedin then gcm id will be overwritend
                    // with gcm id of the logged in user.




                    Intent intent1 = new Intent();
                    intent1.setClass(getApplicationContext(), HomeMainActivity.class);
                    intent1.putExtra("city", city);
                    startActivity(intent1);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Opps! There seems to be some issue. Please retry!", Toast.LENGTH_LONG).show();
                }
                //Log.d("krishna", "Json"+result);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }

    }

}
