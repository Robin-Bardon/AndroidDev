package com.example.android_project;

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
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PremierFragment extends Fragment {

    private String TAG = "PremierFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.premier_fragment, container, false);

        Log.d(TAG,"onCreateView");

        ListView list = rootView.findViewById(R.id.pict_list);
        ArrayList<Drawable> images = new ArrayList<Drawable>();
        MonAdapteur monImageAdapteur = new MonAdapteur(list.getContext(),images);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i);
                Bitmap p = (Bitmap) ((MainActivity)getActivity()).getLinks().get(i);
                ((MainActivity)getActivity()).getmDb().insertPicture(p);
                //((MainActivity)getActivity()).getmDb().readData();
            }
        });

        for (int i = 0; i < ((MainActivity)getActivity()).getLinks().size(); i++){
            Bitmap bitmap = (Bitmap) ((MainActivity)getActivity()).getLinks().get(i);
            images.add(new BitmapDrawable(getResources(), bitmap));
        }
        list.setAdapter(monImageAdapteur);

        return rootView;
    }
}