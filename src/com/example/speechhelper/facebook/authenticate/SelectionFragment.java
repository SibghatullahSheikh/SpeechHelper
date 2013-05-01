package com.example.speechhelper.facebook.authenticate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.speechhelper.R;
import com.example.speechhelper.facebook.SpeechHelperApplication;
import com.example.speechhelper.facebook.util.HaveAction;
import com.example.speechhelper.facebook.util.PostResponse;
import com.example.speechhelper.facebook.util.TalkGraphObject;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectionFragment extends Fragment {
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private ListView listView;
	private List<BaseListElement> listElements;
	private List<GraphUser> selectedUsers;
	private ProgressDialog progressDialog;
	private Button announceButton;
	private GraphPlace selectedPlace = null;
	
	private String talkChoiceUrl = null;
	private String talkChoice = null;
	private static final String TAG = "SelectionFragment";
	private static final String FRIENDS_KEY = "friends";
	private static final String PLACE_KEY = "place";
	private static final String TALK_KEY = "talk";
	private static final String TALK_URL_KEY = "talk_url";
	// Activity code to flag an incoming activity result is due to a new permissions request
	private static final int REAUTH_ACTIVITY_CODE = 100;
	// Key used in storing the pendingAnnounce flag
	private static final String PENDING_ANNOUNCE_KEY = "pendingAnnounce";	
	// URL for authentication 
	private static final Uri FACEBOOK_URL = Uri.parse("http://m.facebook.com");
	// post action path defined on App Dashboard using app's package 
	private static final String POST_ACTION_PATH = "me/speechhelper:have";
	 // List of additional write permissions being requested
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private UiLifecycleHelper uiLifecycleHelper;
	// Indicates an on-going reauthorization request
	private boolean pendingAnnounce;
	
   
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    
	    View view = inflater.inflate(R.layout.selection, container, false);
	    // Find the user's profile picture custom view
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
	    profilePictureView.setCropped(true);

	    // Find the user's name view
	    userNameView = (TextView) view.findViewById(R.id.selection_user_name);
	    // Find the list view
	    listView = (ListView) view.findViewById(R.id.selection_list);
	    
	    // Set up the publish action button
	    announceButton = (Button) view.findViewById(R.id.announce_button);
	    announceButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	 handleAnnounce();
	        }
	    });
	    init(savedInstanceState);
	    return view;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiLifecycleHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiLifecycleHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REAUTH_ACTIVITY_CODE) {
	        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
	    }else if (resultCode == Activity.RESULT_OK && 
	            requestCode >= 0 && requestCode < listElements.size()) {
	        listElements.get(requestCode).onActivityResult(data);
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiLifecycleHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    for (BaseListElement listElement : listElements) {
	        listElement.onSaveInstanceState(bundle);
	    }
	    bundle.putBoolean(PENDING_ANNOUNCE_KEY, pendingAnnounce);
	    uiLifecycleHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiLifecycleHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiLifecycleHelper.onDestroy();
	}
	
	private void onPostActionResponse(Response response) {
			if (progressDialog != null) {
		        progressDialog.dismiss();
		        progressDialog = null;
		    }
		    if (getActivity() == null) {

		        return;
		    }

		    PostResponse postResponse = 
		        response.getGraphObjectAs(PostResponse.class);

		    if (postResponse != null && postResponse.getId() != null) {
		        String dialogBody = String.format(getString(
		                                R.string.result_dialog_text), 
		                                postResponse.getId());
		        new AlertDialog.Builder(getActivity())
		                .setPositiveButton(R.string.result_dialog_button_text, 
		                                   null)
		                .setTitle(R.string.result_dialog_title)
		                .setMessage(dialogBody)
		                .show();
		        init(null);
		    } else {
		        handleError(response.getError());
		    }
		}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    if (session != null && session.isOpened()) {
	        if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	            // Session updated with new permissions, try once more.
	            tokenUpdated();
	        } else {
	            // Get the user data.
	            userDataRequest(session);
	        }
	    }
	}
	
    private List<GraphUser> restoreByteArray(byte[] bytes) {
	    try {
	        @SuppressWarnings("unchecked")
	        List<String> usersAsString =
	                (List<String>) (new ObjectInputStream
	                                (new ByteArrayInputStream(bytes)))
	                                .readObject();
	        if (usersAsString != null) {
	            List<GraphUser> users = new ArrayList<GraphUser>
	            (usersAsString.size());
	            for (String user : usersAsString) {
	                GraphUser graphUser = GraphObject.Factory
	                .create(new JSONObject(user), 
	                                GraphUser.class);
	                users.add(graphUser);
	            }   
	            return users;
	        }   
	    } catch (ClassNotFoundException e) {
	        Log.e(TAG, "ClassNotFoundException: unable to deserialize users.", e); 
	    } catch (IOException e) {
	        Log.e(TAG, "IOException: unable to deserialize users.", e); 
	    } catch (JSONException e) {
	        Log.e(TAG, "JSONException: unable to deserialize users.", e); 
	    }   
	    return null;
	}
	private class LocationListElement extends BaseListElement {

	    public LocationListElement(int requestCode) {
	        super(getActivity().getResources()
	              .getDrawable(R.drawable.action_location),
	              getActivity().getResources()
	              .getString(R.string.action_location),
	              getActivity().getResources()
	              .getString(R.string.action_location_default),
	              requestCode);
	    }
	    @Override
	    protected void populateOGAction(OpenGraphAction action) {
	        if (selectedPlace != null) {
	            action.setPlace(selectedPlace);
	        }   
	    } 
	    private void setPlaceText() {
	        String text = null;
	        if (selectedPlace != null) {
	            text = selectedPlace.getName();
	        }   
	        if (text == null) {
	            text = getResources().getString(R.string.action_location_default);
	        }   
	        setText2(text);
	    }
	    @Override
	    protected void onActivityResult(Intent data) {
	        selectedPlace = ((SpeechHelperApplication) getActivity()
	                .getApplication()).getSelectedPlace();
	        setPlaceText();
	        notifyDataChanged();
	    }  
	    @Override
	    protected void onSaveInstanceState(Bundle bundle) {
	        if (selectedPlace != null) {
	            bundle.putString(PLACE_KEY, 
	                    selectedPlace.getInnerJSONObject().toString());
	        }   
	    }
	    @Override
	    protected boolean restoreState(Bundle savedState) {
	        String place = savedState.getString(PLACE_KEY);
	        if (place != null) {
	            try {
	                selectedPlace = GraphObject.Factory.create(
	                        new JSONObject(place), 
	                        GraphPlace.class);
	                setPlaceText();
	                return true;
	            } catch (JSONException e) {
	                Log.e(TAG, "JSONException: unable to deserialize place.", e); 
	            }   
	        }   
	        return false;
	    }  
	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            toStartPickerActivity(PickerActivity.PLACE_URL, getRequestCode());
	            }
	        };
	    }
	}
	private class TalkListElement extends BaseListElement {

	    private final String[] talkChoices;
	    private final String[] talkUrls;

	    public TalkListElement(int requestCode) {
	        super(getActivity().getResources()
	                .getDrawable(R.drawable.ic_launcher),
	              getActivity().getResources()
	                .getString(R.string.action_have),
	              getActivity().getResources()
	                .getString(R.string.action_have_default),
	              requestCode);
	        talkChoices = getActivity().getResources()
	                          .getStringArray(R.array.talk_types);
	        talkUrls = getActivity().getResources()
	                       .getStringArray(R.array.talk_og_urls);
	    }
	    @Override
	    protected void populateOGAction(OpenGraphAction action) {
	        if (talkChoiceUrl != null) {
	            HaveAction haveAction = action.cast(HaveAction.class);
	            TalkGraphObject talk = 
	                GraphObject.Factory.create(TalkGraphObject.class);
	            talk.setUrl(talkChoiceUrl);
	            haveAction.setTalk(talk);
	        }   
	    } 

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	showTalkOptions();
	            }
	        };
	    }
	    private void showTalkOptions() {
	        String title = getActivity().getResources().getString(R.string.select_speech);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(title).
	                setCancelable(true).
	                setItems(talkChoices, new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialogInterface, int i) {
	                          talkChoice = talkChoices[i];
	                          talkChoiceUrl = talkUrls[i];
	                          setTalkText();
	                          notifyDataChanged();
	                    }   
	                }); 
	        builder.show();
	    }
	    private void setTalkText() {
	    	 if (talkChoice != null && talkChoiceUrl != null) {
	    	        setText2(talkChoice);
	    	        announceButton.setEnabled(true);
	    	    } else {
	    	        setText2(getActivity().getResources()
	    	                        .getString(R.string.action_have_default));
	    	        announceButton.setEnabled(false);
	    	    }  
	    }
	    @Override
	    protected void onSaveInstanceState(Bundle bundle) {
	        if (talkChoice != null && talkChoiceUrl != null) {
	            bundle.putString(TALK_KEY, talkChoice);
	            bundle.putString(TALK_URL_KEY, talkChoiceUrl);
	        }   
	    }
	    @Override
	    protected boolean restoreState(Bundle savedState) {
	        String talk = savedState.getString(TALK_KEY);
	        String talkUrl = savedState.getString(TALK_URL_KEY);
	        if (talk != null && talkUrl != null) {
	            talkChoice = talk;
	            talkChoiceUrl = talkUrl;
	            setTalkText();
	            return true;
	        }   
	        return false;
	    } 

	}
	private class PeopleListElement extends BaseListElement {

		    public PeopleListElement(int requestCode) {
		        super(getActivity().getResources().getDrawable(R.drawable.action_people),
		              getActivity().getResources().getString(R.string.action_people),
		              getActivity().getResources().getString(R.string.action_people_default),
		              requestCode);
		    }

		    @Override
		    protected View.OnClickListener getOnClickListener() {
		        return new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	toStartPickerActivity(PickerActivity.FRIEND_URL, getRequestCode());
		            }
		        };
		    }
		    
		    @Override
		    protected void onActivityResult(Intent data) {
		        selectedUsers = ((SpeechHelperApplication) getActivity()
		                 .getApplication())
		                 .getSelectedUsers();
		        setUsersText();
		        notifyDataChanged();
		    }
		    @Override
		    protected void onSaveInstanceState(Bundle bundle) {
		        if (selectedUsers != null) {
		            bundle.putByteArray(FRIENDS_KEY,
		                    getByteArray(selectedUsers));
		        }   
		    } 
		    @Override
		    protected boolean restoreState(Bundle savedState) {
		        byte[] bytes = savedState.getByteArray(FRIENDS_KEY);
		        if (bytes != null) {
		            selectedUsers = restoreByteArray(bytes);
		            setUsersText();
		            return true;
		        }   
		        return false;
		    } 
		    private void setUsersText() {
			    String text = null;
			    if (selectedUsers != null) {
			            // If there is one friend
			        if (selectedUsers.size() == 1) {
			            text = String.format(getResources()
			                    .getString(R.string.single_user_selected),
			                    selectedUsers.get(0).getName());
			        } else if (selectedUsers.size() == 2) {
			            // If there are two friends 
			            text = String.format(getResources()
			                    .getString(R.string.two_users_selected),
			                    selectedUsers.get(0).getName(), 
			                    selectedUsers.get(1).getName());
			        } else if (selectedUsers.size() > 2) {
			            // If there are more than two friends 
			            text = String.format(getResources()
			                    .getString(R.string.multiple_users_selected),
			                    selectedUsers.get(0).getName(), 
			                    (selectedUsers.size() - 1));
			        }   
			    }   
			    if (text == null) {
			        // If no text, use the placeholder text
			        text = getResources()
			        .getString(R.string.action_people_default);
			    }   
			    // Set the text in list element. This will notify the 
			    // adapter that the data has changed to
			    // refresh the list view.
			    setText2(text);
			} 
		    @Override
		    protected void populateOGAction(OpenGraphAction action) {
		        if (selectedUsers != null) {
		            action.setTags(selectedUsers);
		        }   
		    }  
		    private byte[] getByteArray(List<GraphUser> users) {
		        // convert the list of GraphUsers to a list of String 
		        // where each element is the JSON representation of the 
		        // GraphUser so it can be stored in a Bundle
		        List<String> usersAsString = new ArrayList<String>(users.size());

		        for (GraphUser user : users) {
		            usersAsString.add(user.getInnerJSONObject().toString());
		        }   
		        try {
		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            new ObjectOutputStream(outputStream).writeObject(usersAsString);
		            return outputStream.toByteArray();
		        } catch (IOException e) {
		            Log.e(TAG, "Unable to serialize users.", e); 
		        }   
		        return null;
		    }   
		    
		}
	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
	    private List<BaseListElement> listElements;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<BaseListElement> listElements) {
	        super(context, resourceId, listElements);
	        this.listElements = listElements;
	        // Set up as an observer for list item changes to
	        // refresh the view.
	        for (int i = 0; i < listElements.size(); i++) {
	            listElements.get(i).setAdapter(this);
	        }
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater =
	                    (LayoutInflater) getActivity()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.listitem, null);
	        }

	        BaseListElement listElement = listElements.get(position);
	        if (listElement != null) {
	            view.setOnClickListener(listElement.getOnClickListener());
	            ImageView icon = (ImageView) view.findViewById(R.id.icon);
	            TextView text1 = (TextView) view.findViewById(R.id.text1);
	            TextView text2 = (TextView) view.findViewById(R.id.text2);
	            if (icon != null) {
	                icon.setImageDrawable(listElement.getIcon());
	            }
	            if (text1 != null) {
	                text1.setText(listElement.getText1());
	            }
	            if (text2 != null) {
	                text2.setText(listElement.getText2());
	            }
	        }
	        return view;
	    }

	}
	private void annoucePermissionRequest(Session session) {
	    if (session != null) {
	        Session.NewPermissionsRequest newPermissionsRequest = 
	            new Session.NewPermissionsRequest(this, PERMISSIONS).
	                setRequestCode(REAUTH_ACTIVITY_CODE);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	    }
	}
	private void handleAnnounce() {
	    pendingAnnounce = false;
	    Session session = Session.getActiveSession();

	    if (session == null || !session.isOpened()) {
	        return;
	    }

	    List<String> permissions = session.getPermissions();
	    if (!permissions.containsAll(PERMISSIONS)) {
	        pendingAnnounce = true;
	        annoucePermissionRequest(session);
	        return;
	    }

	 // Show a progress dialog when waiting
	 progressDialog = ProgressDialog.show(getActivity(), "", 
	         getActivity().getResources()
	         .getString(R.string.progress_dialog_text), true);

	 // Run this in a background thread. And create a new AsyncTask that returns a Response object
	 AsyncTask<Void, Void, Response> task = 
	     new AsyncTask<Void, Void, Response>() {

	     @Override
	     protected Response doInBackground(Void... voids) {
	         // Create an have action which is defined when registered this app
	         HaveAction haveAction = 
	         GraphObject.Factory.create(HaveAction.class);
	         // Populate the action with the POST parameters: the talk, friends, and place information
	         for (BaseListElement element : listElements) {
	             element.populateOGAction(haveAction);
	         }   
	         // Set up a request with the active session, set up an HTTP POST 
	         Request request = new Request(Session.getActiveSession(),
	                 POST_ACTION_PATH, null, HttpMethod.POST);
	         // Add the have action as post parameter
	         request.setGraphObject(haveAction);
	         // Execute the request in the background and return the response.
	         return request.executeAndWait();
	     }   

	     @Override
	     protected void onPostExecute(Response response) {
	         // Process the response
	         onPostActionResponse(response);
	      }   
	 };  

	 // Execute the task
	 task.execute();

	}
    private void tokenUpdated() {
	    // Check if a publish action is in progress
	    // awaiting a successful reauthorization
	    if (pendingAnnounce) {
	        // Publish the action
	        handleAnnounce();
	    }
	}
	private void handleError(FacebookRequestError error) {
	    DialogInterface.OnClickListener listener = null;
	    String dialogBody = null;

	    if (error == null) {
	        // no response from the server.
	        dialogBody = getString(R.string.error_dialog_default_text);
	    } else {
	        switch (error.getCategory()) {
	            case AUTHENTICATION_RETRY:
	                
	                //Show message id, and retry the operation later.
	                String userAction = (error.shouldNotifyUser()) ? "" :
	                        getString(error.getUserActionMessageId());
	                dialogBody = getString(R.string.error_authentication_retry, 
	                                       userAction);
	                listener = new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialogInterface, 
	                                        int i) {
	                        // Take the user to the mobile site.
	                        Intent intent = new Intent(Intent.ACTION_VIEW, 
	                                                   FACEBOOK_URL);
	                        startActivity(intent);
	                    }
	                };
	                break;

	            case AUTHENTICATION_REOPEN_SESSION:
	                // Close and reopen session
	                dialogBody = 
	                    getString(R.string.error_authentication_reopen);
	                listener = new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialogInterface, 
	                                        int i) {
	                        Session session = Session.getActiveSession();
	                        if (session != null && !session.isClosed()) {
	                            session.closeAndClearTokenInformation();
	                        }
	                    }
	                };
	                break;

	            case PERMISSION:
	                //  Permissions-related error
	                dialogBody = getString(R.string.error_permission);
	                listener = new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialogInterface, 
	                                        int i) {
	                        pendingAnnounce = true;
	                        // Request announce permission
	                        annoucePermissionRequest(Session.getActiveSession());
	                    }
	                };
	                break;

	            case SERVER:
	            case THROTTLING:
	                // This is usually temporary, don't clear the fields, and
	                // ask the user to try again.
	                dialogBody = getString(R.string.error_server);
	                break;

	            case BAD_REQUEST:
	                // This is likely a coding error, ask the user to file a bug.
	                dialogBody = getString(R.string.error_bad_request, 
	                                       error.getErrorMessage());
	                break;

	            case OTHER:
	            case CLIENT:
	            default:
	                // An unknown issue occurred, this could be a code error, or a server side issue or other issues
	                dialogBody = getString(R.string.error_unknown, 
	                                       error.getErrorMessage());
	                break;
	        }
	    }

	    // Show the error 
	    new AlertDialog.Builder(getActivity())
	            .setPositiveButton(R.string.error_dialog_button_text, listener)
	            .setTitle(R.string.error_dialog_title)
	            .setMessage(dialogBody)
	            .show();
	}
	private void userDataRequest(final Session session) {
	    // Make an API call to get user data and define a new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    // Set the id for the ProfilePictureView view to display the profile photo.
	                    profilePictureView.setProfileId(user.getId());
	                    // Set the Textview text to display the user name.
	                    userNameView.setText(user.getName());
	                }
	            }
	            if (response.getError() != null) {
	            	// Handle errors 
	                handleError(response.getError());
	            }
	        }
	    });
	    request.executeAsync();
	} 
	private void toStartPickerActivity(Uri someuri, int requestCode) {
	     Intent intent = new Intent();
	     intent.setData(someuri);
	     intent.setClass(getActivity(), PickerActivity.class);
	     startActivityForResult(intent, requestCode);
	 }    
    private void init(Bundle savedInstanceState){
		// Disable the button initially
	    announceButton.setEnabled(false);
	    // Set up the list view items
	    listElements = new ArrayList<BaseListElement>();
	    // Add an item for the talk picker
	    listElements.add(new TalkListElement(0));
	    // Add an item for the place picker
	    listElements.add(new LocationListElement(1));
	    // Add an item for the friend picker
	    listElements.add(new PeopleListElement(2));
	    if (savedInstanceState != null) {
	        // Restore the states
	        for (BaseListElement listElement : listElements) {
	            listElement.restoreState(savedInstanceState);
	        }  
	     // Restore the pending flag
	        pendingAnnounce = savedInstanceState.getBoolean(
	                PENDING_ANNOUNCE_KEY, false);
	    }
	    listView.setAdapter(new ActionListAdapter(getActivity(), 
	                        R.id.selection_list, listElements));
	    // Check for an open session
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	        userDataRequest(session);
	    }
	}
   
}
