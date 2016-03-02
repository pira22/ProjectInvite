package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import pira.stellerin.com.projectinvite.Adapter.ProgramFriendAdapter;
import pira.stellerin.com.projectinvite.Adapter.UserfriendsAdapter;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramfriendInvit extends Fragment {

    ListView list;
    ProgramFriendAdapter adapter;
    Bundle bundle;
    String programId;
    public ProgramfriendInvit() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bundle = getArguments();
        programId = bundle.getString("objectId");
        ((AppCompatActivity)activity).getSupportActionBar().setTitle(R.string.addFriends);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programfriend_invit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (ListView) getView().findViewById(
                R.id.listinvitfriend);
        ParseObject programme = ParseObject.createWithoutData("Programmes",programId);
        ParseQuery parsenotification = ParseQuery.getQuery("Notification");
        parsenotification.include("user");
        parsenotification.whereEqualTo("program",programme);
        parsenotification.whereEqualTo("confirm",false);
        parsenotification.whereEqualTo("refus",false);
        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseRelation<ParseUser> parseRelation =parseUser.getRelation("friends");
        ParseQuery<ParseUser> parseQuery = parseRelation.getQuery();
        parseQuery.whereDoesNotMatchKeyInQuery("objectId", "iduser", parsenotification);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                adapter = new ProgramFriendAdapter(getActivity(),
                        R.layout.item_proram_friends, users,programId);
                try {
                    list.setAdapter(adapter);
                } catch (NullPointerException ex) {

                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try{
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
}
