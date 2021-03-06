package com.example.speechhelper.facebook.authenticate;

import com.example.speechhelper.R;
import com.example.speechhelper.facebook.SpeechHelperApplication;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PickerActivity extends FragmentActivity {
	
	public static final Uri FRIEND_URL = Uri.parse("picker://friend");
	public static final Uri PLACE_URL = Uri.parse("picker://place");
	private FriendPickerFragment friendPickerFragment;
	private PlacePickerFragment placePickerFragment;
	private LocationListener locationListener;
	//default location for emulator
	private static final Location DEFAULT_LOCATION = new Location("") {{
        setLatitude(37.422006);
        setLongitude(-122.084095);
    }};
    private static final int SEARCH_RADIUS = 5000;//meters
    private static final int SEARCH_RESULTS_NUM = 50;//display 50 results
    private static final int LOCATION_CHANGE_THRESHOLD = 50; // meters
    private static final String SEARCH_TEXT = "";//default search
    @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pickers);

	    Bundle args = getIntent().getExtras();
	    FragmentManager manager = getSupportFragmentManager();
	    Fragment fragmentToShow = null;
	    Uri intentUri = getIntent().getData();
       // use intentUri to determine fragment will be shown
	    if (FRIEND_URL.equals(intentUri)) {
	        if (savedInstanceState == null) {
	            friendPickerFragment = new FriendPickerFragment(args);
	        } else {
	            friendPickerFragment = 
	                (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
	        }
	        
	        // listener for click on done button
	        friendPickerFragment.setOnDoneButtonClickedListener(
	                new PickerFragment.OnDoneButtonClickedListener() {
	            @Override
	            public void onDoneButtonClicked(PickerFragment<?> fragment) {
	                finishActivity();
	            }
	        });
	        fragmentToShow = friendPickerFragment;

	    }else if (PLACE_URL.equals(intentUri)) {
	        if (savedInstanceState == null) {
	            placePickerFragment = new PlacePickerFragment(args);
	        } else {
	            placePickerFragment = 
	                (PlacePickerFragment) manager.findFragmentById(R.id.picker_fragment);
	        }
	        //set listener for selection changed, once one location is selected, finish this activity
	        placePickerFragment.setOnSelectionChangedListener(
	                new PickerFragment.OnSelectionChangedListener() {
	            @Override
	            public void onSelectionChanged(PickerFragment<?> fragment) {
	                finishActivity(); 
	            }
	        });
	        //set listener for done button
	        placePickerFragment.setOnDoneButtonClickedListener(
	               new PickerFragment.OnDoneButtonClickedListener() {
	            @Override
	            public void onDoneButtonClicked(PickerFragment<?> fragment) {
	                finishActivity();
	            }
	        });
	        fragmentToShow = placePickerFragment;
	    }  else {
	        
	        setResult(RESULT_CANCELED);
	        finish();
	        return;
	    }

	    manager.beginTransaction()
	           .replace(R.id.picker_fragment, fragmentToShow)
	           .commit();
	}

	private void onError(Exception error) {
	    onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.error_dialog_title).
	            setMessage(error).
	            setPositiveButton(R.string.error_dialog_button_text, 
	               new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialogInterface, int i) {
	                    if (finishActivity) {
	                        finishActivity();
	                    }
	                }
	            });
	    builder.show();
	}

	private void finishActivity() {
		SpeechHelperApplication app = (SpeechHelperApplication) getApplication();
		if (FRIEND_URL.equals(getIntent().getData())) {
		    if (friendPickerFragment != null) {
		        app.setSelectedUsers(friendPickerFragment.getSelection());
		    }   
		} else if (PLACE_URL.equals(getIntent().getData())) {
	        if (placePickerFragment != null) {
	            app.setSelectedPlace(placePickerFragment.getSelection());
	        }
	    }
	    setResult(RESULT_OK, null);
	    finish();
	}
	@Override
	protected void onStart() {
	    super.onStart();
	    if (FRIEND_URL.equals(getIntent().getData())) {
	        try {
	            friendPickerFragment.loadData(false);
	        } catch (Exception e) {
	            onError(e);
	        }
	    } else if (PLACE_URL.equals(getIntent().getData())) {
	        try {
	            Location location = null;
	            // default criteria for a location provider
	            Criteria criteria = new Criteria();
	            // location manager from the system services
	            LocationManager locationManager = 
	                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	            // location provider that best matches the criteria
	            String bestProvider = locationManager.getBestProvider(criteria, false);
	            if (bestProvider != null) {
	       
	                location = locationManager.getLastKnownLocation(bestProvider);
	                if (locationManager.isProviderEnabled(bestProvider) 
	                            && locationListener == null) {
	                    // location listener and the selected provider 
	                    locationListener = new LocationListener() {
	                        @Override
	                        public void onLocationChanged(Location location) {
	                            // compare the current location to the desired location set in the place picker
	                            float distance = location.distanceTo(
	                                    placePickerFragment.getLocation());
	                            if (distance >= LOCATION_CHANGE_THRESHOLD) {
	                                placePickerFragment.setLocation(location);
	                                placePickerFragment.loadData(true);
	                            }
	                        }
	                        @Override
	                        public void onStatusChanged(String s, int i, 
	                                    Bundle bundle) {
	                        }
	                        @Override
	                        public void onProviderEnabled(String s) {
	                        }
	                        @Override
	                        public void onProviderDisabled(String s) {
	                        }
	                    };
	                    locationManager.requestLocationUpdates(bestProvider, 
	                            1, LOCATION_CHANGE_THRESHOLD,
	                            locationListener, 
	                            Looper.getMainLooper());
	                }
	            }
	            if (location == null) {
	                // using the default location if there is no initial location
	                String model = Build.MODEL;
	                if (model.equals("sdk") || 
	                        model.equals("google_sdk") || 
	                        model.contains("x86")) {
	                    // If using the emulator, use the default location
	                    location = DEFAULT_LOCATION;
	                }
	            }
	            if (location != null) {
	                // Configure the place picker: search center, radius, query, and maximum results.
	                placePickerFragment.setLocation(location);
	                placePickerFragment.setRadiusInMeters(SEARCH_RADIUS);
	                placePickerFragment.setSearchText(SEARCH_TEXT);
	                placePickerFragment.setResultsLimit(SEARCH_RESULTS_NUM);
	                placePickerFragment.loadData(true);
	            } else {
	                    // show an error when no  location found
	                onError(getResources()
	                        .getString(R.string.no_location_error), true);
	            }
	        } catch (Exception ex) {
	            onError(ex);
	        }
	    }
	}
	@Override
	protected void onStop() {
	    super.onStop();
	    if (locationListener != null) {
	        LocationManager locationManager = 
	            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        locationManager.removeUpdates(locationListener);
	        locationListener = null;
	    }
	}
}
