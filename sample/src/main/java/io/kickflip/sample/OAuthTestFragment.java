package io.kickflip.sample;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.kickflip.sdk.APICallback;
import io.kickflip.sdk.AWSCredentials;
import io.kickflip.sdk.FFmpegWrapper;
import io.kickflip.sdk.KickflipAPIClient;
import io.kickflip.sdk.Util;


/**
 * This Fragment demonstrates use of KickflipAPIClient,
 * which allows you to manage your Kickflip account and users
 */
public class OAuthTestFragment extends Fragment {
    private static final String TAG = "OAuthTestFragment";

    private static final String ARG_CLIENT_KEY = "key";
    private static final String ARG_CLIENT_SECRET = "secret";

    private String mClientKey;
    private String mClientSecret;
    private KickflipAPIClient mKickflipClient;

    private OAuthTestFragmentListener mListener;


    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // KickflipAPIClient automatically fetches credentials for a new user on creation
            // or retrieves cached credentials
            setupKickflip();
        }
    };

    /** Convenience method for creating Fragment instance
     * with arguments
     * @param clientKey Client key
     * @param clientSecret Client secret
     * @return
     */
    public static OAuthTestFragment newInstance(String clientKey, String clientSecret) {
        OAuthTestFragment fragment = new OAuthTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLIENT_KEY, clientKey);
        args.putString(ARG_CLIENT_SECRET, clientSecret);
        fragment.setArguments(args);
        return fragment;
    }

    public OAuthTestFragment() {
        // Required empty public constructor
    }

    /**
     * This method creates a KickflipAPIClient instance,
     * which automatically authenticates and acquires access
     * credentials for video storage. Depending on your plan
     * these may be AWSCredentials or an RTMP endpoint.
     */
    public void setupKickflip(){
        if(mKickflipClient != null) return;

        Context applicationContext = getActivity().getApplicationContext();
        mKickflipClient = new KickflipAPIClient(applicationContext, mClientKey, mClientSecret, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(getActivity(), "Success: " + ((AWSCredentials) response).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(getActivity(), "Error: " + (response).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientKey = getArguments().getString(ARG_CLIENT_KEY);
            mClientSecret = getArguments().getString(ARG_CLIENT_SECRET);
        }else{
            Log.w(TAG, "No client credentials provided! This fragment won't do anything");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_oauth_test, container, false);
        root.findViewById(R.id.oauthButton).setOnClickListener(buttonClickListener);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OAuthTestFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OAuthTestFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OAuthTestFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
