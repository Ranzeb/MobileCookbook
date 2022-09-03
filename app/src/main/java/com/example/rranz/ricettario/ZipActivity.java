package com.example.rranz.ricettario;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipActivity extends AppCompatActivity {
    File imageToZip;


    private ArrayList<File> csvToDeleteList = new ArrayList<>();
    private ArrayList<File> imgToDeleteList = new ArrayList<>();

    private static final int BUFFER = 2048;
    private CompressFiles mCompressFiles;
    private ArrayList<String> mFilePathList = new ArrayList<>();
    private ArrayList<ArrayList> mFolderPathList = new ArrayList<>();
    private TextView mProgressView;
    private static File mediaStorageDir;
    private static String fileName;

    public static File getOutputZipFile(String fileName) {

        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Ricettario");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ZipActivity.this, MainActivity.class);

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
        setContentView(R.layout.activity_zip);

        mProgressView = (TextView) findViewById(R.id.progress_text_view);
        Button btnZip = (Button) findViewById(R.id.btn_zip);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));
       // getSupportActionBar().setTitle("Esporta");
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Esporta");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


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
                    ActivityCompat.requestPermissions(ZipActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

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
                }
            }
        });
    }

    private File addFilesToZip() {
        CSVWriter writer = null;

        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Ricettario");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }




        //Interrogo il db per prendere il piatto random
        final GestioneDB db = new GestioneDB(this);

        db.open();
        //Cursor mCursors = db.ottieniRicetta(22);


        Cursor mCursor = db.ottieniTutteRicette();

        int cursorSize = mCursor.getCount();

        Log.d("Query counter: "," " +cursorSize);
        if(cursorSize > 0) {
            if (mCursor != null && mCursor.moveToFirst()) {


                for (int i = 0; i < cursorSize; i++) {

                    int recipeIndex = mCursor.getInt(0);
                    String recipeCategory = mCursor.getString(1);
                    String recipeName = mCursor.getString(2);
                    String recipeIngredients = mCursor.getString(3);
                    String recipeDescription = mCursor.getString(4);
                    String recipeNPeople = mCursor.getString(5);
                    String recipeKcalPopup = mCursor.getString(9);
                    String recipeProteinPopup = mCursor.getString(10);
                    String recipeCarboPopup = mCursor.getString(11);
                    String recipeFatPopup = mCursor.getString(12);

                    fileName = "Ricettario" + ".zip";
//                    Log.d("fileName: ", fileName);

                    String csv = mediaStorageDir.getPath() + File.separator + recipeName + ".csv";
                    csvToDeleteList.add(new File(mediaStorageDir.getPath(), recipeName + ".csv"));


                    // Creazione csv da salvare
                    try {
                        writer = new CSVWriter(new FileWriter(csv));

                        List<String[]> data = new ArrayList<String[]>();
                        data.add(new String[]{"Nome ricetta", recipeName});
                        data.add(new String[]{"Numero persone", recipeNPeople});
                        data.add(new String[]{"Categoria", recipeCategory});
                        data.add(new String[]{"Ingredienti", recipeIngredients});
                        data.add(new String[]{"Descrizione", recipeDescription});
                        data.add(new String[]{"Kcal", recipeKcalPopup});
                        data.add(new String[]{"Proteine", recipeProteinPopup});
                        data.add(new String[]{"Carboidrati", recipeCarboPopup});
                        data.add(new String[]{"Grassi", recipeFatPopup});


                        writer.writeAll(data); // data is adding to csv

                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mFilePathList.add(csv);


                    Cursor fotoCursor = db.ottieniFoto(recipeIndex);

                    int fotoCursorSize = fotoCursor.getCount();

  //                  Log.d("Query counter: "," " +fotoCursorSize);
                    if(fotoCursorSize > 0) {
                        if (fotoCursor != null && fotoCursor.moveToFirst()) {


                            for(int j = 0; j < fotoCursorSize; j++) {
                                // Creazione immagine da salvare

                                String immagine = fotoCursor.getString(1);
                                if(!immagine.equals("")) {
                                    Bitmap selectedImage = StringToBitMap(immagine);

                                    String fname = recipeName + (j + 1) + ".jpg";

                                    imageToZip = new File(mediaStorageDir.getPath(), fname);
                                    imgToDeleteList.add(imageToZip);

    //                                Log.i("Creazione jpg", "" + imageToZip);
                                    if (imageToZip.exists())
                                        imageToZip.delete();
                                    try {
                                        FileOutputStream out = new FileOutputStream(imageToZip);
                                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                        out.flush();
                                        out.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String imageToAdd = mediaStorageDir.getPath() + File.separator + fname;
                                    mFilePathList.add(imageToAdd);
                                }
                                fotoCursor.moveToNext();
                            }
                        }
                    }


                    mFolderPathList.add(mFilePathList);

                    mCursor.moveToNext();
                }
            }


        }
        db.close();


    return mediaStorageDir;
    }

    //Function will get the call from compress function
    public void setCompressProgress(int filesCompressionCompleted) {
        mCompressFiles.publish(filesCompressionCompleted);
    }

    //Zipping function
    public void zip(String zipFilePath) {
        try {
      //      Log.d("path: ",zipFilePath);
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFilePath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];



                for(int j = 0; j < mFilePathList.size(); j++) {
                    setCompressProgress(j + 1);


                    FileInputStream fi = new FileInputStream(mFilePathList.get(j));
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(mFilePathList.get(j).substring(mFilePathList.get(j).lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    Log.d("giro", " " + j + " : " + mFilePathList.get(j));
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }


            out.close();
            for(int i = 0; i < csvToDeleteList.size(); i++)
                csvToDeleteList.get(i).delete();

            for(int i = 0; i < imgToDeleteList.size(); i++)
                imgToDeleteList.get(i).delete();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //zip() will be called from this AsyncTask as this is long task.
    private class CompressFiles extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

            try {
                mProgressView.setText("0% Completato");
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
                mProgressView.setText(Integer.toString(progress[0]) + "% Completato");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        protected void onPostExecute(Boolean flag) {
            Log.d("COMPRESS_TASK", "COMPLETED");
            mProgressView.setText("100 % Completato");
            Toast.makeText(getApplicationContext(), "Archiviazione completata", Toast.LENGTH_SHORT).show();
        }
    }
}


        /*      */

