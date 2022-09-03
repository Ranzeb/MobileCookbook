package com.example.rranz.ricettario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SelectedActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private ViewPager mViewPager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(SelectedActivity.this, ViewActivity.class);

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

    String valueCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);


        //ottengo valore selezionato alla activity precedente, categoria piatto
        Intent this_intent = getIntent();
        valueCategory = this_intent.getStringExtra("selected");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(valueCategory);
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);




        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager,valueCategory);


                                                                                                                                                                                                                                                                                                                                                           }

    private void setupViewPager(ViewPager viewPager,String valueToSend) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Tab2Fragment fragmentToAdd = new Tab2Fragment();
        Bundle args = new Bundle();


        args.putString("Category",valueToSend);
        fragmentToAdd.setArguments(args);

        adapter.addFragment(fragmentToAdd);
        viewPager.setAdapter(adapter);
    }

}
