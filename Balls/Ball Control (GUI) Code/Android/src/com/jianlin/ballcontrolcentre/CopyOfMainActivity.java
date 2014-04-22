package com.jianlin.ballcontrolcentre;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

	private static final String TAG = "BCC";
	
	public int flag=0;
	
	//for tab_0
	SeekBar tab_0_seekbar_r_1,tab_0_seekbar_g_1,tab_0_seekbar_b_1,tab_0_seekbar_r_2,tab_0_seekbar_g_2,tab_0_seekbar_b_2;
	Button tab_0_set;
	EditText tab_0_delay;
	Spinner tab_0_effect;
	
	//for tab_1
    SeekBar tab_1_seekbar_r_1,tab_1_seekbar_g_1,tab_1_seekbar_b_1,tab_1_seekbar_r_2,tab_1_seekbar_g_2,tab_1_seekbar_b_2;
    Button tab_1_set;
    EditText tab_1_delay;
    Spinner tab_1_effect;
	
    //for tab_2
    SeekBar tab_2_seekbar_r_1,tab_2_seekbar_g_1,tab_2_seekbar_b_1,tab_2_seekbar_r_2,tab_2_seekbar_g_2,tab_2_seekbar_b_2;
    Button tab_2_set;
    EditText tab_2_delay;
    Spinner tab_2_effect;
    
    //for tab_3
    SeekBar tab_3_seekbar_r_1,tab_3_seekbar_g_1,tab_3_seekbar_b_1,tab_3_seekbar_r_2,tab_3_seekbar_g_2,tab_3_seekbar_b_2;
    Button tab_3_set;
    EditText tab_3_delay;
    Spinner tab_3_effect;
	

	// SPP UUID
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// MAC address
	private static String address = "20:13:01:23:02:56";
	
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d(TAG, "In onCreate()");
        //btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
  
        // For each of the sections in the app, add a tab to the action bar.
        Log.d(TAG, "Adding tabs...");
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        Log.d(TAG, "Done!");
        
        Log.d(TAG, "Linking GUI...");
        //for tab_0
        tab_0_seekbar_r_1 = (SeekBar) findViewById(R.id.tab_0_label_colour_r_1);
        tab_0_seekbar_g_1 = (SeekBar) findViewById(R.id.tab_0_label_colour_g_1);
        tab_0_seekbar_b_1 = (SeekBar) findViewById(R.id.tab_0_label_colour_b_1);
        tab_0_seekbar_r_2 = (SeekBar) findViewById(R.id.tab_0_label_colour_r_2);
        tab_0_seekbar_g_2 = (SeekBar) findViewById(R.id.tab_0_label_colour_g_2);
        tab_0_seekbar_b_2 = (SeekBar) findViewById(R.id.tab_0_label_colour_b_2);
        tab_0_set = (Button) findViewById(R.id.tab_0_button_set);
        tab_0_delay = (EditText) findViewById(R.id.tab_0_delay);
        tab_0_effect = (Spinner) findViewById(R.id.tab_0_effect);
        
        //for tab_1
        tab_1_seekbar_r_1 = (SeekBar) findViewById(R.id.tab_1_label_colour_r_1);
        tab_1_seekbar_g_1 = (SeekBar) findViewById(R.id.tab_1_label_colour_g_1);
        tab_1_seekbar_b_1 = (SeekBar) findViewById(R.id.tab_1_label_colour_b_1);
        tab_1_seekbar_r_2 = (SeekBar) findViewById(R.id.tab_1_label_colour_r_2);
        tab_1_seekbar_g_2 = (SeekBar) findViewById(R.id.tab_1_label_colour_g_2);
        tab_1_seekbar_b_2 = (SeekBar) findViewById(R.id.tab_1_label_colour_b_2);
        tab_1_set = (Button) findViewById(R.id.tab_1_button_set);
        tab_1_delay = (EditText) findViewById(R.id.tab_1_delay);
        tab_1_effect = (Spinner) findViewById(R.id.tab_1_effect);
        
        //for tab_2
        tab_2_seekbar_r_1 = (SeekBar) findViewById(R.id.tab_2_label_colour_r_1);
        tab_2_seekbar_g_1 = (SeekBar) findViewById(R.id.tab_2_label_colour_g_1);
        tab_2_seekbar_b_1 = (SeekBar) findViewById(R.id.tab_2_label_colour_b_1);
        tab_2_seekbar_r_2 = (SeekBar) findViewById(R.id.tab_2_label_colour_r_2);
        tab_2_seekbar_g_2 = (SeekBar) findViewById(R.id.tab_2_label_colour_g_2);
        tab_2_seekbar_b_2 = (SeekBar) findViewById(R.id.tab_2_label_colour_b_2);
        tab_2_set = (Button) findViewById(R.id.tab_2_button_set);
        tab_2_delay = (EditText) findViewById(R.id.tab_2_delay);
        tab_2_effect = (Spinner) findViewById(R.id.tab_2_effect);
        
        //for tab_3
        tab_3_seekbar_r_1 = (SeekBar) findViewById(R.id.tab_3_label_colour_r_1);
        tab_3_seekbar_g_1 = (SeekBar) findViewById(R.id.tab_3_label_colour_g_1);
        tab_3_seekbar_b_1 = (SeekBar) findViewById(R.id.tab_3_label_colour_b_1);
        tab_3_seekbar_r_2 = (SeekBar) findViewById(R.id.tab_3_label_colour_r_2);
        tab_3_seekbar_g_2 = (SeekBar) findViewById(R.id.tab_3_label_colour_g_2);
        tab_3_seekbar_b_2 = (SeekBar) findViewById(R.id.tab_3_label_colour_b_2);
        tab_3_set = (Button) findViewById(R.id.tab_3_button_set);
        tab_3_delay = (EditText) findViewById(R.id.tab_3_delay);
        tab_3_effect = (Spinner) findViewById(R.id.tab_3_effect);
        Log.d(TAG, "Done!");
        
        Log.d(TAG, "Create listener...");
        /*
        tab_1_set.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		//set
				Log.d(TAG, "Tab 1 set button clicked!");
	    	}  	
	    });
        
        tab_2_set.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		//set
				Log.d(TAG, "Tab 2 set button clicked!");
	    	}  	
	    });
    
        tab_3_set.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		//set
				Log.d(TAG, "Tab 3 set button clicked!");
	    	}  	
	    });
	    */
        Log.d(TAG, "Done!");
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //args.clear();
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        	View rootView;
            //View rootView = inflater.inflate(R.layout.tab_all, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            
            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
            	case 1:rootView = inflater.inflate(R.layout.tab_predefine, container, false);break;
	            case 2:rootView = inflater.inflate(R.layout.tab_all, container, false);break;
	            case 3:rootView = inflater.inflate(R.layout.tab_ball_1, container, false);break;
	            case 4:rootView = inflater.inflate(R.layout.tab_ball_2, container, false);break;
	            case 5:rootView = inflater.inflate(R.layout.tab_ball_3, container, false);break;
	            default:rootView = inflater.inflate(R.layout.fragment_main, container, false);
            }
            
            return rootView;
        }
    }

}
