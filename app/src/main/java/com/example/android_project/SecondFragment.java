package com.example.android_project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SecondFragment extends Fragment {

    private String TAG = "MainActivity";
    ImageService mService;
    boolean mBound = false;
    Context mContext;
    FragmentManager monManager;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.second_fragment, container, false);
        mContext = getActivity();
        Intent intent = new Intent(mContext, ImageService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Button monBouttonStartService = rootView.findViewById(R.id.button);
        monBouttonStartService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText mEdit;
                mEdit = (EditText) rootView.findViewById(R.id.editTextTextPersonName);
                mService.setParam(mEdit.getText().toString());
                mContext.startService(new Intent(mContext, ImageService.class));
                ((MainActivity)getActivity()).setLinks();
                mContext.stopService(new Intent(mContext, ImageService.class));
                ((MainActivity)getActivity()).monFragmentManager(new PremierFragment().getClass());
            }
        });

        return rootView;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // on est  bound à ExempleDeService
            // On a le binder local du service et à travers lui on
            // peut récupérer une instance de notre service
            Log.d(TAG,"onServiceConnected");
            ImageService.MonBinder binder = (ImageService.MonBinder) service;
            mService = binder.getService();

            //On enregistre un listener auprès de notre service
            mService.setMonListener(new ServiceListener() {

                // Cette méthode est notifiée depuis le thread il faut donc bien faire
                // attention à mettre à jours les textView sur le threadUI
                @Override
                public void onThreadStateChange(boolean running) {
                    TextView tState = rootView.findViewById(R.id.etat_thread);
                    if (running) {
                        tState.post(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("end of the world boys but not yet ...");
                                tState.setText("Chargement veuillez patienter ");
                            }
                        });
                    }else{
                        tState.post(new Runnable() {
                            @Override
                            public void run() {
                                tState.setText("Le Thread s'est arrêté");
                            }
                        });
                    }
                }
            });
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
