package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_project.ImageService.MonBinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private String TAG = "MainActivity";
    ImageService mService;
    boolean mBound = false;
    private List links = new ArrayList();
    Context mContext;
    FragmentManager mManager;
    MaDataBase mDb;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // on est  bound à ExempleDeService
            // On a le binder local du service et à travers lui on
            // peut récupérer une instance de notre service
            Log.d(TAG,"onServiceConnected");
            MonBinder binder = (MonBinder) service;
            mService = binder.getService();

            //On enregistre un listener auprès de notre service
            mService.setMonListener(new ServiceListener() {

                // Cette méthode est notifiée depuis le thread il faut donc bien faire
                // attention à mettre à jours les textView sur le threadUI
                @Override
                public void onThreadStateChange(boolean running) {
                    TextView tState = findViewById(R.id.etat_thread);
                    if (running) {
                        tState.post(new Runnable() {
                            @Override
                            public void run() {
                                tState.setText("Le thread est en train d'être executé");
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

    public void monFragmentManager(Class fragmentClass) {
        Log.d(TAG, "monFragmentManager: Framgent called : "+ fragmentClass);
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }   catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "monFragmentManager: Framgent called : "+ fragment);
        mManager.beginTransaction().replace(R.id.mon_fragment_container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mDb = new MaDataBase(this);
        mDb.clearData();


        mManager = getSupportFragmentManager();
        FragmentTransaction maTransaction = mManager.beginTransaction();
        mManager.beginTransaction().replace(R.id.mon_fragment_container, new SecondFragment(),null).commit();



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Action Bar");
        actionBar.setSubtitle("Custom Action Bar");
        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // on se bind à notre service avec le flag BIND_AUTO_CREATE
        // il indique que la méthode bonService va directement créer
        // le service (onCreate) mais sans le démarrer
        // (on ne passera pas par onStartCommande)

        Intent intent = new Intent(mContext, ImageService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);



        /*Button monBouttonGetRandomNumber = findViewById(R.id.button2);
        monBouttonGetRandomNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ImageView imageView = (ImageView) findViewById(R.id.imageView);

                monManager.beginTransaction().replace(R.id.mon_fragment_container, new PremierFragment(),null).commit();
                if(mBound){
                    TextView tRand = findViewById(R.id.random_number);
                    tRand.setText(" Random Number généré par le thread : "+mService.getRandomNumber());
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
                //monManager.beginTransaction().replace(R.id.mon_fragment_container, new PremierFragment(),null).commit();
                mManager.beginTransaction().replace(R.id.mon_fragment_container, new SecondFragment(), null).commit();
                break;
            case R.id.favories:
                Toast.makeText(this, "Favories clicked", Toast.LENGTH_SHORT).show();
                mManager.beginTransaction().replace(R.id.mon_fragment_container, new TroisiemeFragment(),null).commit();
                break;
            case R.id.parametre:
                Toast.makeText(this, "Parameters clicked", Toast.LENGTH_SHORT).show();
                mManager.beginTransaction().replace(R.id.mon_fragment_container, new PremierFragment(),null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public int getRandomNumber() {
        return mService.getRandomNumber();
    }

    public List getLinks() {
        return mService.getBitmaps();
    }

    public void setLinks() {
        this.links = mService.getBitmaps();
    }

    public MaDataBase getmDb() {
        return mDb;
    }
}