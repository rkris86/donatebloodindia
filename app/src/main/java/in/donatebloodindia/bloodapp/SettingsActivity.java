package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class SettingsActivity extends Activity implements communicator
{

    public ProgressDialog progress;
    Context contxt;
    public String userSessionId;
    public static final String TAG = "SettingsActivity";


    protected void logoutprocess()
    {
        Intent intent_logout = new Intent(this, ActivityFirstLogin.class);
        startActivity(intent_logout);
        SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("applogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh_pref.edit();
        editor.putInt("applogincredentials", 0);
        editor.commit();
    }
    protected void donotUpdateCitySubcity(int city, int subcity)
    {
        // To udpate city and subcity by the user
        //  pass on city and subcity argument
        SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
        String session_id = sh_pref.getString("userSessionId", "fail");

        String str_ChangeCitySubcity = "http://donatebloodindia.in/api/donor_city_update.php?City="+String.valueOf(city)+"&Subcity="+String.valueOf(subcity)+"&SessionId="+session_id;
        Log.d(TAG+"urlurl",str_ChangeCitySubcity);
        Log.d(TAG,"Change city and subcity :got session id"+ session_id);

        String[] params= {str_ChangeCitySubcity};
        new changecitysubcitynow().execute(params);
    }
    protected void donorEmailUpdate(String em)
    {
        // To udpate Email  by the user
        //  pass on email argument
        SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
        String session_id = sh_pref.getString("userSessionId", "fail");
        Log.d("Change city and subcity :got session id", session_id);

        String str_email = "http://donatebloodindia.in/api/donor_email_update.php?Emailid="+em;
        String[] params= {str_email};
        new changeemailnow().execute(params);
    }


    protected void onCreate( Bundle savedInstanceState)
    {
        Log.d("settings" , "Am in settings page");
        super.onCreate(savedInstanceState);

        contxt = this;
        setContentView(R.layout.settings_activity_listview);
        String settings_list_items[] = {"Change Password" , "Change City" , "Delete your Account" ,"Notifications" , "Update your Email id"};

        ListView lv = (ListView) findViewById(R.id.settings_activity_listview);
        //lv.setTextFilterEnabled(true);

        progress = new ProgressDialog(this);
        ArrayAdapter<String>  setting_list_adaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, settings_list_items);
        lv.setAdapter(setting_list_adaptor);


        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Log.d("setting position", String.valueOf(position));
                if(position==0)
                {
                    FragmentManager fm =  getFragmentManager();
                    SettingsChangePasswordFragment  scpf = new SettingsChangePasswordFragment();
                    scpf.show(fm, "test");

                }
                else if(position == 1)
                {
                    // change city
                    AlertDialog.Builder ab1 = new AlertDialog.Builder(SettingsActivity.this);
                    ab1.setTitle("Select you city");
                    //add cities to items
                    ab1.setItems(R.array.listentries, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which)
                        {
                            // select subcity
                            AlertDialog.Builder ab2 = new AlertDialog.Builder(SettingsActivity.this);
                            ab2.setTitle("Change sub city");

                            if (which == 0)
                            {
                                ab2.setItems(R.array.mumbaisubcities, new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Log.d(TAG, "subcity_msg"+ String.valueOf(which));
                                        donotUpdateCitySubcity(1, which+1);

                                    }
                                });
                            }
                            if (which == 1)
                            {
                                ab2.setItems(R.array.hyderabadsubcities, new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        donotUpdateCitySubcity(2, 6 + which+1);
                                    }
                                });
                            }
                            if (which == 2)
                            {
                                ab2.setItems(R.array.bangalaloresubcities, new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        donotUpdateCitySubcity(3, 12 + which+1);
                                    }
                                });
                            }
                            if (which == 3)
                            {
                                ab2.setItems(R.array.delhisubcities, new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Log.d("subcity_msg", String.valueOf(which));
                                        // every city has 6 sub cities and starting with Mumbai, Delhi, Hyderabad, Bangalore & Chennai
                                        donotUpdateCitySubcity(4, 18 + which+1);
                                    }

                                });
                            }
                            if (which == 4)
                            {
                                ab2.setItems(R.array.chennaisubcities, new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        donotUpdateCitySubcity(5, 24 + which+1);
                                    }
                                });
                            }

                            ab2.show();

                        }
                    });

                    ab1.show();

                }
                else if(position == 2)
                {

                    // Deactivate the account
                    AlertDialog.Builder ab1 = new AlertDialog.Builder(SettingsActivity.this);
                    ab1.setTitle("Delete Account");
                    ab1.setMessage("Are you sure you want to delete account. All your records will be deleted");
                    ab1.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Delete Records
                            SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                            String session_id = sh_pref.getString("userSessionId", "fail");
                            Log.d(" delete user got session id", session_id);
                            String url_pass ="http://donatebloodindia.in/api/donor_deleteuser.php?&SessionId="+session_id;
                            String params[] = { url_pass };
                            new deleterecords().execute(params);

                        }
                    });
                    ab1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO Auto-generated method stub

                        }
                    });
                    ab1.show();
                }
                else if(position == 3)
                {
                    // get the Notifications status of the user
                    // User to change notification status
                    // Change user notification

                    SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                    String userNotificationStatus = sh_pref.getString("userStatus", "fail");
                    userSessionId = sh_pref.getString("userSessionId", "fail");

                    Log.d(" user notification status ", userNotificationStatus);

                    // Display the notification status to user
                    AlertDialog.Builder  adb = new AlertDialog.Builder(SettingsActivity.this);
                    adb.setTitle("Notification");
                    if(userNotificationStatus.equalsIgnoreCase("InActive"))
                    {
                        adb.setMessage("Your Notifications are Disabled. ");
                        adb.setPositiveButton("Enable Notifications",  new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which)
                            {
                                String string_url = "http://donatebloodindia.in/api/change_notifications.php?Status=InActive&SessionId="+userSessionId;
                                String params_notification[]= {string_url ,"InActive"};
                                new changeNotifications().execute(params_notification);

                            }
                        });
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });

                    }
                    else if(userNotificationStatus.equalsIgnoreCase("Active"))
                    {
                        adb.setMessage("Your Notifications are Enabled. ");
                        adb.setPositiveButton("Disable Notifications",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String string_url = "http://donatebloodindia.in/api/change_notifications.php?Status=Active&SessionId="+userSessionId;
                                String params_notification[]= {string_url ,"Active"};
                                new changeNotifications().execute(params_notification);
                            }
                        });
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // TODO Auto-generated method stub

                            }
                        });

                    }
                    adb.show();

                }
                else if(position == 4)
                {

                    FragmentManager fm =  getFragmentManager();
                    SettingsUpdateEmailidFragment  scpf = new SettingsUpdateEmailidFragment();
                    scpf.show(fm, "test");

                }
            }
        });

    }

    public void respond(String p, String p1 )
    {
        if(p.contains("fail"))
        {

            AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setTitle("Password Mismatch");
            alertDialog.setMessage("Mismatch in Passwords or Password to be have min 6 characters long");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            FragmentManager fm =  getFragmentManager();
                            SettingsChangePasswordFragment  scpf = new SettingsChangePasswordFragment();
                            scpf.show(fm, "test");
                        }
                    });
            alertDialog.show();
        }
        else if( p.contains("success"))
        {

            // Password entered are equal and have minimum password requirements
            SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
            String session_id = sh_pref.getString("userSessionId", "fail");
            Log.d("got session id", session_id);
            String url_pass ="http://donatebloodindia.in/api/donor_password_update.php?Password="+p1+"&SessionId="+session_id;
            String params[] = { url_pass };
            new changepasswordnow().execute(params);

        }

    }


    public void email_respond(String em, String emailid  )
    {
        if(em.contains("emailfail"))
        {

            AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setTitle("Incorrect Email");
            alertDialog.setMessage("Please provide correct Email id");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            FragmentManager fm =  getFragmentManager();
                            SettingsUpdateEmailidFragment  scpf = new SettingsUpdateEmailidFragment();
                            scpf.show(fm, "test");
                        }
                    });
            alertDialog.show();
        }
        else if ( em.contains("emailsuccess") && !emailid.isEmpty())
        {
            // Password entered are equal and have minimum password requirements
            SharedPreferences sh_pref = getApplicationContext().getSharedPreferences("myregistration", Context.MODE_PRIVATE);
            String session_id = sh_pref.getString("userSessionId", "fail");

            String str_email = "http://donatebloodindia.in/api/donor_email_update.php?Emailid="+emailid+"+&SessionId="+session_id;
            String[] params= {str_email};
            new changeemailnow().execute(params);

        }

    }


    public class changepasswordnow extends AsyncTask<String, Void , String>
    {
        public AlertDialog.Builder alert1;
        protected void onPreExecute()
        {
            //progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("Changing Password");
            progress.show();
        }
        protected String doInBackground(String... params)
        {

            String url_pass = params[0];
            InputStream is=null;
            char[] buffer = new char[2000];
            //String url_response = null;
            try
            {
                URL url_changepassword = new URL(url_pass);

                HttpURLConnection  connection =  (HttpURLConnection) url_changepassword.openConnection();
                connection.connect();
                Log.d("changepassword", "connection established");

                //Get input stream
                is = connection.getInputStream();
                Reader reader = null;
                reader = new InputStreamReader(is, "UTF-8");
                reader.read(buffer);

                Log.d("ChangePasswordActivity", "changepasswordjsonresponse" + String.valueOf(buffer).trim());
                return String.valueOf(buffer).trim();


            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String  response)
        {
            progress.dismiss();

            JSONObject jsonobj1, jsonobj2;
            try
            {
                jsonobj1 = new JSONObject(response);
                jsonobj2 = jsonobj1.getJSONObject("Donor Update");
                String s2  = jsonobj2.getString("status");

                if(s2.equals("fail"))
                {

                    Toast.makeText(getApplicationContext(), "There seems to change,  please try again", Toast.LENGTH_LONG).show();
                }
                else if ( s2.equals("success"))
                {

                    AlertDialog.Builder ad_dialog = new AlertDialog.Builder(SettingsActivity.this);
                    ad_dialog.setTitle("Alert");
                    ad_dialog.setMessage("Your city has been changed. You will be logged out . Please login");
                    ad_dialog.setPositiveButton("Logout", new AlertDialog.OnClickListener()
                    {

                        public void onClick(DialogInterface dialog, int which)
                        {
                            // On pressing ok, Earlier screen is called
                            logoutprocess();
                            finish();
                        }
                    });
                    ad_dialog.show();

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class changecitysubcitynow extends AsyncTask<String, Void, String>
    {

        public AlertDialog.Builder alert1;
        protected void onPreExecute()
        {
            //progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("Changing City and Subcity");
            progress.show();
        }

        protected String doInBackground(String... params)
        {

            URL url; InputStream is; Reader reader;
            char[] responseBuffer = new char[2000];

            try
            {
                url = new URL(params[0]);
                HttpURLConnection  connection =  (HttpURLConnection) url.openConnection();
                is = connection.getInputStream();
                reader = new InputStreamReader(is,"UTF-8");
                reader.read(responseBuffer);
                Log.d("changesubcityresponse", String.valueOf(responseBuffer));
                return String.valueOf(responseBuffer);

            }
            catch (MalformedURLException e)
            { e.printStackTrace();   }
            catch (IOException e)
            {	e.printStackTrace(); }
            return null;

        }
        protected void onPostExecute(String response)
        {
            progress.dismiss();
            JSONObject jsonobj1, jsonobj2;
            try
            {
                jsonobj1 = new JSONObject(response);
                jsonobj2 = jsonobj1.getJSONObject("Donor Update");
                String s2  = jsonobj2.getString("status");

                if(s2.equals("failed"))
                {

                    Toast.makeText(getApplicationContext(), "There seems to be some problem,  please try again", Toast.LENGTH_LONG).show();
                }
                else if ( s2.equals("success"))
                {
//					alertMessage = new AlertDialog.Builder(context);
//					alertMessage.setMessage("Logout");
//					AlertDialog ad = alertMessage.create();
//					ad.show();

                    Toast.makeText(getApplicationContext(), "City and Subcity  change successful. Please login again", Toast.LENGTH_LONG).show();
                    logoutprocess();
                }

            } catch (JSONException e)
            {	e.printStackTrace(); }
        }

    }



    // Asynctask class to update email id
    public class changeemailnow extends AsyncTask<String, Void, String>
    {

        public AlertDialog.Builder alert1;
        protected void onPreExecute()
        {
            //progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("Updating Email id");
            progress.show();
        }

        protected String doInBackground(String... params)
        {
            //

            URL url; InputStream is; Reader reader;
            char[] responseBuffer = new char[2000];

            try
            {
                url = new URL(params[0]);
                HttpURLConnection  connection =  (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);

                if( connection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                    is = connection.getInputStream();
                    reader = new InputStreamReader(is, "UTF-8");
                    reader.read(responseBuffer);
                    Log.d(TAG, "changeemailresponse " + String.valueOf(responseBuffer));
                    return String.valueOf(responseBuffer);
                }
                else
                    return "Httpconnectionfailure"; // if HttpURLConnection response code is not OK.
                    // send this message to post execute

            }
            catch (MalformedURLException e)
            { e.printStackTrace();   } catch (SocketTimeoutException e)
            {   e.printStackTrace();  } catch (IOException e)
            {	e.printStackTrace(); }
            return "Httpconnectionfailure";

        }
        protected void onPostExecute(String response)
        {
            progress.dismiss();
            JSONObject jsonobj1, jsonobj2;
            try
            {

                if(response.contains("Httpconnectionfailure"))
                {
                    Toast.makeText(getApplicationContext(), "There seems to change,  please try again", Toast.LENGTH_LONG).show();
                }
                else
                {
                    jsonobj1 = new JSONObject(response);
                    jsonobj2 = jsonobj1.getJSONObject("Donor Update");
                    String s2 = jsonobj2.getString("status");

                    if (s2.equals("fail")) {
                        Toast.makeText(getApplicationContext(), "There seems to change,  please try again", Toast.LENGTH_LONG).show();
                    } else if (s2.equals("success")) {
//					alertMessage = new AlertDialog.Builder(context);
//					alertMessage.setMessage("Logout");
//					AlertDialog ad = alertMessage.create();
//					ad.show();

                        Toast.makeText(getApplicationContext(), "Emailid updation successful.", Toast.LENGTH_LONG).show();
                        //logoutprocess();
                    }
                }

            } catch (JSONException e)
            {	e.printStackTrace(); }
        }

    }



    //Asynctask to delete records in database
    public class deleterecords extends AsyncTask<String, Void, String>
    {

        public AlertDialog.Builder alert1;
        protected void onPreExecute()
        {
            //progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("Deleting your Account");
            progress.show();
        }

        protected String doInBackground(String... params)
        {

            URL url; InputStream is; Reader reader;
            char[] responseBuffer = new char[2000];

            try
            {
                url = new URL(params[0]);

                HttpURLConnection  connection =  (HttpURLConnection) url.openConnection();
                is = connection.getInputStream();
                reader = new InputStreamReader(is,"UTF-8");
                reader.read(responseBuffer);
                Log.d("deleterecords", String.valueOf(responseBuffer));
                return String.valueOf(responseBuffer);


            } catch (MalformedURLException e)
            {	e.printStackTrace(); }
            catch (IOException e)
            {	e.printStackTrace(); }
            return null;
        }

        protected void onPostExecute(String response)
        {
            progress.dismiss();
            JSONObject jsonobj1, jsonobj2;
            try
            {
                jsonobj1 = new JSONObject(response);
                jsonobj2 = jsonobj1.getJSONObject("Donor Update");
                String s2  = jsonobj2.getString("status");

                if(s2.equals("fail"))
                {
                    Toast.makeText(getApplicationContext(), "Could not delete,  please try again", Toast.LENGTH_LONG).show();
                }
                else if ( s2.equals("success"))
                {
                    AlertDialog.Builder ad_dialog = new AlertDialog.Builder(SettingsActivity.this);
                    ad_dialog.setTitle("Delert Alert");
                    ad_dialog.setMessage("Your Account has bee succesfully Deleted. Thank You for using Blood App");
                    ad_dialog.setPositiveButton("OK", new AlertDialog.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // On pressing ok, Earlier screen is called
                            logoutprocess();
                            finish();
                        }
                    });
                    ad_dialog.show();
                }

            } catch (JSONException e)
            {	e.printStackTrace(); }
        }
    }

    public class changeNotifications extends AsyncTask<String, Void, String[]>
    {
        protected void  onPreExecute()
        {
            progress.setMessage("Changing your Notifications status");
            progress.show();
        }
        protected String[] doInBackground( String... params)
        {

            URL url; InputStream is; Reader reader;
            char[] responseBuffer = new char[2000];

            try
            {
                url = new URL(params[0]);
                HttpURLConnection  connection =  (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(4000);
                is = connection.getInputStream();

                reader = new InputStreamReader(is,"UTF-8");
                reader.read(responseBuffer);
                Log.d("change notification", String.valueOf(responseBuffer));

                String[] response= { String.valueOf(responseBuffer), params[1] };
                return response;

            }
            catch (MalformedURLException e)
            { e.printStackTrace();   }
            catch (SocketTimeoutException e)
            {
                Log.d(TAG,"TIMEOUT");
                e.printStackTrace();
            }
            catch (IOException e)
            {	e.printStackTrace(); }
            return null;

        }
        protected  void onPostExecute(String result[])
        {
            progress.dismiss();

            JSONObject jsonobj1, jsonobj2;
            try
            {
                jsonobj1 = new JSONObject(result[0]);
                jsonobj2 = jsonobj1.getJSONObject("Donor Notification status Update");
                String s2  = jsonobj2.getString("status");

                if(s2.equals("fail"))
                {
                    Toast.makeText(getApplicationContext(), "Could not Change notifications settings,  please try again", Toast.LENGTH_LONG).show();
                }
                else if ( s2.equals("success"))
                {
                    AlertDialog.Builder ad_dialog = new AlertDialog.Builder(SettingsActivity.this);
                    ad_dialog.setTitle("Notification Alert");
                    if(result[1].equalsIgnoreCase("Active"))
                    {
                        ad_dialog.setMessage("Now, Notifications from Blood App has been disabled. ");
                        SharedPreferences shared_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                        SharedPreferences.Editor  edi_tor =  shared_pref.edit();
                        edi_tor.putString("userStatus", "InActive");
                        edi_tor.commit();

                    }
                    else if(result[1].equalsIgnoreCase("InActive"))
                    {
                        ad_dialog.setMessage("Notifications from Blood App has been Enabled. You will recieve Notifications from blood app ");
                        SharedPreferences shared_pref = getSharedPreferences("myregistration", Context.MODE_PRIVATE);
                        SharedPreferences.Editor  edi_tor =  shared_pref.edit();
                        edi_tor.putString("userStatus", "Active");
                        edi_tor.commit();
                    }

                    ad_dialog.setPositiveButton("OK", new AlertDialog.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // On pressing ok, Earlier screen is called
                            finish();
                        }
                    });
                    ad_dialog.show();
                }

            } catch (JSONException e)
            {	e.printStackTrace(); }

        }
    }
}

interface communicator
{
    public void respond(String p , String p1);

    public void email_respond(String emailstatus, String emailid);
}

