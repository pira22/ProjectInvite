package pira.stellerin.com.projectinvite.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPicFragment extends Fragment {
    public static Bundle bundle;
    TextView share;
    String url;
    ImageView imageView;
    CircularProgressButton add;
    String objectId;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    public static DetailPicFragment newInstance(Bundle arg) {
        DetailPicFragment detailPicFragment = new DetailPicFragment();
        detailPicFragment.setArguments(arg);
        return detailPicFragment;
    }

    public DetailPicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RootProgrammeFragment.menuitem = "ee";
        setHasOptionsMenu(true);
        bundle = getArguments();
        url = bundle.getString("url");
        ActivityContainer.DETECT = "detail";
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        share = (TextView) getActivity().findViewById((R.id.textshare));
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

        imageView = (ImageView) getActivity().findViewById(R.id.image);
        imageView.setDrawingCacheEnabled(true);
        imageView.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return true;
    }
}
