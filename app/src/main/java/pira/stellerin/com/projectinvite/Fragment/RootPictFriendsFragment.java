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
public class RootPictFriendsFragment extends Fragment {


    public RootPictFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_root_pict_friends, container, false);
        Bundle bundle = getArguments();
        ListPicFriendsFragment pic=new ListPicFriendsFragment();
        pic.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.containerrootpicf, pic,"list_pic_friends");
        transaction.commit();
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
