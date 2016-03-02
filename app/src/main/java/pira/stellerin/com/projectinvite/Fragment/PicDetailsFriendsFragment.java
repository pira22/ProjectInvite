package pira.stellerin.com.projectinvite.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.squareup.picasso.Picasso;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicDetailsFriendsFragment  extends Fragment {


    public PicDetailsFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic_details_friends, container, false);
    }


        public static Bundle bundle;
        TextView share;
        String url;
        ImageView imageView;
        CircularProgressButton add;

    public static PicDetailsFriendsFragment newInstance(Bundle arg) {
        PicDetailsFriendsFragment detailPicFragment = new PicDetailsFriendsFragment();
        detailPicFragment.setArguments(arg);
        return detailPicFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        url = bundle.getString("url");
        Log.d("url",url);
        ActivityContainer.DETECT ="detailprofilfriends";
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        share = (TextView) getActivity().findViewById((R.id.textsharedetail));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share image via..."));
            }
        });

        imageView = (ImageView) getActivity().findViewById(R.id.imagedetail);
        imageView.setDrawingCacheEnabled(true);
        imageView.setVisibility(View.VISIBLE);
        Log.d("urlpicasso",url);
        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}


