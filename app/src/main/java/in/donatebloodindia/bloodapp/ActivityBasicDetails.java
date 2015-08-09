package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ActivityBasicDetails extends Activity
{

    String SessionId;
    ProgressDialog pd;
    Context contxt;
    public static final String TAG= "ActivityBasicDetails";
    public String result_output; // store output of onPostExecute function
    protected void onCreate( Bundle savedInstanceState)
    {

        result_output="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicprofile_detail);
        contxt = this;

        String url_string  = "http://donatebloodindia.in/api/getReports.php?SessionId="+SessionId;
        Log.d("Myprofile", "url_string " + url_string);
        setTitle("My Profile Details");

        // Find out if there are any results, if yes in post execute display the results

        SharedPreferences shared_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);

        String user_city = shared_pref.getString("usercity", "");

        String user_name =shared_pref.getString("userName", "");
        String user_bloodgroup =shared_pref.getString("userBloodGroup", "");
        //edi_tor.putString("userAge", responsejson[5]);
        this.SessionId = shared_pref.getString("userSessionId", "");


        String user_location =shared_pref.getString("userLocation", "");
        String user_phone =shared_pref.getString("userPhone", "");

        TextView tv_bloodgroup = (TextView) findViewById(R.id.mybasicprofile_detail_bloodgroup_textview);
        tv_bloodgroup.setText(user_bloodgroup);

        TextView tv_name = (TextView) findViewById(R.id.mybasicprofile_detail_name_textview);
        tv_name.setText(user_name);

        TextView tv_location = (TextView) findViewById(R.id.mybasicprofile_detail_location_textview);
        tv_location.setText(user_location);

        TextView tv = (TextView) findViewById(R.id.mybasicprofile_detail_city_textview);
        tv.setText(user_city);


    }
    public void showMyDonationReports(View view)
    {

        Log.d("TAG", "TO call mydonation reports "+this.SessionId);
        //Intent intent_mydetails = new Intent(this, ActivityMyProfileDetails.class);
        Intent intent_mydetails = new Intent(this, ActivityMyProfileDetails.class);


        intent_mydetails.putExtra("sessionid", this.SessionId);
        startActivity(intent_mydetails);
    }

}
