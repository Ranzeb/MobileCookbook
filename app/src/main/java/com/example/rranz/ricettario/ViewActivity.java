package com.example.rranz.ricettario;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Scegli la categoria");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);

        CardView btnSearch = (CardView) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ViewActivity.this, SearchActivity.class);
                    intent.putExtra("selected", "Search");
                    startActivity(intent);

                }
        });
        //in base al bottone, mi sposto nella pagina di estrazione ricetta random

        //Interrogo il db per prendere il piatto random
        final GestioneDB db = new GestioneDB(this);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        db.open();

        //Bottone antipasti
        CardView btnAntipasti = (CardView) findViewById(R.id.btnAntipasti);
        btnAntipasti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            //Cursor mCursors = db.ottieniRicetta(22);

            Cursor mCursor = db.ottieniTutteRicetteByCategory("Antipasti");
            int size = mCursor.getCount();
            if(size == 0)
            {

                //mCursor.close();
                // set title
                alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna Ricetta per la categoria scelta!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else if(size > 0){

                Intent intent = new Intent(ViewActivity.this, SelectedActivity.class);
                intent.putExtra("selected", "Antipasti");
                startActivity(intent);
            }

        }
    });

    //Bottone Primi
    CardView btnPrimi = (CardView) findViewById(R.id.btnFirst);
    btnPrimi.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {




            Cursor mCursor = db.ottieniTutteRicetteByCategory("Primi");
            int size = mCursor.getCount();
            if(size == 0)
            {


                // set title
                alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna Ricetta per la categoria scelta!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else if(size > 0){
                // Perform action on click
                //mCursor.close();
                Intent intent = new Intent(ViewActivity.this, SelectedActivity.class);
                intent.putExtra("selected", "Primi");
                startActivity(intent);
            }


        }
    });


    //Bottone Secondi
    CardView btnSecondi = (CardView) findViewById(R.id.btnSecond);
    btnSecondi.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            //Cursor mCursors = db.ottieniRicetta(22);

            Cursor mCursor = db.ottieniTutteRicetteByCategory("Secondi");
            int size = mCursor.getCount();
            if(size == 0)
            {

                //mCursor.close();
                // set title
                alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna Ricetta per la categoria scelta!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else if(size > 0){
                // Perform action on click
                //mCursor.close();
                Intent intent = new Intent(ViewActivity.this, SelectedActivity.class);
                intent.putExtra("selected", "Secondi");
                startActivity(intent);
            }



        }
    });


    //Bottone Contorni
    CardView btnContorni = (CardView) findViewById(R.id.btnContorni);
    btnContorni.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            //Cursor mCursors = db.ottieniRicetta(22);

            Cursor mCursor = db.ottieniTutteRicetteByCategory("Contorni");
            int size = mCursor.getCount();
            if(size == 0)
            {

                //mCursor.close();
                // set title
                alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna Ricetta per la categoria scelta!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else if(size > 0){
                // Perform action on click
                //mCursor.close();
                Intent intent = new Intent(ViewActivity.this, SelectedActivity.class);
                intent.putExtra("selected", "Contorni");
                startActivity(intent);
            }

        }
    });


    //Bottone Dessert
    CardView btnDessert = (CardView) findViewById(R.id.btnDessert);
    btnDessert.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            //Cursor mCursors = db.ottieniRicetta(22);

            Cursor mCursor = db.ottieniTutteRicetteByCategory("Dolci");
            int size = mCursor.getCount();
            if(size == 0)
            {

                //mCursor.close();
                // set title
                alertDialogBuilder.setTitle("Ehi,si è verificato un problema");

                // set dialog message
                alertDialogBuilder.setMessage("Nessuna Ricetta per la categoria scelta!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else if(size > 0){
                // Perform action on click
                //mCursor.close();
                Intent intent = new Intent(ViewActivity.this, SelectedActivity.class);
                intent.putExtra("selected", "Dolci");
                startActivity(intent);
            }


         }
    });

    }
}



