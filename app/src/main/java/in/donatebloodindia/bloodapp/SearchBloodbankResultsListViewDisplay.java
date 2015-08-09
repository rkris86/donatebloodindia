package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SearchBloodbankResultsListViewDisplay extends ArrayAdapter<String>
{

    public Context context;

    public String[] bloodbank_name;
    public String[] bloodbank_description;
    public String[] bloodbank_city;
    public String[] bloodbank_phonenumber;

    TextView txtname =null, txtdescription =null , txtcity=null, txtlocation=null;
    Button call_button=null;

    public SearchBloodbankResultsListViewDisplay( Context context, String[]  names , String[]  description, String[] phonenumber )
    {


        super(context,  R.layout.layout_search_bloodbankresults,  names);

        this.context = context;
        // TODO Auto-generated constructor stub
        this.bloodbank_name = names;
        this.bloodbank_description = description;
        this.bloodbank_phonenumber = phonenumber;
        //this.bloodbank_city = city;

        Log.d("ActivitySearchBloodbankResults", "In contructor");

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        //Log.d("ActivitySearchResults", "IN getview");
        // for optimization
        View rowview = convertView;
        if( rowview == null)
        {
            LayoutInflater inflater =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            rowview = inflater.inflate(R.layout.layout_search_bloodbankresults, parent, false);

        }

        txtname = (TextView) rowview.findViewById(R.id.search_bloodbankresults_bloodbankname);
        txtname.setText(this.bloodbank_name[position]);

        txtdescription = (TextView) rowview.findViewById(R.id.search_bloodbankresults_description);
        txtdescription.setText(this.bloodbank_description[position]);

        //txtcity = (TextView) rowview.findViewById(R.id.search_donorresults_citylocation);
        //txtcity.setText(this.bloodbank_location[position] + ", " + this.bloodbank_city[position]);

        //setting action items on Donate button. Adding functionality in button
        call_button =  (Button) rowview.findViewById(R.id.search_bloodbankresults_call_button);
        call_button.setTag(this.bloodbank_phonenumber[position]);

        Log.d("search blood bank results phone", String.valueOf(bloodbank_phonenumber[position]));
        call_button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Call the phone number of the Donor

                View parent_view = (View) v.getParent();
                //TextView  tv = (TextView) parent_view.findViewById(R.id.search_donorresults_mobilenumber);
                Button bu= (Button) parent_view.findViewById(R.id.search_bloodbankresults_call_button);

                //Log.d("callbutton", String.valueOf(bu.getTag()));
                Uri number = Uri.parse("tel: "+ String.valueOf(bu.getTag()));
                Intent intent = new Intent( Intent.ACTION_CALL);
                intent.setData(number);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("Abouttocall-searchbloodbankresults-call", String.valueOf(bu.getTag()) );
                context.startActivity(intent);

            }
        });

        return rowview;

    }
}
