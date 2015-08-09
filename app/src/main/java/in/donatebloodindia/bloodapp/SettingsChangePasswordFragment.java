package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.app.Fragment;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;

public class SettingsChangePasswordFragment extends DialogFragment
{
    /*public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_settings_changepassword, null);
        return view;
    } */
    communicator comm;

    public Dialog onCreateDialog(Bundle SavedInstanceState)
    {
        AlertDialog.Builder  ad1 = new AlertDialog.Builder(getActivity());

        LayoutInflater li = getActivity().getLayoutInflater();
        View view = li.inflate(R.layout.fragment_settings_changepassword, null);
        ad1.setView(view);
        ad1.setTitle("Change Password");


        //ed1 = (EditText) getActivity().findViewById(R.id.settings_changepassword_fragment_textinput1);
        ad1.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which)
            {
                Dialog df = (Dialog) dialog;
                Log.d("settings" , "change mobile number. yesss" );
                comm = (communicator)getActivity();
                EditText ed_password1, ed_password2;
                ed_password1  =   (EditText) df.findViewById(R.id.settings_changepassword_fragment_textinput1);
                ed_password2  =   (EditText) df.findViewById(R.id.settings_changepassword_fragment_textinput2);

                // check whether password1 and password2 are same or not
                if ((ed_password1.getText().toString().equals(ed_password2.getText().toString()))   &&  (ed_password1.getText().toString().length() > 5))
                {
                    // respond with success message to activity
                    comm.respond("success" , ed_password1.getText().toString() );

                }
                else
                {
                    //respond with fail so that password can be changes
                    comm.respond("fail" , "fail");

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


