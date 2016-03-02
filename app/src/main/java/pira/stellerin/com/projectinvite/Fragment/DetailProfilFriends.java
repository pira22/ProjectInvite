package pira.stellerin.com.projectinvite.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailProfilFriends extends Fragment {


    SupportMapFragment mapFragment;
    private static View view;
    TextView titre, description, date;
    double lat, lon;
    String titree,descriptionn,datetxt;
    public DetailProfilFriends() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        bundle.getString("objectId");
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
            view = inflater.inflate(R.layout.fragment_detail_profil_friends, container, false);
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
