package com.example.andrey.petsitter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.andrey.petsitter.Database.PetsitterApp;
import com.example.andrey.petsitter.Location.MyLocationListener;
import com.example.andrey.petsitter.Models.AnimalType;
import com.example.andrey.petsitter.Models.Classified;
import com.example.andrey.petsitter.Models.ClassifiedParse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static ArrayList<Classified> classifieds;
    private ArrayList<Classified> animalArray;
    private Classified currentClassified;
    private LocationManager locationManager;
    private MyLocationListener locationListener;

    private CharSequence mTitle;
    private boolean isChecked = false;
    private boolean isFromDrawer = true;
    int countObjects = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager = null;
        locationListener = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ClassifiedParse.class);
        Parse.initialize(this, "eyhGBGaMPlCFZTg0HVBKj3oGhF39D3T7YsTSFlMb", "SSI3I7VhYU4nxehokb4JDQ0O15hkYTLn6xiB5s83");
        setClassifiedsList();
        setContentView(R.layout.activity_main);

        locationListener = MyLocationListener.instance(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 100, locationListener);
        setClassifiedsList();
    }

    private void setClassifiedsList() {
        final ParseQuery<ClassifiedParse> query = ParseQuery.getQuery(ClassifiedParse.class);

        int count = 0;
        try {
            count = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(count != 0 && count != countObjects) {

            classifieds = new ArrayList<Classified>();
            List<ClassifiedParse> classifiedsParse = null;

            try {
                classifiedsParse = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (ClassifiedParse classified : classifiedsParse) {
                Classified newClassified = getClassified(classified);
                classifieds.add(newClassified);
            }

            animalArray = classifieds;
        }
    }

    private Classified getClassified(ClassifiedParse classified) {
        String title = classified.getTitle();
        String description = classified.getDescription();
        String authorName = classified.getAuthorName();
        String phone = classified.getPhone();
        String address = classified.getAddress();

        String animalStr = classified.getAnimalType();
        AnimalType animalType = AnimalType.Other;

        if(animalStr.equals("Dog")){
            animalType = AnimalType.Dog;
        } else if(animalStr.equals("Cat")){
            animalType = AnimalType.Cat;
        } else if(animalStr.equals("Bird")){
            animalType = AnimalType.Bird;
        } else if(animalStr.equals("Rodent")){
            animalType = AnimalType.Rodent;
        } else if(animalStr.equals("Fish")){
            animalType = AnimalType.Fish;
        }

        byte[] imageByte = new byte[0];
        try {
            imageByte = classified.getImage().getData();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        ParseGeoPoint location = classified.getLocation();
        Location classifiedLocation = new Location("GPS_PROVIDER");

        if(location != null) {
            classifiedLocation.setLatitude(location.getLatitude());
            classifiedLocation.setLongitude(location.getLongitude());
        } else {
            classifiedLocation.setLatitude(0);
            classifiedLocation.setLongitude(0);
        }

        return new Classified(title, description, authorName,phone, address, image, animalType, classifiedLocation);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        animalArray = new ArrayList<Classified>();
        isFromDrawer = true;

        if (position == 0) {
            animalArray = classifieds;
            inflateFragment(1, animalArray);
        } else if (position == 1) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Dog)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(2, animalArray);
        } else if (position == 2) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Cat)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(3, animalArray);
        } else if (position == 3) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Bird)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(4, animalArray);
        } else if (position == 4) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Rodent)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(5, animalArray);
        } else if (position == 5) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Fish)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(6, animalArray);
        } else if (position == 6) {
            for (Classified animal : classifieds) {
                if (animal.getAnimalType().equals(AnimalType.Other)) {
                    animalArray.add(animal);
                }
            }
            inflateFragment(7, animalArray);
        }

        if (position > 0 && position <= classifieds.size()) {
            currentClassified = classifieds.get(position - 1);
        } else {
            currentClassified = null;
        }
    }

    private void inflateFragment(int number, ArrayList<Classified> array) {
        android.app.Fragment fragment = ClassifiedsFragment.newInstance("section_number", number, array, locationListener);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        if (number == 1) {
            mTitle = "All";
        } else {
            mTitle = String.valueOf(AnimalType.values()[number - 2]) + "s";
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setActionBarTabs() {
        ActionBar actionBarTab = getSupportActionBar();

        actionBarTab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBarTab.setDisplayShowTitleEnabled(true);

        actionBarTab.removeAllTabs();

        ActionBar.Tab tabAll = actionBarTab.newTab();
        tabAll.setText("All");
        tabAll.setTabListener(this);
        actionBarTab.addTab(tabAll);


        ActionBar.Tab tabMap = actionBarTab.newTab();
        tabMap.setText("Map");
        tabMap.setTabListener(this);
        actionBarTab.addTab(tabMap);

        ActionBar.Tab tabCity = actionBarTab.newTab();
        tabCity.setText("My city");
        tabCity.setTabListener(this);
        actionBarTab.addTab(tabCity);

        ActionBar.Tab myClassifieds = actionBarTab.newTab();
        myClassifieds.setText("My Classifieds");
        myClassifieds.setTabListener(this);
        actionBarTab.addTab(myClassifieds);

        ActionBar.Tab tabAdd = actionBarTab.newTab();
        tabAdd.setText("Add");
        tabAdd.setTabListener(this);
        actionBarTab.addTab(tabAdd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            setActionBarTabs();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 0) {
            if (isChecked) {
                isChecked = false;
            } else {
                if (isFromDrawer) {
                    inflateFragment(1, animalArray);

                    isFromDrawer = false;
                    isChecked = true;
                } else {
                    if (!isChecked) {
                        animalArray = classifieds;

                        inflateFragment(1, animalArray);

                        isChecked = true;
                    }
                }
            }
        } else if (tab.getPosition() == 1) {
            android.app.Fragment fragment = MapFragment.newInstance(locationListener, this.classifieds);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else if (tab.getPosition() == 2) {
            ArrayList<Classified> townClassifieds = new ArrayList<Classified>();
            String city = "";
            if(locationListener.getCurrentAddress().equals("")|| locationListener.getCurrentAddress().equals("No address found")){
                city = "";
            } else {
                city = locationListener.getCurrentAddress();
            }

            for (Classified notice : classifieds) {
                if (notice.getAddress().toLowerCase().contains(city)) {
                    townClassifieds.add(notice);
                }
            }

            inflateFragment(1, townClassifieds);
            isChecked = false;
        } else if (tab.getPosition() == 3) {
            ArrayList<Classified> myClassifieds = ((PetsitterApp) this.getApplication()).getUserDB().getAllBasketItems();

            android.app.Fragment fragment = MyClassifiedsFragment.newInstance(myClassifieds);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else if (tab.getPosition() == 4) {
            android.app.Fragment fragment = AddFragment.newInstance(locationListener);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "GPS Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "GPS Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
            showDialog(connectionResult.getErrorCode());
        }
    }
}