package pira.stellerin.com.projectinvite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pira.stellerin.com.projectinvite.Entity.PlacesAuto;
import pira.stellerin.com.projectinvite.R;

/**
 * Created by Pira on 27/08/2015.
 */
public class PlaceAutocompleteAdapter extends ArrayAdapter<PlacesAuto> {
    Context context;
    int layoutResourceId;
    List<PlacesAuto> tag = null;
    String nameplace;

    public PlaceAutocompleteAdapter(Context context, int layoutResourceId,
                                    List<PlacesAuto> tags) {
        super(context, layoutResourceId, tags);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.tag = tags;


    }

    @Override
    public String toString() {
        return nameplace;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.txtName = (TextView) row.findViewById(R.id.textView);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }

        PlacesAuto places = tag.get(position);
        nameplace =places.getDescription();
        holder.txtName.setText(places.getDescription());
        return row;
    }

    static class UserHolder {

        TextView txtName;
    }
}
