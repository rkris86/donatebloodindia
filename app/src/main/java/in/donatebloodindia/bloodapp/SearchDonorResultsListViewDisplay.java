package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.R.integer;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchDonorResultsListViewDisplay extends ArrayAdapter<String> implements OnClickListener
{

    public Context context;
    public Activity activity;

    public String[] donor_name;
    public String[] donor_bloodgroup;
    public String[] donor_phone;
    public String[] donor_city;
    public String[] donor_location;

    TextView txtname =null, txtphone=null , txtcity=null, txtlocation=null;

    Button call_button =null, msg_button=null;


    public SearchDonorResultsListViewDisplay( Context context, String[]  names , String[]  phone, String[] city, String[] location )
    {

        super(context,  R.layout.layout_search_donorresults_list,  names);

        this.context = context;
        // TODO Auto-generated constructor stub
        this.donor_name = names;
        //this.donor_bloodgroup=bloodgroup;
        this.donor_phone = phone;
        this.donor_location = location;
        this.donor_city = city;

        Log.d("SearchDonorResultsListViewDisplay", "In contructor");


    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowview = convertView;
        if (rowview == null)
        {

            // Inflating Search results Layout with Donor search name, mobile number with call and submit
            LayoutInflater inflater =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            rowview = inflater.inflate(R.layout.layout_search_donorresults_list, parent, false);

        }
        txtname = (TextView) rowview.findViewById(R.id.search_donorresults_donorname);
        txtname.setText(this.donor_name[position]);

        txtphone = (TextView) rowview.findViewById(R.id.search_donorresults_mobilenumber);
        txtphone.setText(this.donor_phone[position]);
        txtphone.setTag(this.donor_phone[position]);

        txtcity = (TextView) rowview.findViewById(R.id.search_donorresults_citylocation);
        txtcity.setText(this.donor_location[position] + ", " + this.donor_city[position]);

        //setting action items on Donate button. Adding functionality in button
        call_button =  (Button) rowview.findViewById(R.id.search_donorresults_call_button);
        call_button.setTag(this.donor_phone[position]);
        call_button.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // Call the phone number of the Donor

                View parent_view = (View) v.getParent();
                //TextView  tv = (TextView) parent_view.findViewById(R.id.search_donorresults_mobilenumber);
                Button bu= (Button) parent_view.findViewById(R.id.search_donorresults_call_button);

                //Log.d("callbutton", String.valueOf(bu.getTag()));
                Uri number = Uri.parse("tel: "+ String.valueOf(bu.getTag()));
                Intent intent = new Intent( Intent.ACTION_CALL);
                intent.setData(number);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("Abouttocall", String.valueOf(bu.getTag()) );
                context.startActivity(intent);

            }
        });

        msg_button = (Button) rowview.findViewById(R.id.search_donorresults_message_button);
        msg_button.setTag(this.donor_phone[position]);
        msg_button.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //message SMS to the result mobile number
                View parent_view = (View) v.getParent();
                Button bu = (Button) parent_view.findViewById(R.id.search_donorresults_message_button);
                //Log.d("Mobile number sms" , String.valueOf(bu.getTag()));

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
                intent.putExtra("address", String.valueOf(bu.getTag()));
                //intent.putExtra("sms_body", "Greetings!");
                intent.setType("vnd.android-dir/mms-sms");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra(Intent.EXTRA_STREAM, attachment);
                try
                {
                    context.startActivity(intent);
                }
                catch ( Exception e)
                {
                    Toast.makeText(context, "You might be using a Tablet, so messaging is not allowed" , Toast.LENGTH_LONG).show();
                }
            }

        });

        return rowview;

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

    }

}
