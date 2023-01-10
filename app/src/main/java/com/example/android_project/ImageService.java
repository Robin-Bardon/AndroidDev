package com.example.android_project;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageService extends Service {
    private String TAG = "Service";
    private static String url = "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=";
    private String param = "cat";
    private int randomNumber;
    private List bitmaps = new ArrayList();
    private ServiceListener monListener;
    private final IBinder binder = new MonBinder();

    public class MonBinder extends Binder {
        ImageService getService() {
            // on renvoie l'instance de notre Service
            return ImageService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onCreate: Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");

        // Notre thread va dormir 5 secondes puis généré un nombre aléatoire
        Thread fetchPictures = new Thread(new Runnable() {
            public void run() {
                if(monListener != null) monListener.onThreadStateChange(true);
                Log.d(TAG,"Début du thread");

                try {
                    randomNumber = new Random().nextInt(100);

                    URL urlRequest = new URL(url + param);
                    HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
                    connection.setDoOutput(true);
                    int responseCode = connection.getResponseCode();
                    Log.d(TAG, "sendPOST: POST Response Code :: " + responseCode);

                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String responseLine = null;
                        StringBuilder response = new StringBuilder();

                        while ((responseLine = in.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        // print result

                        List links = new ArrayList();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        Bitmap bitmap;
                        InputStream inputStream = null;
                        for(int i=0; i<jsonResponse.getJSONArray("items").length(); i++) {
                            URL url = new URL(jsonResponse.getJSONArray("items").getJSONObject(i).getJSONObject("media").getString("m"));
                            try {
                                inputStream = url.openStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                links.add(bitmap);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }

                        setBitmaps(links);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,"Fin du thread");
                if(monListener != null) monListener.onThreadStateChange(false);
            }
        });
        fetchPictures.start();
        try {
            fetchPictures.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getRandomNumber() {
        return new Random().nextInt(bitmaps.size());
    }

    public List getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List bitmaps){
        this.bitmaps = bitmaps;
    }

    public void setMonListener(ServiceListener monListener) {
        this.monListener = monListener;
    }

    public void setParam(String param) {
        this.param = param;
    }
}