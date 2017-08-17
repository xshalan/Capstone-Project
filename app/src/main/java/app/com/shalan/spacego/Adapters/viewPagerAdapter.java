package app.com.shalan.spacego.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noura on 31/07/2017.
 */

public class viewPagerAdapter extends FragmentStatePagerAdapter{
    private  List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();



    public viewPagerAdapter(FragmentManager fm) {
        super(fm);

        mFragmentTitleList.add("About") ;
        mFragmentTitleList.add("Feature") ;
        mFragmentTitleList.add("Map");
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void setmFragmentList(List<Fragment> fragmentList){
        mFragmentList = fragmentList ;
    }

}
