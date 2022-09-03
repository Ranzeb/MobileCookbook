package com.example.rranz.ricettario;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.example.rranz.ricettario.R.id.imageLayout2;

public class RandomActivity extends AppCompatActivity {


    String recipeKcalPopup;
    String recipeProteinPopup;
    String recipeCarboPopup;
    String recipeFatPopup;
    String recipeCategory;
    String recipeName;
    String recipeIngredients;
    String recipeDescription;
    String recipeNPeople;
    String img1;
    String img2;
    String img3;
    Bitmap selectedImage1;
    Bitmap selectedImage2;
    Bitmap selectedImage3;

    File imageFirst;
    File imageSecond;
    File imageThird;
    File csvToDelete;
    int recipeIndex;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(RandomActivity.this, DishSelectionActivity.class);

                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // The method that displays the popup.
    @SuppressLint("SetTextI18n")
    private void showStatusPopup(final Activity context, Point p) {

        // Inflate the popup_layout.xml

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, null);

        // Creating the PopupWindow
        PopupWindow changeStatusPopUp = new PopupWindow(context);
        changeStatusPopUp.setContentView(layout);
        changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -300;
        int OFFSET_Y = 80;

        //Clear the default translucent background
        changeStatusPopUp.setBackgroundDrawable(new BitmapDrawable());




        final GestioneDB db = new GestioneDB(RandomActivity.this);

        db.open();







        TextView recipeKcalView = (TextView) changeStatusPopUp.getContentView().findViewById(R.id.recipeKcal);
        recipeKcalView.setText(recipeKcalPopup+"g");
        TextView recipeProteinView = (TextView) changeStatusPopUp.getContentView().findViewById(R.id.recipeProtein);
        recipeProteinView.setText(recipeProteinPopup+"g");
        TextView recipeCarboView = (TextView) changeStatusPopUp.getContentView().findViewById(R.id.recipeCarbo);
        recipeCarboView.setText(recipeCarboPopup+"g");
        TextView recipeFatView = (TextView) changeStatusPopUp.getContentView().findViewById(R.id.recipeFat);
        recipeFatView.setText(recipeFatPopup+"g");



