package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class SearchBloodbankFragment extends Fragment
{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view_f =  inflater.inflate(R.layout.fragment_search_bloodbank, container, false);

        final Spinner  tl = (Spinner) view_f.findViewById(R.id.search_bloodbank_city_spinner);
        final Button submit = (Button) view_f.findViewById(R.id.search_bloodbank_button_submit);
        submit.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "in search "+  String.valueOf(tl.getSelectedItem()), Toast.LENGTH_LONG).show();

                //Search  Blood banks
                Intent intent  = new Intent (getActivity().getApplicationContext(), ActivitySearchBloodBankResults.class);
                intent.putExtra("selected_city", String.valueOf( tl.getSelectedItem()));
                startActivity(intent);

            }
        });
        return view_f;
    }

}
