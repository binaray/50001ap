package com.five_o_one.ap1d;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{

    private DatabaseHelper databaseHelper;
    private List<LocationData> dataList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView bg_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        dataList = databaseHelper.getDataList();

        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        bg_img=(ImageView)findViewById(R.id.main_bg);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onFragmentInteraction(int position) {
        bg_img.setImageResource(position);
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
                    return MainFragment.newInstance(dataList);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onSettingsAction(MenuItem item) {
        Intent settingsIntent=new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
