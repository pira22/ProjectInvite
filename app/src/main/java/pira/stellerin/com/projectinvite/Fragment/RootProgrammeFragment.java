package pira.stellerin.com.projectinvite.Fragment;

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
public class RootProgrammeFragment extends Fragment {
     public static  String menuitem ;
    public static String ID_USER , ID_USER_PARTAGE ,TAG = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        menuitem ="child";
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_programme_fragment, container, false);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.root_prog, new ListeProgramme(),"listeProgramme");
        transaction.commit();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       if(menuitem.equals("child")) {
            menu.add(0, 161, 100, R.string.addimage).setIcon(R.drawable.ic_notif_act).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.add(0, 2, 100, R.string.addimage).setIcon(R.drawable.ic_add_box).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }//else if(menuitem.equals("dz")) {
           // menu.add(0, 3, 100, R.string.addimage).setIcon(R.drawable.ic_camera_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

       // }

        if(ID_USER!=null) {
            if (!ID_USER.equals(ID_USER_PARTAGE)) {
                try {
                    menu.findItem(3).setVisible(false);
                }catch (NullPointerException e){

                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (item.getItemId()){

            case 2:
                PostFragment gp = new PostFragment();
                transaction.replace(R.id.root_prog, gp, "post");
                transaction.addToBackStack("post");
                transaction.commit();
                return true;
            case 161:
                NotificationFragment nf = new NotificationFragment();
                transaction.replace(R.id.root_prog, nf, "notif");
                transaction.addToBackStack("notif");
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
