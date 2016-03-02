package pira.stellerin.com.projectinvite.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;

import pira.stellerin.com.projectinvite.Fragment.ProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.RootFragment;
import pira.stellerin.com.projectinvite.Fragment.RootProgrammeFragment;

/**
 * Created by Pira on 01/09/2015.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	private ArrayList<Integer> listIcon;

	public CustomPagerAdapter(FragmentManager fm, ArrayList<Integer> list) {
		super(fm);
		this.listIcon = list;
	}


	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position){
			case 0: fragment = new  RootFragment();
				break;
			case 1: fragment = new RootProgrammeFragment();

				break;
			case 3 : fragment = new ProfilFragment();
				break;
		}
		return fragment;
    }

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return "";
	}


	@Override
	public int getIconResId(int index) {
		return listIcon.get(index);
	}

}
