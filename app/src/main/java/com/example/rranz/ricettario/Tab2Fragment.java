package com.example.rranz.ricettario;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;



public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private ListView mListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_layout,container,false);
        mListView = (ListView) view.findViewById(R.id.listView);

        Bundle args = getArguments();
        String element = args.getString("Category");

        String[] listOfArgs = element.split(" ");

        String index = listOfArgs[0];
        String tagToFind = "";
        if(index.equals("Search")) {
             tagToFind = listOfArgs[1];
        }

        ArrayList<Card> list = new ArrayList<>();


        //Interrogo il db per prendere il piatto random
        final GestioneDB db = new GestioneDB(getContext());

        db.open();
        //Cursor mCursors = db.ottieniRicetta(22);



        if(index.equals("Search")) {


            //Query db of tags

            Cursor tagCursor = db.ottieniRicettaByTag(tagToFind);

            int cursorSize = tagCursor.getCount();

            Log.d("CursorSize"," " +cursorSize);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            int tagIndex;
            //Uri image1Uri = null;
            String image1Uri = "";
            String recipeName = "";
            //Random rand = new Random();
            if (cursorSize > 0) {


                if (tagCursor != null && tagCursor.moveToFirst()) {

                    for (int i = 0; i < cursorSize; i++) {

                        //ottengo la ricetta randomizzata

                        tagIndex = tagCursor.getInt(2);
                        Cursor mCursor = db.ottieniRicetta(tagIndex);

                        int indexRecipe = mCursor.getInt(0);
                        String img1 = mCursor.getString(6);

                        Log.d("img to add: ",img1);

                        recipeName = mCursor.getString(2);
                        Log.d("Category2Fragment", i + " " + recipeName);

                        if (img1 != "") {
                            image1Uri = img1;
                        }

                        list.add(new Card(image1Uri, recipeName, indexRecipe));
                        mCursor.moveToNext();
                    }
                }


            }else{
                tagCursor.close();
                // set title
               alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna ricetta trovata!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }


        }else {
                Cursor mCursor = db.ottieniTutteRicetteByCategory(index);

                int cursorSize = mCursor.getCount();


                //Uri image1Uri = null;
                String image1Uri = "";
                String recipeName = "";
                Random rand = new Random();
                if (cursorSize > 0) {


                    if (mCursor != null && mCursor.moveToFirst()) {

                        for (int i = 0; i < cursorSize; i++) {

                            //ottengo la ricetta randomizzata


                            int indexRecipe = mCursor.getInt(0);
                            String img1 = mCursor.getString(6);

                            recipeName = mCursor.getString(2);
                            Log.d("Category2FragmentProva", i + " " + recipeName+ "img: "+ img1);

                            if (img1 != "") {
                                image1Uri = img1;
                            }

                            list.add(new Card(image1Uri, recipeName, indexRecipe));
                            mCursor.moveToNext();
                        }
                    }


            }
}
        db.close();

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.card_layout_main, list);
        mListView.setAdapter(adapter);

        return view;
    }
}