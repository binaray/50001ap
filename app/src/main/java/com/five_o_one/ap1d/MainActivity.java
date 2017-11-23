package com.five_o_one.ap1d;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager=(ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }


    public class SectionPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{getString(R.string.locator_tab),
                getString(R.string.main_tab), getString(R.string.itenary_tab)};

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //a,b= parameters to pass if needed
                    return LocatorFragment.newInstance("a","b");
                case 1:
                default:
                    return MainFragment.newInstance("a","b");

                case 2:
                    return ItenaryFragment.newInstance("a","b");
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Locator";
                case 1:
                default:
                    return "Main";
                case 2:
                    return "Itenary";
            }
        }
    }

}
