package com.example.android_project;

import android.app.appsearch.GetByDocumentIdRequest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class TroisiemeFragment extends Fragment {

    private String TAG = "TroisiemeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.troisieme_fragment, container, false);
        ListView list = rootView.findViewById(R.id.fav_list);
        ArrayList<Drawable> images = new ArrayList<Drawable>();
        MonAdapteur monImageAdapteur = new MonAdapteur(list.getContext(),images);

        System.out.println("Ma db from fragment");

        String select = new String("SELECT * from " + "mydatabase");
        Cursor cursor = ((MainActivity)getActivity()).getmDb().querryData("SELECT * from mydatabase");
        Log.i(TAG, "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                byte[] bimage = cursor.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bimage, 0, bimage.length);
                images.add(new BitmapDrawable(getResources(), bitmap));
            } while (cursor.moveToNext());
        }
        list.setAdapter(monImageAdapteur);

        return rootView;
    }
}