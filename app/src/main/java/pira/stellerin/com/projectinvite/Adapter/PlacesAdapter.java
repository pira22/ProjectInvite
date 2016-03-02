package pira.stellerin.com.projectinvite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import pira.stellerin.com.projectinvite.Entity.Place;
import pira.stellerin.com.projectinvite.R;

public class PlacesAdapter extends ArrayAdapter<Place> {
	 Context context; 
	    int layoutResourceId;    
	    List<Place> place = null;
	    
	    public PlacesAdapter(Context context, int layoutResourceId, List<Place> places) {
	        super(context, layoutResourceId, places);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.place = places;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        UserHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new UserHolder();
	            holder.txtName = (TextView)row.findViewById(R.id.name_place);
	            holder.image = (ImageView)row.findViewById(R.id.list_image);
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (UserHolder)row.getTag();
	        }
	        
	        Place places = place.get(position);
	        holder.txtName.setText(places.getName());
			Picasso.with(context)
					.load(places.getIcon())
					.placeholder(R.drawable.loading_spinner)
					.into(holder.image);

	       
	        return row;
	    }
	    
	    static class UserHolder
	    {
	        
	        TextView txtName;
	        ImageView image;
	    }
}
