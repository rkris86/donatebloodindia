package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.ActionBar;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyProfileListViewDisplay extends ArrayAdapter<String>
{

    private Context contxt;
    private String city[], location[], bloodbank[], date[] ,platelets[] , hemoglobin[];

    public MyProfileListViewDisplay(Context context , String[] bloodbank1, String[] city1, String[] location1, String[] date1, String[] platelets1, String[] hemoglobin1)
    {

        // Make sure that at least one parameter is sent in the super call . eg: bloodbank1 is being sent

        super(context, R.layout.layout_myprofile_list , bloodbank1);
        this.contxt = context;
        this.bloodbank = bloodbank1;
        this.city = city1;
        this.location = location1;
        this.date = date1;
        this.platelets=platelets1;
        this.hemoglobin= hemoglobin1;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        // for Optimization
        View rowview = convertView;
        if ( rowview == null)
        {
            // Inflating the layout with layout_myprofile_list where it contains buttons, textview are put in a single item in list.
            LayoutInflater inflater =   (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            rowview = inflater.inflate(R.layout.layout_myprofile_list, parent, false);
        }

        TextView bl_bank_txtview = (TextView) rowview.findViewById(R.id.myprofileBloodBanktextview);
        bl_bank_txtview.setText(this.bloodbank[position]);

        TextView city_txtview = (TextView) rowview.findViewById(R.id.myprofileCitytextview);
        city_txtview.setText(this.city[position]);

        TextView date_txtview = (TextView) rowview.findViewById(R.id.myprofileDatetextview);
        date_txtview.setText(this.date[position]);

        TextView location_txtview = (TextView) rowview.findViewById(R.id.myprofileLocationtextview);
        location_txtview.setText(this.location[position]);

        TextView plalelets_txtview = (TextView) rowview.findViewById(R.id.myprofilePlaleletstextview);
        plalelets_txtview.setText(this.platelets[position]);

        TextView hemoglobin_txtview = (TextView) rowview.findViewById(R.id.myprofileHemoglobintextview);
        hemoglobin_txtview.setText(this.hemoglobin[position]);

        return rowview;

    }
}
