package com.example.rranz.ricettario;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {


    private ViewPager mViewPager;

    String valueCategory;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(SearchActivity.this, ViewActivity.class);

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
        setContentView(R.layout.activity_search_view);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));
        //  getSupportActionBar().setTitle("Inserisci la tua ricetta");
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Cerca la ricetta che vuoi");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView exportNotToShow = (ImageView) findViewById(R.id.imgZip);
        exportNotToShow.setVisibility(View.INVISIBLE);

        //ottengo valore selezionato alla activity precedente, categoria piatto
        Intent this_intent = getIntent();
        valueCategory = this_intent.getStringExtra("selected");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        final SearchView searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = s;
                Log.d("SearchText",s);
                setupViewPager(mViewPager,valueCategory,text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }

    private void setupViewPager(ViewPager viewPager,String valueToSend,String textToSend) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Tab2Fragment fragmentToAdd = new Tab2Fragment();
        Bundle args = new Bundle();


        args.putString("Category",valueToSend + " " +textToSend);
        args.putString("Tag",textToSend);
        fragmentToAdd.setArguments(args);

        adapter.addFragment(fragmentToAdd);
        viewPager.setAdapter(adapter);
    }

}
