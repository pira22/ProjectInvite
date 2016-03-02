package pira.stellerin.com.projectinvite.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import pira.stellerin.com.projectinvite.Adapter.AdapterCircleGrid;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailProfilFragment extends Fragment {

    SupportMapFragment mapFragment;
    private static View view;
    TextView titre, description, date;
    double lat, lon;
    String titree,descriptionn,datetxt,objectId;
    GridView gridView;
    AdapterCircleGrid adapterCircleGrid;
    CardView cardView;
    public DetailProfilFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        objectId=bundle.getString("objectId");
        titree = bundle.getString("titre");
        descriptionn = bundle.getString("Descritpion");
        lon = bundle.getDouble("longitude");
        lat = bundle.getDouble("latitude");
        datetxt = bundle.getString("date");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_detail_profil, container, false);
        } catch (InflateException e) {
			/* map is already there, just return view as it is */
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                titre = (TextView) getView().findViewById(R.id.texttitre);
                date = (TextView) getView().findViewById(R.id.textDatep);
                description = (TextView) getView().findViewById(R.id.textdes);
                SetMap();
                date.setText(datetxt);
                titre.setText(titree);
                description.setText(descriptionn);
                gridView = (GridView) getView().findViewById(R.id.gridcercle);
                cardView =(CardView) getView().findViewById(R.id.cardsgrid);
                cardView.setVisibility(View.GONE);
            }
        });

        ParseQuery parseQuery = ParseQuery.getQuery("Programmes");
        parseQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                ParseRelation parseRelation = parseObject.getRelation("friendaccept");
                parseRelation.getQuery().include("user").whereEqualTo("confirm",true).findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {

                        try {
                            if (e != null)
                                Log.d("erreur_notification", e.getMessage());
                            else {
                                if(list.size()==0){

                                }else {
                                    cardView.setVisibility(View.VISIBLE);
                                    adapterCircleGrid = new AdapterCircleGrid(getActivity(), R.layout.item_friendcercle, list);
                                    gridView.setAdapter(adapterCircleGrid);
                                }
                            }
                        } catch (NullPointerException e1) {

                        }


                    }


                });
            }

        });


    }

    public void SetMap(){
        if (mapFragment == null && getActivity() != null
                && getActivity().getSupportFragmentManager() != null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
                    R.id.map_fragment_detail);
            if (mapFragment != null) {

                mapFragment.getMap().animateCamera(
                        CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),
                                12.0f));
                mapFragment.getMap().getUiSettings()
                        .setAllGesturesEnabled(false);
                mapFragment.getMap().getUiSettings()
                        .setZoomControlsEnabled(false);
                mapFragment.getMap().getUiSettings()
                        .setMyLocationButtonEnabled(false);
                mapFragment.getMap().addMarker(
                        new MarkerOptions().position(new LatLng(lat, lon))
                                .title(titree));


            }
        }
    }


}
