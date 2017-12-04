package com.five_o_one.ap1d;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener, LocatorFragment.OnLocatorFragmentInteractionListener{

    private DatabaseHelper databaseHelper;
    private List<LocationData> dataList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ListView container;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView bg_img;
    private int rand;
    private Button itinenaryButton;
    private List<Integer> selectedPositions=new ArrayList<Integer>();
    private SelectedListAdapter selectedListAdapter;
    private SharedPreferences preferences;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        container=(ListView) findViewById(R.id.drawer_list);
        itinenaryButton=(Button)findViewById(R.id.process_itinerary);
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
                try {
                    databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
                    dataList = databaseHelper.getDataList();
                }
                catch (Exception e){
                    return e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception e){
                pd.dismiss();
                setupUi();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onStart() {
        super.onStart();
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(getString(R.string.key_firstStart),true)) {
            new AsyncTask<String, Integer, Exception>() {
                @Override
                protected void onPreExecute() {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }

                @Override
                protected Exception doInBackground(String... strings) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception e) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }.execute();
            preferences.edit().putBoolean(getString(R.string.key_firstStart),false).apply();
        }
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
        drawerToggle.syncState();
        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        itinenaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ItinenaryActivity.class));
            }
        });

        //store selected locations indices to a list
        for (int i=0;i<dataList.size();i++) if (dataList.get(i).isSelected()==1) selectedPositions.add(i);
        selectedListAdapter=new SelectedListAdapter();
        container.setAdapter(selectedListAdapter);
    }

    @Override
    public void onMainFragmentInteraction(int position) {
        bg_img.setImageResource(dataList.get(position).getImageUrl());
    }

    @Override
    public void onLocationAdded(int position) {
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        itinenaryButton.setVisibility(View.VISIBLE);
        if (position==0) Toast.makeText(this,"Selected location is the start point!",Toast.LENGTH_SHORT).show();
        else if (selectedPositions.contains(position)) Toast.makeText(this,"Location has already been added!",Toast.LENGTH_SHORT).show();
        else {
            dataList.get(position).setSelected(1);
            databaseHelper.updateSelected(dataList.get(position));
            selectedPositions.add(position);
            selectedListAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Location added to itinerary!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocate(int currentPos) {
        viewPager.setCurrentItem(1);
        LocatorFragment.getInstance(dataList).locate(currentPos);
    }

    @Override
    public void onMain(int currentPos) {
        viewPager.setCurrentItem(0);
        MainFragment.getInstance(dataList,0).setFeatured(currentPos);

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
                    return MainFragment.getInstance(dataList,rand);
                case 1:
                    return LocatorFragment.getInstance(dataList);
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

    private class SelectedListAdapter extends ArrayAdapter<Integer>{
        public SelectedListAdapter() {
            super(MainActivity.this,R.layout.nav_drawer_item ,selectedPositions);
            if (selectedPositions.size() != 0) {
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                itinenaryButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final int index=selectedPositions.get(position);

            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.nav_drawer_item, parent, false);

            ((TextView) rowView.findViewById(android.R.id.text1)).setText(dataList.get(index).getName());
            rowView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataList.get(index).setSelected(0);
                    databaseHelper.updateSelected(dataList.get(index));
                    selectedPositions.remove(position);
                    notifyDataSetChanged();
                    if (selectedPositions.size() == 0) {
                        findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                        itinenaryButton.setVisibility(View.GONE);
                    }
                }
            });
            return rowView;
        }
    }
}