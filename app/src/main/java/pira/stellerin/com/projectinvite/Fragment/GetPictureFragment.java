package pira.stellerin.com.projectinvite.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.dd.CircularProgressButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.GetPicAdapter;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;

/**
 * Created by Pira on 28/08/2015.
 */
public class GetPictureFragment extends Fragment {
    private static final int INTENT_REQUEST_IMAGES = 25;
    Button btn,add;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    private ViewGroup mSelectedImagesContainer;
    Bitmap bitmap;
    List<byte[]> listByte;
    List<Uri> uriList;
    String objectId ;
    Boolean activity = false ;
    Uri uri;
    GetPicAdapter adapter;
    GridView gridView;
    Bundle bundle;
    ProgressWheel progressWheel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityContainer.DETECT ="";
        RootProgrammeFragment.menuitem ="zdzd";
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        objectId=bundle.getString("objectId");

        if(!activity){
            Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
            Config config = new Config.Builder()
                    .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                    .setTabSelectionIndicatorColor(R.color.blue)
                    .setCameraButtonColor(R.color.green)
                    //.setSelectionLimit(1)    // set photo selection limit. Default unlimited selection.
                    .build();
            ImagePickerActivity.setConfig(config);
            startActivityForResult(intent, INTENT_REQUEST_IMAGES);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RootProgrammeFragment.menuitem = "dz";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.get_pic,container,false);
        progressWheel = (ProgressWheel)view.findViewById(R.id.progresscercle);
        progressWheel.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSelectedImagesContainer = (ViewGroup) getActivity().findViewById(R.id.selected_photos_container);
        gridView = (GridView) getActivity().findViewById(R.id.grid);
        add =(Button) getActivity().findViewById(R.id.button3);
        add.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SavePic().execute();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try {
            menu.findItem(101).setVisible(false);
            menu.findItem(150).setVisible(false);
        }catch (NullPointerException e){

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:


              MainDetailProfilFragment mainDetailProgramme = new MainDetailProfilFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                mainDetailProgramme.setArguments(bundle);
                fragmentTransaction.replace(R.id.root_profil, mainDetailProgramme, "profildetailmain");
                fragmentTransaction.commit();

                return true;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        activity = true;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_IMAGES) {
                listByte = new ArrayList<byte[]>();
                uriList = new ArrayList<Uri>();
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                if (uris != null) {
                    for (Uri urii : uris) {
                        Log.i("uri picture", " uri: " + urii);

                        mMedia.add(urii);
                    }

                  new  GetPic().execute();


                }

            }
        }
    }

    private void showMedia() {
        Iterator<Uri> iterator = mMedia.iterator();
        while (iterator.hasNext()) {
            uri = iterator.next();


            if (!uri.toString().contains("content://")) {
                // probably a relative uri
                uri = Uri.fromFile(new File(uri.toString()));
            }
            uriList.add(uri);
            Log.d("Uri File", uri.toString());
            Shared.listPicProfile.add(uri+"");
            InputStream image_stream = null;

            try {
                image_stream = getActivity().getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            listByte.add(byteArray);
        }
    }

    public class GetPic extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressWheel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            showMedia();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                adapter = new GetPicAdapter(getActivity(), R.layout.item_galerie, uriList);
            }
            catch(NullPointerException e){
                Log.d("Adapter","vide");
            }
            progressWheel.setVisibility(View.GONE);
            gridView.setAdapter(adapter);
            add.setVisibility(View.VISIBLE);
        }
    }


    public class SavePic extends AsyncTask<Void,Integer,Void>{
        Integer a =0;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("values progress", values + "");
            Log.d("values test", "pira" +values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(int i =0;i<listByte.size();i++) {
                ParseFile data = new ParseFile("file.jpg", listByte.get(i));
                data.saveInBackground(new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {
                       a += 100/listByte.size();
                        onProgressUpdate(a);
                    }
                });
                ParseObject programme = ParseObject.createWithoutData("Programmes", objectId);
                ParseObject gallery = new ParseObject("Gallery");
                gallery.put("picture", data);
                gallery.put("programme", programme);
                gallery.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("parse.com", "saved");
                        } else {
                            Log.d("parse.com", e.getMessage());
                        }
                    }
                });



            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getActivity().onBackPressed();
        }
    }


}
