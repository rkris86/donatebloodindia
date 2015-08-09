package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.client.methods.HttpOptions;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// Creating custom list view HomeListAnnoucements
// this custom list is used to display item in the home button
// The data from parsing of json data is store in String array title_list_results[] & description_list_results[]
public class HomeListAnnoucements extends ArrayAdapter<String>
{

    public Context context;
    public String[] title;
    public String[] description ;
    public String[] announce_id;
    public ProgressDialog pd;
    public AlertDialog.Builder alert ;

    TextView txttitle,txtdescription;
    Button donate_button,later_button;

    // Creating a custom construction where title and description messages passed in arguments

    HomeListAnnoucements(Context context2, String[] objects , String[] description_list , String[] annoucement_id)
    {

        super(context2, R.layout.layout_home_list, R.id.title_message, objects);

        //context is used to get layout inflate manager
        this.context = context2;

        // Passing the title & Description data of Json to local objects
        // these data is put dynamically in the view
        this.title = objects;
        this.description= description_list;
        this.announce_id= annoucement_id;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        // setting the process dialog- to be used when user presses donate button on the announcement

        View rowview = convertView;
        final Context parent_context= parent.getContext();

        // Optimization of row view
        if( rowview == null)
        {
            LayoutInflater inflater =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            rowview= inflater.inflate(R.layout.layout_home_list, parent, false);
        }

        txttitle = (TextView) rowview.findViewById(R.id.title_message);
        txttitle.setText(this.title[position]);

        txtdescription = (TextView) rowview.findViewById(R.id.description_message);
        txtdescription.setText(this.description[position]);

        //setting action items on Donate button. Adding functionality in button

        donate_button =  (Button) rowview.findViewById(R.id.donate_button_annoucement);

        // setting the set-tag to get position of the clicked button in the list view
        // so that one can use it later
        donate_button.setTag(position);


        donate_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // get the position of the donate button clicked

                View vw  =  (View) v.getParent();
                Button bt =  (Button)vw.findViewById(R.id.donate_button_annoucement);
                Log.d("HomeListAnnoucements", "Donate is clicked  position " + announce_id[(Integer) bt.getTag()]);
                final String  id_temp= announce_id[(Integer) bt.getTag()];
                bt.setBackgroundColor(Color.parseColor("#Faaaaa"));


                // Setting up AlertDailog alert
                AlertDialog.Builder alert = new AlertDialog.Builder( parent_context);
                alert.setTitle("Blood App");
                alert.setMessage("We are about to send bloodinformation to group");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do something with value!
                        pd = new ProgressDialog(parent_context);
                        SharedPreferences shared_pref = context.getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                        String session_string = shared_pref.getString("userSessionId", "");
                        Log.d("send donate msg bck 2 server: sessionid: ", session_string);

                        String url = "http://donatebloodindia.in/api/message-response.php?SessionId="+ session_string+"&AnnounceId="+id_temp+"&Response=1";
                        giveresponse_donate donate_aTask = new giveresponse_donate(parent_context);
                        donate_aTask.execute(url);
                    }
                });


                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
            }
        });
        return rowview;
    }

    public class giveresponse_donate extends AsyncTask<String, Void, String>
    {
        private Context myContext;
        giveresponse_donate(Context context)
        {
            myContext = context;
        }
        protected void onPreExecute()
        {

            pd.setMessage("Sending");
            pd.show();
        }
        @Override
        protected String doInBackground(String... params)
        {
            char bufferingchar[] = new char[1300];
            try
            {
                URL url_url = new URL(params[0]);
                HttpURLConnection connect  =  (HttpURLConnection) url_url.openConnection();
                connect.connect();
                InputStream is = connect.getInputStream();

                Reader readerstream = null;
                readerstream = new InputStreamReader(is, "UTF-8");
                readerstream.read(bufferingchar);
                Log.d("donate response in json", String.valueOf(bufferingchar).trim());

            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }

            return  String.valueOf(bufferingchar).trim();

        }
        protected void onPostExecute(String output)
        {

            String result_response = json_encode(output);
            pd.dismiss();
            AlertDialog.Builder alert2 = new AlertDialog.Builder(myContext );
            alert2.setTitle("Blood App");

            if(result_response.equalsIgnoreCase("success"))
            {
                alert2.setMessage("The Interest has been noted. The blood bank will contact you soon");
            }
            else
            {
                alert2.setMessage("Oops. There seems to be some issue. Please try again");

            }
            alert2.setCancelable(false);
            alert2.setNegativeButton("Ok", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            alert2.show();

            //Log.d("response result", result_response);

        }
        public String json_encode(String s)
        {
            JSONObject message_response_jsonobj1, message_response_jsonobj2;
            try
            {
                message_response_jsonobj1 = new JSONObject(s);
                message_response_jsonobj2=  message_response_jsonobj1.getJSONObject("Message Response");
                return message_response_jsonobj2.getString("status");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }


    }

}
