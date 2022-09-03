package com.example.rranz.ricettario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_card);

      //  ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();


        //Aggiungi ricetta
        CardView btnAdd = (CardView) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

       //Ricetta Random
        CardView btnRandom = (CardView) findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this, DishSelectionActivity.class);
                startActivity(intent);
            }
        });

        //Vedi ricette
        CardView btnView = (CardView) findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });


        //Vedi ricette
        CardView btnZip = (CardView) findViewById(R.id.btnZip);
        btnZip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this, ZipActivity.class);
                startActivity(intent);
            }
        });



    }

}
