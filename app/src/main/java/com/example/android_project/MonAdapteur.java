package com.example.android_project;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MonAdapteur extends ArrayAdapter<Drawable> {



    public MonAdapteur(@NonNull Context context, ArrayList<Drawable> imgs) {
        super(context, R.layout.image_elem, imgs);
    }

    /***
     * Utilisé par le framework lors de la gestion d'une ListView
     * pour chaque item de la liste cette méthode sera appelé
     * @param position : position de l'item à afficher
     * @param convertView : vue de la liste (ici mon_element_image)
     * @param parent : vue parente de la liste (ici activity_main)
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de l'item à la position "position"
        // Les items sont récupérés depuis l'ArrayList passé dans le constructeur

        Drawable img = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_elem,parent,false);
        }

        // Ajout de notre image à la vue de notre liste
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewElement);
        imageView.setImageDrawable(img);

        return convertView;
    }
}
