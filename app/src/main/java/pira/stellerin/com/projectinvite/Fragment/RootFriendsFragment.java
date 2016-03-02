package pira.stellerin.com.projectinvite.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootFriendsFragment extends Fragment {


    public RootFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_root_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.add(R.id.root_friends, ListFriendsFragment.NewInstance(), "listFriends");
        transaction.commit();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         menu.add(0, 4, 100, R.string.addimage).setIcon(R.drawable.ic_person_add_white_48dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
