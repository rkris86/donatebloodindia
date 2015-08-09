package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ShowDialog  extends DialogFragment
{

    public int  response_code;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        response_code = getArguments().getInt("response_code");

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
        Log.e( "krishna","response_code_alertdialog"+response_code);

        //Based on response code, appropriate message will be shown.
        // if response_code = -1 , then fields are empty
        bd.setTitle("Alert Message");

        if(response_code == -1)
            bd.setMessage("Please fill up all  First Name, Last Name , Mobile number details");
        else if ( response_code == 1)
            bd.setMessage("Please enter valid first name");
        else if ( response_code == 2)
            bd.setMessage("Please enter valid last name");
        else if (response_code == 3)
            bd.setMessage("Please enter valid last & first name");
        else if(  response_code == 4)
            bd.setMessage("Please enter valid 10 digit mobile number");
        else if (response_code==7)
            bd.setMessage("Please enter valid first name, last name , mobile number");



        bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });


        Dialog dialog = bd.create();
        return dialog;
    }

}
