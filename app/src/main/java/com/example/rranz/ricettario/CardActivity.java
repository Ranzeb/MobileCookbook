package com.example.rranz.ricettario;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
public class CardActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);


        final GestioneDB db = new GestioneDB(this);

        db.open();
        Cursor mCursor = db.ottieniTutteRicette();
        int size = mCursor.getCount();

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutCardView);



        LayoutParams layoutparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );


        if(size > 0){
            if(mCursor != null && mCursor.moveToFirst()){
                do{
                    ImageView image = new ImageView(CardActivity.this);
                    image.setLayoutParams(layoutparams);
                    image.setBackgroundResource(R.drawable.ic_launcher_background);
                    layout.addView(image);
                    TextView text = new TextView(CardActivity.this);
                    text.setText(mCursor.getString(2));
                    text.setTextSize(18);


                    layout.addView(text);

                }while(mCursor.moveToNext());

            }
        }

    db.close();


    }
}
