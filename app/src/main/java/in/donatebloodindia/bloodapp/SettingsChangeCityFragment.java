package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class SettingsChangeCityFragment extends DialogFragment
{

    public Dialog OnCreateDialog(Bundle SavedInstanceState)
    {
        AlertDialog.Builder ab1 = new AlertDialog.Builder(getActivity());
        ab1.setTitle("Change city");

        CharSequence[] cs = {"mumbai", "adfd" ,"adf"};
        ab1.setItems(cs, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub

            }
        });

        return ab1.create();
    }

}
