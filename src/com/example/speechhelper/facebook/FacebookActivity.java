package com.example.speechhelper.facebook;
import com.facebook.*;
import com.example.speechhelper.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class FacebookActivity extends FragmentActivity  {
	private static final int LOGIN = 0;
	private static final int SELECTION = 1;
	private static final int SETTINGS = 2;
	private boolean isResumed = false;
	private Fragment[] fragments = new Fragment[3];
	private MenuItem logout;
	private UiLifecycleHelper uiLifecycleHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		uiLifecycleHelper = new UiLifecycleHelper(this, callback);
	    uiLifecycleHelper.onCreate(savedInstanceState);
		
	    FragmentManager fm = getSupportFragmentManager();
	    fragments[LOGIN] = fm.findFragmentById(R.id.splashFragment);
	    fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
	    fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

	    FragmentTransaction transaction = fm.beginTransaction();
	    for(int i = 0; i < fragments.length; i++) {
	        transaction.hide(fragments[i]);
	    }
	    transaction.commit();
	    
		
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // add the menu when logged in
	    if (fragments[SELECTION].isVisible()) {
	        if (menu.size() == 0) {
	            logout = menu.add(R.string.logout);
	        }
	        return true;
	    } else {
	        menu.clear();
	        logout = null;
	    }
	    return false;
	}
	
	
	
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open, try to show the selection fragment
	        showFragment(SELECTION, false);
	    } else {
	        // otherwise present the splash screen and ask the user to login.
	        showFragment(LOGIN, false);
	    }
	}

	@Override
	public void onResume() {
	    super.onResume();
	    uiLifecycleHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiLifecycleHelper.onPause();
	    isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiLifecycleHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiLifecycleHelper.onSaveInstanceState(outState);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.equals(logout)) {
	        showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
	    FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    for (int i = 0; i < fragments.length; i++) {
	        if (i == fragmentIndex) {
	            transaction.show(fragments[i]);
	        } else {
	            transaction.hide(fragments[i]);
	        }
	    }
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
	}
    
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes when the activity is visible
	    if (isResumed) {
	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	            // If the session state is open: show the authenticated fragment
	            showFragment(SELECTION, false);
	        } else if (state.isClosed()) {
	            // If the session state is closed: show the login fragment
	            showFragment(LOGIN, false);
	        }
	    }
	}
	
	private Session.StatusCallback callback = 
		    new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception e) {
		        onSessionStateChange(session, state, e);
		    }
		};

}

