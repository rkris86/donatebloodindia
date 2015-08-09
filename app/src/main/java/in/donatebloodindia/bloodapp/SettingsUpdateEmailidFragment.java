package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SettingsUpdateEmailidFragment extends DialogFragment
{
    communicator comm2;
    public static final String TAG= "SettingsUpdateEmailidFragment";

    public Dialog onCreateDialog(Bundle SavedInstanceState)
    {
        AlertDialog.Builder  ad1 = new AlertDialog.Builder(getActivity());

        LayoutInflater li = getActivity().getLayoutInflater();
        View view = li.inflate(R.layout.fragment_settings_update_emailid, null);
        ad1.setView(view);
        ad1.setTitle("Update Email");

        ad1.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which)
            {
                Dialog df = (Dialog) dialog;
                comm2 = (communicator)getActivity();
                EditText ed_emailid ;

                ed_emailid  =   (EditText) df.findViewById(R.id.settings_update_emailid_fragment_textinput);

                if (TextUtils.isEmpty(ed_emailid.getText().toString().trim()) )
                {
                    Log.d(TAG, "email is null");
                    comm2.email_respond("emailfail","emailfail");
                }
                else if (android.util.Patterns.EMAIL_ADDRESS.matcher(ed_emailid.getText().toString().trim()).matches())
                {
                    Log.d("email is valid",  ed_emailid.getText().toString().trim());
                    comm2.email_respond("emailsuccess", ed_emailid.getText().toString().trim());

                }
                else
                {
                    Log.d(TAG, "email is invalid"+  ed_emailid.getText().toString().trim());
                    comm2.email_respond("emailfail","emailfail");
                    ed_emailid.setError("enter valid email address");
                }
            }
        });

        ad1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
            }
        });
        Dialog dlg =  ad1.create();

        return dlg;

    }

    public void okclicked()
    {


    }
}
