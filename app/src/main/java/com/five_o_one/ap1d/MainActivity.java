package com.five_o_one.ap1d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener{

    private DatabaseHelper databaseHelper;
    private List<LocationData> dataList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private LinearLayout container;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView bg_img;
    private int rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        container=(LinearLayout) findViewById(R.id.drawer_list);
        bg_img=(ImageView)findViewById(R.id.main_bg);
        rand=new Random().nextInt(10);

        new AsyncTask<String, Integer, Exception>() {
            ProgressDialog pd;
            @Override
            protected void onPreExecute(){
                pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("Please Wait");
                pd.setMessage("Loading database");
                pd.show();
            }
            @Override
            protected Exception doInBackground(String... strings) {
                databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
                dataList = databaseHelper.getDataList();
                return null;
            }
            @Override
            protected void onPostExecute(Exception e){
                pd.dismiss();
                setupUi();
            }
        }.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMainFragmentInteraction(int position) {
        bg_img.setImageResource(dataList.get(position).getImageUrl());
        addItem(position);
    }

    public void setupUi(){
        bg_img.setImageResource(dataList.get(rand).getImageUrl());  //set random featured bg
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.drawer_title));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{getString(R.string.main_tab), getString(R.string.locator_tab)};

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                default:
                    return MainFragment.newInstance(dataList,rand);
                case 1:
                    return LocatorFragment.newInstance("a","b");
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


    private void addItem(int position) {
        // Hides textview
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.nav_drawer_item, container, false);

        // Set the text in the new row to a location
        ((TextView) newView.findViewById(android.R.id.text1)).setText(dataList.get(position).getName());

        // Set a click listener for the "X" button in the row that will remove the row.
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                container.removeView(newView);
                // If there are no rows remaining, show the empty view.
                if (container.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });
        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        container.addView(newView, 0);
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void onSettingsAction(MenuItem item) {
        Intent settingsIntent=new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
