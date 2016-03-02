package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.NotifAdapter;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    ListView listView;
    NotifAdapter  notifAdapter;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        ActivityContainer.DETECT ="notiffragment";
        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)activity).getSupportActionBar().setTitle(R.string.notif);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView =(ListView) view.findViewById(R.id.listView2);
        ParseQuery parseQuery = ParseQuery.getQuery("Notification");
        parseQuery.include("program.user");
        parseQuery.include("program");
        parseQuery.include("user");
        parseQuery.whereEqualTo("confirm", false);
        parseQuery.whereEqualTo("refus",false);
        parseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                try {
                    if (e != null)
                        Log.d("erreur_notification", e.getMessage());
                    else {
                        Log.d("size list", list.size() + "");
                        notifAdapter = new NotifAdapter(getActivity(), R.layout.item_notif, list);
                        listView.setAdapter(notifAdapter);
                    }
                } catch (NullPointerException e1) {

                }

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try{
            menu.findItem(161).setVisible(false);
            menu.findItem(2).setVisible(false);

        }catch (NullPointerException e){

        }


    }




}
