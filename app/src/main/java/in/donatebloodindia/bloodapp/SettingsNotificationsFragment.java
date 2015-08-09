package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SettingsNotificationsFragment extends DialogFragment
{

    public Dialog onCreateDialog( Bundle SavedInstanceState)
    {

        AlertDialog.Builder adbuild = new AlertDialog.Builder(getActivity());
        LayoutInflater li = getActivity().getLayoutInflater();
        View view = li.inflate(R.layout.fragment_settings_notifications, null);

        adbuild.setTitle("Notifications");
        adbuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        return adbuild.create();


    }

}
