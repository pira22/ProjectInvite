package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.UserAdapter;
import pira.stellerin.com.projectinvite.Adapter.UserfriendsAdapter;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvitFriendsFragment extends Fragment {
        ListView list;
        UserfriendsAdapter adapter;
        List<String>fbids = new ArrayList<String>();
        public InvitFriendsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ActivityContainer. DETECT = "addfriend";
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.addFriends);
            setHasOptionsMenu(true);
            for(int i=0;i<Shared.listFriends.size();i++)
                fbids.add(Shared.listFriends.get(i).getId());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_invit_friends, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            list = (ListView) getView().findViewById(
                    R.id.listfriends);
            ParseUser parseUser = ParseUser.getCurrentUser();
            ParseRelation<ParseUser> parseRelation =parseUser.getRelation("friends");
            ParseQuery<ParseUser> parseQuery = parseRelation.getQuery();
            ParseQuery<ParseUser> parseQuery1= ParseUser.getQuery();
            parseQuery1.whereContainedIn("fbId", fbids);
            parseQuery1.whereDoesNotMatchKeyInQuery("objectId", "objectId", parseQuery);
            parseQuery1.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    adapter = new UserfriendsAdapter(getActivity(),
                            R.layout.item_user_friends, users);
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
            MenuItem item = menu.findItem(4);
            item.setVisible(false);

        }catch (NullPointerException e){

        }
    }
}
