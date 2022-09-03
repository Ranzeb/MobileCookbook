package com.example.rranz.ricettario;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddIngredients extends AppCompatActivity {


    //Torno indietro alla AddActivity con la freccia nella toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddIngredients.this, AddActivity.class);

                              /*  TextInputEditText textName = (TextInputEditText) findViewById(R.id.recipeName);
                                textName.setText("");
*/

                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getSupportActionBar().setTitle("Inserisci un ingrediente");

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Inserisci un ingrediente");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));



        Intent this_intent = getIntent();
        final String ingredientOld = this_intent.getStringExtra("oldIngredients");
        final String tagsOld = this_intent.getStringExtra("oldTags");

        final String UriFirst = this_intent.getStringExtra("imageUriFirst");
        final String UriSecond = this_intent.getStringExtra("imageUriSecond");
        final String UriThird = this_intent.getStringExtra("imageUriThird");

        final int height = this_intent.getIntExtra("height",0);

        final int width = this_intent.getIntExtra("width",0);


        Button btnSave= findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                TextInputEditText textName = (TextInputEditText) findViewById(R.id.ingredientName);
                final String ingredientName = textName.getText().toString();

                TextInputEditText textQuantity = (TextInputEditText) findViewById(R.id.ingredientQuantity);
                final String ingredientQuantity = textQuantity.getText().toString();

                Intent intent = new Intent(AddIngredients.this, AddActivity.class);

                if(!ingredientName.isEmpty() || !ingredientQuantity.isEmpty()) {

                    final String ingredientToChange = ingredientOld + '\n' + ingredientQuantity + ' ' + ingredientName + '\n';
                    intent.putExtra("IngredientText", ingredientToChange);
                    intent.putExtra("TagsText", tagsOld);
                }
                intent.putExtra("width",width);
                intent.putExtra("height",height);
                intent.putExtra("imageFirstToAdd",UriFirst);
                intent.putExtra("imageSecondToAdd",UriSecond);
                intent.putExtra("imageThirdToAdd",UriThird);

                startActivity(intent);

            }

        });



    }


}
