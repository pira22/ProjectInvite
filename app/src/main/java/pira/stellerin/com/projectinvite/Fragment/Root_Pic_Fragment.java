package pira.stellerin.com.projectinvite.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by Pira on 02/09/2015.
 */
public class Root_Pic_Fragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_pic_fragment, container, false);
        Bundle bundle = getArguments();
        ListPicFragment pic=new ListPicFragment();
        pic.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.root_pic, pic,"list_pic");
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
