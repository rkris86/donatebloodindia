package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HomeMainActivity extends Activity {

    // result_json . In this variable, the JSON output is stored
    public String result_json ;
    public static final String TAG="HomeMainActivity";
    public static String  connectionresponsestring ;
    // used to store session id - comes from login API response
    public String sessionid ;

    // Store the text and description . This below variables will be used in
    //JSON Parser jsonparse_list() of this class

    public String[] title_list_results;
    public String[]  description_list_results;
    public String[] annoucement_id;
    public int list_item_count;
    public ProgressDialog progress;
    private boolean backpressedagain=false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        connectionresponsestring="default"; // this string used to find out issues in network connecion ,
                                            // set to "default"

        setTitle("Blood App");

        //boolean used to check whether back button is pressed or not
        backpressedagain=false;
        progress = new ProgressDialog(this);

        // Getting the city the user has selected and showing the announcements that are made in that city
        // SessionId will used to get the MyProfile details of the customer
        String city = getIntent().getExtras().getString("city");
        this.sessionid = getIntent().getExtras().getString("SessionId");

        // Set color to the background color or else default color i.e black will be taken
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb( 247, 36, 36)));

        // Find out all the announcements of that city
        announcements_city(city);

    }
    public void announcements_city(String city)
    {
        // execute the Asynctask object here.
        // For announcement info , we need to call Announcements API which will be executed in AsyncTask Class
        String url_str = "http://donatebloodindia.in/api/announcementbycity.php?city="+city;
        Log.d(TAG, "HomeMainActivity - URL strname"+ url_str);

        new executeHttpConnection_home().execute(url_str);

    }

    // This jsonparse_list() is called to extract data from JSON response of Annoucement API
    public void  jsonparse_list() throws Exception
    {

        this.list_item_count = 0;  // used to count items in the JSON response
        Log.d(TAG, "jsonparse_Announcements");

        JSONObject json_obj = new JSONObject(result_json);
        JSONObject json_fchild = json_obj.getJSONObject("Announcements");
        JSONArray json_array = json_fchild.getJSONArray("Immediate");

        Log.d(TAG, String.valueOf(json_array.length()));

        // Declaring the size of String[] where we will store parsing data
        title_list_results = new String[json_array.length()];
        description_list_results = new String[json_array.length()];
        annoucement_id = new String[json_array.length()];

        JSONObject json_obj1 =null;
        for(int i = 0; i< json_array.length(); i++ )
        {

            json_obj1 =json_array.getJSONObject(i);
            Log.d(TAG, json_obj1.getString("Name"));
            // Add every title to titl_list_results

            title_list_results[this.list_item_count] = json_obj1.getString("Name");
            Log.d(TAG, title_list_results[this.list_item_count]);

            // Add every description  to description_list_results
            description_list_results[this.list_item_count] = json_obj1.getString("Announcement");

            // Add annoucement id to annoucement_id . This is used when user click any item
            annoucement_id[this.list_item_count] = json_obj1.getString("Id");

            //Log.d("HomeMainActivity", json_obj1.getString("City"));
            //Log.d("HomeMainActivity", json_obj1.getString("Location"));
            //Log.d("HomeMainActivity", json_obj1.getString("Date"));

            // Increment the list_item_count
            this.list_item_count++;
        }

    }

    // This class is used to establish network connection
    private class executeHttpConnection_home extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
            Log.d(TAG,"In preExecute");
            progress.setMessage("Loading Messages");
            progress.show();
            //Llayout.addView(pb);
        }
        protected String doInBackground(String... params)
        {
            String temp=null;
            try
            {
                //Passing URL in params[0]
                temp = establish_connection(params[0]);
                Log.d(TAG, "After assign in doing background" + temp);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "Exception in executeHttpConnection AsyncTask");

            }
            if( temp!=null)
                result_json = temp.trim();

            return result_json;
        }

        // function to create connection and get jsonstring in response
        private String establish_connection(String test_string) throws Exception
        {
            String response = null;
            InputStream is = null;
            //Log.e("HomeMainActivity", "In establish_connection");
            try
            {
                URL url = new URL(test_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(7000);
                conn.connect();

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    //get the inputstream
                    is = conn.getInputStream();
                    //read the inputstream
                    Reader read = null;
                    char[] buffer= new char[5000];
                    read = new InputStreamReader(is, "UTF-8");
                    read.read(buffer);
                    //Log.e("HomeMainActivity", "Connect success full establish_connection");
                    Log.d(TAG, String.valueOf(buffer).trim());
                    return String.valueOf(buffer).trim();

                }
            }
            catch( SocketTimeoutException ste)
            {
                Log.d(TAG, "Exception thrown in HttpURLConnection");
                ste.printStackTrace();
                connectionresponsestring="exception";
            }
            catch(Exception e)
            {
                //System.out.print(e.getMessage());
                Log.d(TAG, "Exception thrown in HttpURLConnection");
                e.printStackTrace();
                connectionresponsestring="exception";

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


        protected void onPostExecute(String  result)
        {
            progress.dismiss();
            
            if(connectionresponsestring.contains("exception"))
            {
                Toast.makeText(HomeMainActivity.this, "Oops there seems to be some issue, try again", Toast.LENGTH_LONG).show();
                return;
            }
            Log.d(TAG,"In postExecute");
            try
            {
                //Post execution, call Parsing of JSON
                jsonparse_list();

            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace()
                Log.d("HomeMainActivity", "Exception in postexecute");

            }

            ListView l = (ListView) findViewById(R.id.listView1);

            HomeListAnnoucements  hl_adapter = new HomeListAnnoucements( getApplicationContext() ,title_list_results, description_list_results,annoucement_id ) ;
            //ArrayAdapter<String> array_adapt = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_home_list, R.id.title_message, data);
            l.setAdapter(hl_adapter);
            l.setItemsCanFocus(false);

            l.setOnItemClickListener(new OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    // TODO Auto-generated method stub
                    SharedPreferences shared_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                    String session_string = shared_pref.getString("userSessionId", "");
                    Log.d(TAG,"Homemainactivity: Onitemclick of listview : sessionid "+session_string);

                }
            });
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_loader, menu);
        return true;

    }

    // For menu related code
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            // Show settings screen
            startActivity(new Intent(this, SettingsActivity.class));

        }
        else if( id == R.id.action_logout)
        {
            // Logout action
            Toast.makeText(getApplicationContext(), "Logout is in progress", Toast.LENGTH_LONG).show();
            Intent intent_logout = new Intent(this, ActivityFirstLogin.class);
            startActivity(intent_logout);

            SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("applogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sh_pref.edit();
            editor.putInt("applogincredentials", 0);
            editor.commit();

        }
        else if( id==R.id.action_myprofile)
        {
            //Start new activity, where my profile details will be displayed
            Log.d(TAG, "Myprofile PAGE about to begin");
            //Intent intent_mydetails = new Intent(this, ActivityMyProfileDetails.class);
            Intent intent_mydetails = new Intent(this, ActivityBasicDetails.class);

            intent_mydetails.putExtra("sessionid", this.sessionid);
            startActivity(intent_mydetails);

        }
        else if(id == R.id.action_search)
        {
            startActivity(new Intent(this, SearchActivity.class));

        }
        else if ( id == R.id.registration_notification)
        {

            startActivity(new Intent(this, Recievenotifications.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Log.d(TAG,"backpressed again "+ backpressedagain);
        if(backpressedagain)
        {
            Intent intent_logout = new Intent(this, ActivityFirstLogin.class);
            startActivity(intent_logout);
            SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("applogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sh_pref.edit();
            editor.putInt("applogincredentials", 0);
            editor.commit();
            finish();

        }
        else
        {
            Toast.makeText(this,"Press again to exit",Toast.LENGTH_LONG).show();
            backpressedagain=true;
        }
    }

    protected void onResume()
    {
        super.onResume();
        backpressedagain=false;
    }
    protected void onPause()
    {
        super.onPause();
        backpressedagain=false;
    }
}
