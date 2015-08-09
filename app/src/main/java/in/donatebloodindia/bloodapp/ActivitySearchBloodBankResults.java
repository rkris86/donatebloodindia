package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ActivitySearchBloodBankResults extends Activity
{

    public static final String TAG="ActivitySearchBloodBankResults";
    public static String search_results_status;
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbloodbank_results_listview);
        search_results_status="default";

        //Get City the user has selected  and pass it on to get get_serachresults to execute
        // search results api
        Log.d(TAG, "In activitysearch-bloodbankresutls "+ "krsi");
        //Get City & Location selected by user
        String city= (String) getIntent().getExtras().get("selected_city");

        int ids = find_id(city);
        //Toast.makeText(getApplicationContext(), "Searching. Please wait" , Toast.LENGTH_LONG).show();

        String result_urlstring = "http://donatebloodindia.in/api/search.php?Usertype=2&City="+ids;
        Log.d(TAG, result_urlstring);
        new get_searchresults().execute(result_urlstring);

    }

    private class get_searchresults extends AsyncTask<String, Void , String>
    {

        char[] buffer= new char[9999];

        // Progress dialog to show progress during search
        ProgressDialog pd;

        // the donor search results of json parsing are store in these below arrays
        String[]  search_bloodbank_name, search_bloodbank_description, search_bloodbank_phonenumber, search_bloodbank_city, search_bloodbank_location ;

        protected void onPreExecute()
        {
            pd = new ProgressDialog( ActivitySearchBloodBankResults.this);
            pd.setMessage("Searching Bloodbanks");
            pd.show();
        }

        protected String doInBackground(String... params)
        {
            InputStream io = null;
            URL url ;
            Reader read;
            HttpURLConnection  conn;
            try
            {
                // URL is passed as params[0] when asynctask class is called
                url = new URL(params[0]);
                conn =  (HttpURLConnection) url.openConnection();
                conn.setFollowRedirects(false);
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(6000);
                conn.connect();

                if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
                {
                    Log.d(TAG, "getResponseCode of connecton is good");
                    io = conn.getInputStream();
                    read = new InputStreamReader(io, "UTF-8");
                    read.read(buffer);
                    Log.d(TAG, String.valueOf(buffer));
                    return String.valueOf(buffer).trim();

                    //BufferedInputStream br = new BufferedInputStream(new InputStreamReader(io));


                }
                else
                {
                    Log.d(TAG,"HTTPConnection ISSUE");
                    search_results_status="exception";
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch ( SocketTimeoutException ste){
                Log.d(TAG, "TIMEOUT ISSUE");
            }
            catch (Exception e){
                Log.d(TAG, "Exception in creating reading info for search results function");
                e.printStackTrace();
            }
            finally
            {
                if (io != null)
                    try {
                        io.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            search_results_status="exception";
            return search_results_status;
        }

        protected void onPostExecute(String results)
        {
            Log.d(TAG, "In PostExecute");
            pd.dismiss();

            if(search_results_status.contains("exception"))
            {
                Toast.makeText(getApplicationContext(),"There seems to be some technical issue. Try again",Toast.LENGTH_LONG).show();
                finish();

            }
            // This function get results in to this array
            try
            {
                JSONObject json_obj = new JSONObject(results);
                JSONArray  json_arr=  json_obj.getJSONArray("Message Response");

                search_bloodbank_name = new String [json_arr.length()];
                search_bloodbank_description = new String [json_arr.length()];
                search_bloodbank_phonenumber = new String [json_arr.length()];
                //search_bloodbank_location = new String [json_arr.length()];
                //search_bloodbank_city = new String [json_arr.length()];

                JSONObject json_search_object ;

                for( int i=0;i< json_arr.length() ;i++)
                {
                    json_search_object    = json_arr.getJSONObject(i);
                    search_bloodbank_name[i]  =  json_search_object.getString("Name");
                    search_bloodbank_description[i] = json_search_object.getString("Description");

                    search_bloodbank_phonenumber[i]  = json_search_object.getString("Mobile");
                    //Log.d("ActivitySearchResults",String.valueOf(search_bloodbank_phonenumber[i] ));
                    //search_bloodbank_city[i]  = find_city(Integer.parseInt(json_search_object.getString("City")));
                    //search_bloodbank_location[i] = find_location(Integer.parseInt(json_search_object.getString("Location")));

                    Log.d(TAG,String.valueOf(search_bloodbank_name[i]));
                }

            }
            catch (Exception e) {
                Log.d(TAG, "Exception in json response handling");
                e.printStackTrace();
                search_results_status="exception";
                Toast.makeText(getApplicationContext(),"There seems to be some techical issue. Try again",Toast.LENGTH_LONG).show();
                finish();
            }

            // Run below code only if search_results_status doesn't have "exception" assigned to it
            if(!search_results_status.contains("exception"))
            {
                // Get the Listview2 created for displaying donor search results
                ListView lv = (ListView) findViewById(R.id.listView3);

                // Creating a custum list arrayadapter searchresults_adapter and passing the data that needs to put in the listview
                // through donor_name, donor_city, donor_phone, donor_city
                SearchBloodbankResultsListViewDisplay searchresults_adapter = new SearchBloodbankResultsListViewDisplay(getApplicationContext(), search_bloodbank_name, search_bloodbank_description, search_bloodbank_phonenumber);

                // just make sure the List view is not null, other wise there will null point exception thrown.
                if ((lv == null))
                    Log.d("ActivitySearchResults", "list view is null ");
                else
                    lv.setAdapter(searchresults_adapter);

            }

        }

    }


    private String find_city (int city_int)
    {
        // function return the city -  used identify city based on the integer passed.
        String cities_id[]  = {"Mumbai","Hyderabad", "Bangalore","Delhi","Chennai"};
        return cities_id[city_int-1];

    }

    private String find_location(int location_int)
    {

        // function return the location based on the integer generated in the search donor results
        String subcities_id[] = {  "South Mumbai",  "Western suburbs" ,"Central suburbs", "Thane and Beyond","Navi Mumbai" ,"Dahisar and Beyond" ,
                "West Hyderabad" , "North Hyderabad"  ,"East Hyderabad" ,   "Central Hyderabad" , "South Hyderabad","Outer Hyderabad",
                "South Bangalore" ,"North Bangalore", "West Bangalore" ,  "Central Bangalore",  "East Bangalore","Outer Bangalore",
                "New Delhi", "South Delhi" , "East Delhi" ,   "North Delhi",  "West Delhi" ,"Gurgaon" ,
                "South Chennai", "North chennai","West Chennai","East Chennai","Central Chennai","Outer Chennai",
        };
        return subcities_id[location_int];

    }

    private int find_id(String city2)
    {

        // This function is used to identify the id's of city and sub_city
        int id_return=0;
        String cities_id[]  = {"Mumbai","Hyderabad", "Bangalore","Delhi","Chennai"};


        for (int i =0; i< cities_id.length; i++)
        {
            if(city2.equalsIgnoreCase(cities_id[i]))
            {
                id_return = ++i;
                Log.d("SearchBloodbank_findid_function","In find function 22 "+ String.valueOf(i));
                break;
            }

        }
        return id_return;

    }

}

