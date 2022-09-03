package com.example.rranz.ricettario;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

public class AddTags extends AppCompatActivity {


    //Torno indietro alla AddActivity con la freccia nella toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddTags.this, AddActivity.class);

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
        setContentView(R.layout.activity_add_tags);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       // getSupportActionBar().setTitle("Inserisci un tag");
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Inserisci un tag");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));

        Intent this_intent = getIntent();
        final String tagsOld = this_intent.getStringExtra("oldTags");
        final String ingredientsOld = this_intent.getStringExtra("oldIngredients");


        //Uri myUri = Uri.parse(this_intent.getStringExtra("imageUri"));
        final String UriFirst = this_intent.getStringExtra("imageUriFirst");
        final String UriSecond = this_intent.getStringExtra("imageUriSecond");
        final String UriThird = this_intent.getStringExtra("imageUriThird");


        final int height = this_intent.getIntExtra("height",0);

        final int width = this_intent.getIntExtra("width",0);



        Button btnSave= findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                TextInputEditText textName = (TextInputEditText) findViewById(R.id.tagsName);
                final String tagName = textName.getText().toString();

                Intent intent = new Intent(AddTags.this, AddActivity.class);


                if(!tagName.isEmpty()) {
                    final String tagsToAdd = tagsOld + tagName + ',';
                    intent.putExtra("TagsText", tagsToAdd);
                    intent.putExtra("IngredientText", ingredientsOld);

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
