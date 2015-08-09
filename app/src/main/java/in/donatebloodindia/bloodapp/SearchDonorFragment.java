package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class SearchDonorFragment extends Fragment implements OnClickListener
{

    Button button;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View view_f =  inflater.inflate(R.layout.fragment_search_donor, container, false);


        final Spinner  tl = (Spinner) view_f.findViewById(R.id.search_donor_city_spinner);
        final Spinner  tl2 = (Spinner) view_f.findViewById(R.id.search_donor_blood_group_spinner);
        final Button submit = (Button) view_f.findViewById(R.id.search_donor_submit);
        submit.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "lost serach"+  String.valueOf(tl.getSelectedItem()), Toast.LENGTH_LONG).show();

                Intent intent = new Intent( getActivity().getApplicationContext(), ActivitySearchDonorResults.class);
                intent.putExtra("city", String.valueOf(tl.getSelectedItem()));
                intent.putExtra("bloodgroup", String.valueOf(tl2.getSelectedItem()));
                startActivity(intent);
                //Search
            }
        });
        return view_f;

    }

    @Override
    public void onClick(View v)
    {

        // TODO Auto-generated method stub

    }




}
