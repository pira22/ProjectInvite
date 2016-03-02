package pira.stellerin.com.projectinvite.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import pira.stellerin.com.projectinvite.R;

/**
 * Created by Pira on 12/09/2015.
 */
public class GalerieAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    List<String> url = null;
    public GalerieAdapter(Context context, int layoutResourceId,
                                        List<String> url) {
        super(context, layoutResourceId, url);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.url = url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserHolder();

            holder.image = (ImageView) row
                    .findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        String urlfile = url.get(position).toString();
        try {
            Glide.with(context).load(urlfile).override(300, 300).into(holder.image);
        } catch (IllegalArgumentException e) {

        }


        return row;
    }

    @Override
    public int getPosition(String item) {
        return super.getPosition(item);
    }

    static class UserHolder {
        ImageView image;
    }
}
