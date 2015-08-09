package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class SearchActivity extends Activity implements ActionBar.TabListener
{

    public ActionBar actionBar;
    public static final String TAG="SearchActivity" ;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actionBar = getActionBar();

        //Adding back button enabled as Up button
        actionBar.setHomeButtonEnabled(false);

        // Adding tabs to the action bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab bloodbankTab = actionBar.newTab().setText("Donor");
        ActionBar.Tab donorTab = actionBar.newTab().setText("Bloodbank");

        // change the color of the action bar or default color would be black
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb( 1, 6, 36)));
        getActionBar().setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);


        //add event listener to the action tabs
        actionBar.addTab(donorTab.setTabListener(this));
        actionBar.addTab(bloodbankTab.setTabListener(this));
        Log.d(TAG, "SearchActivity: Action bar created");



    }
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
        //  Get FragmentManager and FragmentTransaction and add fragementlayout to UI
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();


        Log.d(TAG, "in tab selected" + String.valueOf(tab.getPosition()));
        // Select from tab selected.
        // add appropriate fragmentlayout to the parent activity activity_search_id

        if (tab.getPosition()==0)
        {
            fragmentTransaction.add(R.id.activity_search_id, new SearchBloodbankFragment(), "donor_tag_frag");
        }
        else
        {
            fragmentTransaction.add(R.id.activity_search_id, new SearchDonorFragment(), "bank_tag_frag");
        }
        fragmentTransaction.commit();

    }
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
}