        // Displaying the popup at the specified location, + offsets.
        changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }



    private static final int BUFFER = 2048;
    private CompressFiles mCompressFiles;
    private ArrayList<String> mFilePathList = new ArrayList<>();

    private static File mediaStorageDir;
    private static String fileName;

    public static File getOutputZipFile(String fileName) {

        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Ricette");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }



    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        ImageView btnZip = (ImageView) findViewById(R.id.imgZip);
        btnZip.setOnClickListener(new View.OnClickListener() {
            private int REQUEST_CODE;

            @Override
            public void onClick(View view) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //File write logic here

                    //Add Files To Zip
                    alertDialogBuilder.setTitle("Attenzione");

                    // set dialog message
                    alertDialogBuilder.setMessage("Sei sicuro di volere esportare tutte le ricette?").setCancelable(false);


                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });


                    alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                            addFilesToZip();

                            mCompressFiles = new CompressFiles();
                            mCompressFiles.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }else{
                    ActivityCompat.requestPermissions(RandomActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                    //Add Files To Zip
                    alertDialogBuilder.setTitle("Attenzione");

                    // set dialog message
                    alertDialogBuilder.setMessage("Sei sicuro di volere esportare la ricetta?").setCancelable(false);


                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });


                    alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                            addFilesToZip();

                            mCompressFiles = new CompressFiles();
                            mCompressFiles.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }


        });

        ImageView imgValNutrizionali = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgValNutrizionali.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int[] location = new int[2];
                //int currentRowId = position;
               // View currentRow = v;
                // Get the x, y location and store it in the location[] array
                // location[0] = x, location[1] = y.
                v.getLocationOnScreen(location);

                //Initialize the Point with x, and y positions
                Point point = new Point();
                point.x = location[0];
                point.y = location[1];


                showStatusPopup(RandomActivity.this, point);

            }
        });


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Ricetta scelta per te");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));

        //ottengo valore selezionato alla activity precedente, categoria piatto
        Intent this_intent = getIntent();
        String value = this_intent.getStringExtra("selected");


        //Interrogo il db per prendere il piatto random
        final GestioneDB db = new GestioneDB(this);

        db.open();
        //Cursor mCursors = db.ottieniRicetta(22);


        Cursor mCursor = db.ottieniTutteRicetteByCategory(value);

        int recipeId = 0;
        int cursorSize = mCursor.getCount();
        Random rand = new Random();




        if(cursorSize > 0){


            int n = rand.nextInt(cursorSize); // Gives n such that 0 <= n < cursorSize
            Log.d("RicettaSelezionata"," " + n);
            if(mCursor != null && mCursor.moveToFirst()) {


                //ottengo la ricetta randomizzata
                mCursor.moveToPosition(n);

                recipeCategory = mCursor.getString(1);
                recipeIndex = mCursor.getInt(0);
                recipeName = mCursor.getString(2);
                recipeIngredients = mCursor.getString(3);
                recipeDescription = mCursor.getString(4);
                recipeNPeople = mCursor.getString(5);
                img1 = mCursor.getString(6);
                img2 = mCursor.getString(7);
                img3 = mCursor.getString(8);
                recipeKcalPopup = mCursor.getString(9);
                recipeProteinPopup = mCursor.getString(10);
                recipeCarboPopup = mCursor.getString(11);
                recipeFatPopup = mCursor.getString(12);


                //Setto valori in view
                TextView textName = (TextView) findViewById(R.id.recipeName);
                textName.setText(recipeName);

                TextView textIngredients = (TextView) findViewById(R.id.recipeIngredients);
                textIngredients.setText(recipeIngredients);

                TextView textNPeople = (TextView) findViewById(R.id.recipeNPersone);
                textNPeople.setText("per " + recipeNPeople + " persone");

                TextView textDescription = (TextView) findViewById(R.id.recipeDescription);
                textDescription.setText(recipeDescription);

            }
        }

        mCursor = db.ottieniFoto(recipeIndex);

        cursorSize = mCursor.getCount();


        if(cursorSize > 0){

            if(mCursor != null && mCursor.moveToFirst()) {



                for(int i = 0; i < cursorSize; i++){
                    String immagine = mCursor.getString(1);
                    mCursor.moveToNext();
                    if(immagine.equals("") && i == 1) {
                        LinearLayout layoutImg = (LinearLayout) findViewById(imageLayout2);
                        layoutImg.setVisibility(View.GONE);

                        Log.d("removeView",i + " removed");
                    }

                    if(immagine.equals("") && i == 2){
                        LinearLayout layoutImg = (LinearLayout) findViewById(R.id.imageLayout3);
                        layoutImg.setVisibility(View.GONE);
                        Log.d("removeView",i+ " removed");
                    }

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int screenHeight = displayMetrics.heightPixels;
                    int screenWidth = displayMetrics.widthPixels;

                    if(!immagine.equals("") && i == 0)  {

                        selectedImage1 = StringToBitMap(immagine);
                        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);

                        Bitmap scaledBitmap = scaleDown(selectedImage1, screenWidth-30, true);

                        imageView1.setImageBitmap(scaledBitmap);
                    }else if(!immagine.equals("") && i == 1){
                        selectedImage2 = StringToBitMap(immagine);
                        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);

                        Bitmap scaledBitmap = scaleDown(selectedImage2, screenWidth-30, true);
                        imageView2.setImageBitmap(scaledBitmap);
                    }else if(!immagine.equals("") && i == 2){
                        selectedImage3 = StringToBitMap(immagine);
                        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

                        Bitmap scaledBitmap = scaleDown(selectedImage3, screenWidth-30, true);
                        imageView3.setImageBitmap(scaledBitmap);
                    }


                }

            }

        }

        db.close();



        }

    private File addFilesToZip() {
        CSVWriter writer = null;

        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Ricette");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }




        //Interrogo il db per prendere il piatto random
        final GestioneDB db = new GestioneDB(this);





                Log.d("recipeName",""+recipeName);
                fileName = recipeName + ".zip";


                String csv = mediaStorageDir.getPath() + File.separator + recipeName +".csv";

                csvToDelete = new File(mediaStorageDir.getPath(), recipeName + ".csv");

                // Creazione csv da salvare
                try {

                    writer = new CSVWriter(new FileWriter(csv));

                    Log.i("Creazione csv", "" + csv);

                    List<String[]> data = new ArrayList<String[]>();
                    data.add(new String[]{"Nome ricetta", recipeName});
                    data.add(new String[]{"Numero persone",recipeNPeople});
                    data.add(new String[]{"Categoria",recipeCategory});
                    data.add(new String[]{"Ingredienti",recipeIngredients});
                    data.add(new String[]{"Descrizione",recipeDescription});
                    data.add(new String[]{"Kcal",recipeKcalPopup});
                    data.add(new String[]{"Proteine",recipeProteinPopup});
                    data.add(new String[]{"Carboidrati",recipeCarboPopup});
                    data.add(new String[]{"Grassi",recipeFatPopup});




                    writer.writeAll(data); // data is adding to csv

                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mFilePathList.add(csv);




                // Creazione immagine da salvare

                String fname = recipeName+ "1.jpg";
                imageFirst = new File(mediaStorageDir.getPath(), fname);

                Log.i("Creazione jpg", "" + imageFirst);
                if (imageFirst.exists())
                    imageFirst.delete();
                try {
                    FileOutputStream out = new FileOutputStream(imageFirst);
                    selectedImage1.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String image1 = mediaStorageDir.getPath() + File.separator + fname;
                mFilePathList.add(image1);


                fname = recipeName+ "2.jpg";
                imageSecond= new File(mediaStorageDir.getPath(), fname);

                Log.i("Creazione jpg", "" + imageSecond);
                if (imageSecond.exists())
                    imageSecond.delete();
                try {
                    FileOutputStream out = new FileOutputStream(imageSecond);
                    selectedImage2.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String image2 = mediaStorageDir.getPath() + File.separator + fname;
                mFilePathList.add(image2);


                fname = recipeName+ "3.jpg";
                imageThird = new File(mediaStorageDir.getPath(), fname);

                Log.i("Creazione jpg", "" + imageThird);
                if (imageThird.exists())
                    imageThird.delete();
                try {
                    FileOutputStream out = new FileOutputStream(imageThird);
                    selectedImage3.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String image3 = mediaStorageDir.getPath() + File.separator + fname;
                mFilePathList.add(image3);






        return mediaStorageDir;
    }

    //Function will get the call from compress function
    public void setCompressProgress(int filesCompressionCompleted) {
        mCompressFiles.publish(filesCompressionCompleted);
    }

    //Zipping function
    public void zip(String zipFilePath) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFilePath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (int i = 0; i < mFilePathList.size(); i++) {

                setCompressProgress(i + 1);

                FileInputStream fi = new FileInputStream(mFilePathList.get(i));
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(mFilePathList.get(i).substring(mFilePathList.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();

            csvToDelete.delete();
            imageFirst.delete();
            imageSecond.delete();
            imageThird.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //zip() will be called from this AsyncTask as this is long task.
    private class CompressFiles extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

            try {
              //  mProgressView.setText("0% Completed");
            } catch (Exception ignored) {
            }
        }

        protected Boolean doInBackground(Void... urls) {

            File file = getOutputZipFile(fileName);

            String zipFileName;
            if (file != null) {
                zipFileName = file.getAbsolutePath();

                if (mFilePathList.size() > 0) {
                    zip(zipFileName);
                }
            }


            return true;
        }

        public void publish(int filesCompressionCompleted) {
            int totalNumberOfFiles = mFilePathList.size();
            publishProgress((100 * filesCompressionCompleted) / totalNumberOfFiles);
        }

        protected void onProgressUpdate(Integer... progress) {

            try {
                //mProgressView.setText(Integer.toString(progress[0]) + "% Completed");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        protected void onPostExecute(Boolean flag) {

            //mProgressView.setText("100 % Completed");
            Toast.makeText(getApplicationContext(), "Archiviazione completata", Toast.LENGTH_SHORT).show();
        }
    }
}




