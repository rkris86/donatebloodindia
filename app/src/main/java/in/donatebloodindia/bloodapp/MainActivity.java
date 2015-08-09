package in.donatebloodindia.bloodapp;



// this activity is used settings page
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements  OnItemSelectedListener {


    EditText  firstname, lastname ,phone;
    Spinner subcity_spinner, city , bloodgroup, gender_spinner;
    String u_fname,u_lname, u_bloodgroup, u_city,u_subcity ,u_phone ,u_gender;

    public static String TAG="MainActivityREGISTERHOMEPAGE";

    //public String[]  userdata ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/

        setTitle("Register");

        subcity_spinner = (Spinner) findViewById(R.id.donor_subcity);

        city= (Spinner) findViewById(R.id.donor_city);
        gender_spinner = (Spinner) findViewById(R.id.donor_gender);


        //adding textwatch listener activity  - to validate input
        // firstname.addTextChangedListener(this);

        //adding listener on item selected on city
        city.setOnItemSelectedListener(this);

        //Set the color of ActionBar as Red or by default black color will be used
        // getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb( 247, 36, 36)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.login_main, container,
                    false);
            return rootView;
        }
    }
    /** Activities when user presses Register button
     *   change the color of button on click
     */
    public void donorRegister(View view)
    {

        //check whether Internet connectivity exists or not
        CharSequence text_not_available = "Internet Not available, Please check to enable Internet ";
        ConnectivityManager  cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork!= null &&    activeNetwork.isConnected() )
        {
            //Toast.makeText(getApplicationContext(), city.getSelectedItem().toString(), Toast.LENGTH_LONG ).show();
            // Loader icon to be displayed ( ie. New activity is called)
            //Collect all the user input

            firstname = (EditText) findViewById(R.id.donor_first_name);
            u_fname = firstname.getText().toString();

            lastname = (EditText) findViewById(R.id.donor_last_name);
            u_lname =  lastname.getText().toString();

            phone = (EditText) findViewById(R.id.donor_mobile);
            u_phone = phone.getText().toString();

            bloodgroup =  (Spinner) findViewById(R.id.donor_blood_group);
            u_bloodgroup = bloodgroup.getSelectedItem().toString();

            //gender_spinner = (Spinner) findViewById(R.id.donor_gender);
            u_gender = gender_spinner.getSelectedItem().toString();

            city =  (Spinner) findViewById(R.id.donor_city);
            u_city = city.getSelectedItem().toString();

            subcity_spinner =  (Spinner) findViewById(R.id.donor_subcity);
            u_subcity = subcity_spinner.getSelectedItem().toString();

            Log.d(TAG," BLOOD GROUP selected" + u_bloodgroup);

            String[] userdata = { u_fname,u_lname,u_phone,u_bloodgroup, u_city, u_subcity};

            int response_int = inputvalidation(userdata);
            Log.e("krishna", "int response"+response_int);

            // Alert Dialog - If user validation is not right
            // if respose_int is zero, then input is validated.
            if(response_int == 0)
            {

                Intent intent = new Intent(this, RegisterLoaderActivity.class);

                intent.putExtra("first_name",u_fname);
                intent.putExtra("last_name",u_lname);
                intent.putExtra("mobile", u_phone);
                intent.putExtra("bloodgroup", u_bloodgroup);
                intent.putExtra("city", u_city);
                intent.putExtra("sub_city", u_subcity);
                intent.putExtra("gender", u_gender);
                startActivity(intent);
            }
            else
            {
                //Creating a AlertDialog & Passing the response code
                //based on response code, appropriate message will be showed

                ShowDialog register_emessage = new ShowDialog();
                Bundle bdl = new Bundle();

                bdl.putInt("response_code", response_int);
                register_emessage.setArguments(bdl);
                register_emessage.show(getFragmentManager(), "tag_register_error_message");

            }

        }
        else
        {
            //show message that that there is not INTERNET connection available
            Toast.makeText(this, text_not_available, Toast.LENGTH_LONG).show();
        }
    }

    // class to validate user input data
    public int inputvalidation( String[] userdata )
    {
        Pattern p = Pattern.compile("[^a-zA-Z ]");
        Log.d("krishsna", "userdata:fname " + userdata[0]);

        Matcher m_fname = p.matcher(userdata[0].trim());
        boolean b_fname = m_fname.find();

        Matcher m_lname = p.matcher(userdata[1].trim());
        boolean b_lname = m_lname.find();

        Pattern number = Pattern.compile("[^0-9 ]");

        Matcher m_phone = number.matcher(userdata[2].trim());
        boolean b_phone = m_phone.find();


        // error_message_register = 0 (if all user input is fine)
        // error_message_register = -1 (some of input is not filled up is fine)
        // error_message_register = 1 (if firstname is not valid)
        // error_message_register = 2 (if lastname is not valid)
        // error_message_register = 3 (if fistname & last is not valid)
        // error_message_register = 4 (if mobile is not valid)
        // error_message_register = 5 (if mobile & Firstname is not valid)
        // error_message_register = 6 (if mobile & last name  is not valid)
        // error_message_register = 7 (if mobile,firstname & last  is not valid)

        int error_message_register = -1;

        if(userdata[0].trim().length()>0 & userdata[1].trim().length()>0 && userdata[2].trim().length()>0)
        {
            error_message_register=0;
            if(b_fname)
            {
                error_message_register=1;
            }
            if (b_lname)
            {
                error_message_register=error_message_register + 2;
            }
            //Log.d("krishna", " "+ b_phone);
            if(b_phone)
            {
                error_message_register = error_message_register + 4;
            }
        }
        return error_message_register;

    }

    //  Used to give subcity locations dynamically in the spinner based on the options
    //  selected by user in the city spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        ArrayList<String> subcity_list = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(), "Itemselected" +city.getSelectedItem().toString() , Toast.LENGTH_LONG).show();

        //Find if the city chosen by user
        if(city.getSelectedItem().toString().equalsIgnoreCase("Mumbai"))
        {
            subcity_list.clear();
            subcity_list.add("South Mumbai");
            subcity_list.add("Western Suburbs");
            subcity_list.add("Central Suburbs");
            subcity_list.add("Thane and Beyond");
            subcity_list.add("Navi Mumbai");
            subcity_list.add("Dahisar and Beyond");

        }
        else if (city.getSelectedItem().toString().equalsIgnoreCase("Hyderabad"))
        {
            subcity_list.clear();
            subcity_list.add("West Hyderabad");
            subcity_list.add("North Hyderabad");
            subcity_list.add("East  Hyderabad");
            subcity_list.add("Central Hyderabad");
            subcity_list.add("South Hyderabad");
            subcity_list.add("Outer Hyderabad");


        }
        else if (city.getSelectedItem().toString().equalsIgnoreCase("Delhi"))
        {
            subcity_list.clear();
            subcity_list.add("New Delhi");
            subcity_list.add("South Delhi");
            subcity_list.add("East  Delhi");
            subcity_list.add("North Delhi");
            subcity_list.add("West Delhi");
            subcity_list.add("Gurgaon");


        }
        else if (city.getSelectedItem().toString().equalsIgnoreCase("Bangalore"))
        {
            subcity_list.clear();
            subcity_list.add("South Bangalore");
            subcity_list.add("North Bangalore");
            subcity_list.add("West  Bangalore");
            subcity_list.add("Central Bangalore");
            subcity_list.add("East Bangalore");
            subcity_list.add("Outer Bangalore");

        }
        else if (city.getSelectedItem().toString().equalsIgnoreCase("Chennai"))
        {
            subcity_list.clear();
            subcity_list.add("South Chennai");
            subcity_list.add("North Chennai");
            subcity_list.add("West  Chennai");
            subcity_list.add("East Chennai");
            subcity_list.add("Central Chennai");
            subcity_list.add("Outer Chennai");

        }

        //Use arrayadapter to load the elements of subcity dynamically to sub_city

        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , subcity_list);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subcity_spinner.setAdapter(LTRadapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub

    }

}
