package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ActivitySearchDonorResults extends Activity
{

    // declaring a static serach results string to capture the result of search
    public static String search_result_status;
    public static final String TAG = "ActivitySearchDonorResults" ;
    protected void onCreate( Bundle savedInstanceState )
    {

        search_result_status="default";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdonor_results);


        //Get City the user has selected  and pass it on to get get_serachresults to execute
        // search results api
        Log.d(TAG,"In activitysearchdonorresutls"+ "krsi");
        //Get City & Location selected by user
        String city= (String) getIntent().getExtras().get("city");
        String subcity="thane";
        String bloodgroup2 = (String) getIntent().getExtras().get("bloodgroup");

        int ids[] = find_id(city, subcity);
        //	find_bloodgroup(bloodgroup2);

        //Toast.makeText(getApplicationContext(), "Searching.. Please wait", Toast.LENGTH_SHORT).show();

        String result_urlstring = "http://donatebloodindia.in/api/search.php?Usertype=1&City=" + ids[0]+"&Bloodgroup="+find_bloodgroup(bloodgroup2);
        Log.d(TAG, result_urlstring);
        // Get search results through async task
        new get_searchresults().execute(result_urlstring);
        if(search_result_status.contains("nodonors"))
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(getApplicationContext());
            adb.setTitle("No Donors");
            adb.setMessage("There are No donors currently for this blood group in this city ");
            adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            adb.show();
        }
    }

    private class get_searchresults extends AsyncTask<String, Void , String>
    {

        InputStream io =null;
        char[] buffer= new char[1500];

        // Progress dialog to show progress during search
        ProgressDialog pd;

        // the donor search results of json parsing are store in these below arrays
        String[]  search_donor_name, search_blood_group, search_donor_phone, search_donor_city, search_donor_location ;

        protected void onPreExecute()
        {
            pd = new ProgressDialog( ActivitySearchDonorResults.this);
            pd.setMessage("Searching.. Donors");
            pd.show();
        }

        protected String doInBackground(String... params)
        {
            Log.d(TAG, "In background activity");
            URL url = null;
            Reader read = null;
            try
            {
                // URL is passed as params[0] when asynctask class is called
                url = new URL(params[0]);
                HttpURLConnection  conn =  (HttpURLConnection) url.openConnection();
                conn.connect();
                io = conn.getInputStream();

                read = new InputStreamReader(io, "UTF-8");
                read.read(buffer);


            } catch (Exception e)
            {
                Log.d("Exception", "Exception in creating reading info for search results function");
                e.printStackTrace();
                search_result_status="exception";
            }
            Log.d(TAG, String.valueOf(buffer).trim());
            return String.valueOf(buffer).trim();

        }


        protected void onPostExecute(String results)
        {
            //Log.d("Activity search results", "In PostExecute");
            pd.dismiss();
            // This function get results in to this array
            if(search_result_status.contains("exception"))
            {

            }
            else
            {
                try {
                    JSONObject json_obj = new JSONObject(results);
                    JSONArray json_arr = json_obj.getJSONArray("Message Response");

                    search_donor_name = new String[json_arr.length()];
                    search_blood_group = new String[json_arr.length()];
                    search_donor_phone = new String[json_arr.length()];
                    search_donor_city = new String[json_arr.length()];
                    search_donor_location = new String[json_arr.length()];

                    JSONObject json_search_object;

                    for (int i = 0; i < json_arr.length(); i++) {
                        json_search_object = json_arr.getJSONObject(i);

                        search_donor_name[i] = json_search_object.getString("Name");
                        search_blood_group[i] = json_search_object.getString("Blood Group");
                        search_donor_phone[i] = json_search_object.getString("Phone");

                        search_donor_city[i] = find_city(Integer.parseInt(json_search_object.getString("City")));
                        search_donor_location[i] = find_location(Integer.parseInt(json_search_object.getString("Location")));

                        //Log.d("ActivitySearchResults", search_donor_city[i]);
                        search_result_status="donorsexist";
                    }

                } catch (JSONException e)
                {
                    Log.d(TAG, "There are no donors.Sorry ");
                    Toast.makeText(getApplicationContext(), "There are no donors in this category..Sorry", Toast.LENGTH_LONG).show();
                    search_result_status="nodonors";
                    finish();
                }
            }
            if(!search_result_status.contains("nodonors"))
            {
                // Get the Listview2 created for displaying donor search results
                ListView lv = (ListView) findViewById(R.id.listView2);

                // Creating a custom list array adapter searchresults_adapter and passing the data that needs to put in the listview
                // through donor_name, donor_city, donor_phone, donor_city
                SearchDonorResultsListViewDisplay searchresults_adapter = new SearchDonorResultsListViewDisplay(getApplicationContext(), search_donor_name, search_donor_phone, search_donor_city, search_donor_location);

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
    private String find_bloodgroup(String bloodgroup)
    {
        if(bloodgroup.equalsIgnoreCase("O+ve"))
            return "1";
        else if (bloodgroup.equalsIgnoreCase("O-ve"))
            return "2";
        else if (bloodgroup.equalsIgnoreCase("A+ve"))
            return "3";
        else if (bloodgroup.equalsIgnoreCase("A-ve"))
            return "4";
        else if (bloodgroup.equalsIgnoreCase("B+ve"))
            return "5";
        else if (bloodgroup.equalsIgnoreCase("B-ve"))
            return "6";
        else if (bloodgroup.equalsIgnoreCase("AB+ve"))
            return "7";
        else if (bloodgroup.equalsIgnoreCase("AB-ve"))
            return "8";

        return "0";

    }

    private String find_location(int location_int)
    {

        // function returnt the location based on the integer genered in the searc donor results
        String subcities_id[] = {  "South Mumbai",  "Western suburbs" ,"Central suburbs", "Thane and Beyond","Navi Mumbai" ,"Dahisar and Beyond" ,
                "West Hyderabad" , "North Hyderabad"  ,"East Hyderabad" ,   "Central Hyderabad" , "South Hyderabad","Outer Hyderabad",
                "South Bangalore" ,"North Bangalore", "West Bangalore" ,  "Central Bangalore",  "East Bangalore","Outer Bangalore",
                "New Delhi", "South Delhi" , "East Delhi" ,   "North Delhi",  "West Delhi" ,"Gurgaon" ,
                "South Chennai", "North chennai","West Chennai","East Chennai","Central Chennai","Outer Chennai",
        };

        return subcities_id[location_int - 1];

    }

    private int[] find_id(String city2, String sub_city2)
    {

        // This function is used to identify the id's of city and sub_city
        int id_return[] ={0,0};
        String cities_id[]  = {"Mumbai","Hyderabad", "Bangalore","Delhi","Chennai"};
        String subcities_id[] = {  "South Mumbai",  "Western suburbs" ,"Central suburbs", "Thane and Beyond","Navi Mumbai" ,"Dahisar and Beyond" ,
                "West Hyderabad" , "North Hyderabad"  ,"East Hyderabad" ,   "Central Hyderabad" , "South Hyderabad","Outer Hyderabad",
                "South Bangalore" ,"North Bangalore", "West Bangalore" ,  "Central Bangalore",  "East Bangalore","Outer Bangalore",
                "New Delhi", "South Delhi" , "East Delhi" ,   "North Delhi",  "West Delhi" ,"Gurgaon" ,
                "South Chennai", "North chennai","West Chennai","East Chennai","Central Chennai","Outer Chennai",
        };


        for (int i =0; i< cities_id.length; i++)
        {
            if(city2.equalsIgnoreCase(cities_id[i]))
            {	id_return[0] = ++i;
                Log.d("RegisterActivity","In find function 22 "+ String.valueOf(i));
                break;
            }

        }
        return id_return;

    }

}
