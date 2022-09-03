package com.example.rranz.ricettario;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static com.example.rranz.ricettario.R.layout.activity_add;

public class AddActivity extends AppCompatActivity {

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


    private boolean checkToClean = false;
    private boolean imgFirstValue = false;
    private boolean imgSecondValue = false;
    private boolean imgThirdValue = false;
    private static final String PREFS_NAME ="MyPrefsFile" ;
    private boolean switchChecked = false;
    private Uri UriFirstToSend;
    private Uri UriSecondToSend;
    private Uri UriThirdToSend;

    private void scaleImage(ImageView view, int ImageViewNumber) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            // bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        ImageView imageViewToModify = null;

        if(ImageViewNumber == 1){
            imageViewToModify  = (ImageView) findViewById(R.id.imageView1) ;
        }

        int imageViewWidth = imageViewToModify.getWidth();
        int imageViewHeight = imageViewToModify.getHeight();


        int height = bitmap.getHeight();
        int boundingWidth = dpToPx(100);
        int boundingHeight = dpToPx(100);
        Log.i("Test", "original width = " + Integer.toString(imageViewWidth));
        Log.i("Test", "original height = " + Integer.toString(imageViewHeight));


        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundingWidth) / width;
        float yScale = ((float) boundingHeight) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    private static final int RESULT_LOAD_IMAGE_FIRST = 1 ;
    private static final int RESULT_LOAD_IMAGE_SECOND = 2 ;
    private static final int RESULT_LOAD_IMAGE_THIRD = 3 ;

    private String imageUri1;
    private String imageUri2;
    private String imageUri3;

    //servono per upload immagine



    private Uri mImageCaptureUri;

    int newWidth;
    int newHeight;
    boolean firstTime = true;

    /*
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,60, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);




        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE_FIRST) {
            try {
                if(firstTime == true) {
                    newWidth = findViewById(R.id.imageView1).getWidth();
                    newHeight = findViewById(R.id.imageView1).getHeight();
                    firstTime = false;
                }

                final Uri imageUri = data.getData();
                imgFirstValue = true;

                UriFirstToSend = imageUri;

                Log.d("add","uri: " + imageUri);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);

                //compressing
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));


                imageUri1 = BitMapToString(compressedSelectedImage);



                Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
                imageView1.setImageBitmap(resized);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText( this,"Something went wrong", Toast.LENGTH_LONG).show();

            }
        }else if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE_SECOND) {
            try {
                if(firstTime == true) {
                    newWidth = findViewById(R.id.imageView2).getWidth();
                    newHeight = findViewById(R.id.imageView2).getHeight();
                    firstTime = false;
                }

                final Uri imageUri = data.getData();
                imgSecondValue = true;

                UriSecondToSend = imageUri;

                Log.d("add","uri: " + UriSecondToSend);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);


                //compressing
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));


                Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
                imageView2.setImageBitmap(resized);

                imageUri2 = BitMapToString(compressedSelectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText( this,"Something went wrong", Toast.LENGTH_LONG).show();

            }
        }else if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE_THIRD) {
            try {
                if(firstTime == true) {
                    newWidth = findViewById(R.id.imageView3).getWidth();
                    newHeight = findViewById(R.id.imageView3).getHeight();
                    firstTime = false;
                }

                final Uri imageUri = data.getData();
                imgThirdValue = true;

                UriThirdToSend = imageUri;


                Log.d("add", "uri: " + imageUri);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);


                //compressing
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));


                imageUri3 = BitMapToString(compressedSelectedImage);


                Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
                imageView3.setImageBitmap(resized);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }else {
            Toast.makeText( this,"You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(AddActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add);
        checkToClean = false;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        /**************TOOLBAR********************************/
      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inserisci la tua ricetta");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));



        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(14,129,209)));
      //  getSupportActionBar().setTitle("Inserisci la tua ricetta");
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Inserisci la tua ricetta");
        ImageView imgNotToShow = (ImageView) findViewById(R.id.imgValoriNutrizionali);
        imgNotToShow.setVisibility(View.INVISIBLE);
        ImageView saveNotToShow = (ImageView) findViewById(R.id.imgZip);
        saveNotToShow.setVisibility(View.INVISIBLE);



        /******************** FINE TOOLBAR ********************************/

        /******************** SPINNER *************************************/
        Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

       // spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);





        Switch switchKcal = findViewById(R.id.switchKcal);

        //listener per switch kcal

        switchKcal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked == true){
                    //inserisco elements
                    switchChecked = true;
                    /************ KCAL input **************/
                    RelativeLayout layoutToAdd = findViewById(R.id.nutritionLayout);

                    RelativeLayout.LayoutParams layoutParamsContainer = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextInputLayout kcalLayout = new TextInputLayout(AddActivity.this);


                    kcalLayout.setId(R.id.kcalText);
                    kcalLayout.setLayoutParams(layoutParamsContainer);


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);



                    layoutParams.leftMargin = 100;
                    layoutParams.rightMargin = 100;

                    TextInputEditText inputKcal = new TextInputEditText(AddActivity.this);

                    inputKcal.setId(R.id.kcalInput);
                    inputKcal.setHint("Kcal/100g");
                    inputKcal.setTextColor(Color.BLACK);
                    inputKcal.setHintTextColor(Color.GRAY);
                    inputKcal.setTypeface(Typeface.DEFAULT_BOLD);
                    inputKcal.setAllCaps(true);
                    inputKcal.setLayoutParams(layoutParams);

                    kcalLayout.addView(inputKcal);
                    layoutToAdd.addView(kcalLayout);


                    /*********** END KCAL INPUT ***********/

                    /************ PROTEIN INPUT **************/

                    RelativeLayout.LayoutParams layoutParamsContainerProtein = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextInputLayout proteinLayout = new TextInputLayout(AddActivity.this);
                    proteinLayout.setId(R.id.proteinText);
                    layoutParamsContainerProtein.addRule(RelativeLayout.BELOW,kcalLayout.getId());
                    proteinLayout.setLayoutParams(layoutParamsContainerProtein);


                    LinearLayout.LayoutParams layoutParamsProtein = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParamsProtein.leftMargin = 100;
                    layoutParamsProtein.rightMargin = 100;

                    TextInputEditText inputProtein = new TextInputEditText(AddActivity.this);

                    inputProtein.setId(R.id.proteinInput);
                    inputProtein.setHint("Proteine/100g");
                    inputProtein.setTextColor(Color.BLACK);
                    inputProtein.setHintTextColor(Color.GRAY);
                    inputProtein.setTypeface(Typeface.DEFAULT_BOLD);
                    inputProtein.setAllCaps(true);
                    inputProtein.setLayoutParams(layoutParamsProtein);

                    proteinLayout.addView(inputProtein);
                    layoutToAdd.addView(proteinLayout);

                    /*********** END PROTEIN INPUT ***********/


                    /************ CARBO INPUT **************/
                    RelativeLayout.LayoutParams layoutParamsContainerCarbo = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );


                    TextInputLayout carboLayout = new TextInputLayout(AddActivity.this);
                    carboLayout.setId(R.id.carboText);
                    layoutParamsContainerCarbo.addRule(RelativeLayout.BELOW,proteinLayout.getId());
                    carboLayout.setLayoutParams(layoutParamsContainerCarbo);

                    LinearLayout.LayoutParams layoutParamsCarbo = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);


                    layoutParamsCarbo.leftMargin = 100;
                    layoutParamsCarbo.rightMargin = 100;

                    TextInputEditText inputCarbo = new TextInputEditText(AddActivity.this);

                    inputCarbo.setId(R.id.carboInput);
                    inputCarbo.setHint("Carboidrati/100g");
                    inputCarbo.setTextColor(Color.BLACK);
                    inputCarbo.setHintTextColor(Color.GRAY);
                    inputCarbo.setTypeface(Typeface.DEFAULT_BOLD);
                    inputCarbo.setAllCaps(true);
                    inputCarbo.setLayoutParams(layoutParamsCarbo);

                    carboLayout.addView(inputCarbo);
                    layoutToAdd.addView(carboLayout);




                    /*********** END CARBO INPUT ***********/



                    /************ FAT INPUT **************/
                    RelativeLayout.LayoutParams layoutParamsContainerFat = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );


                    TextInputLayout fatLayout = new TextInputLayout(AddActivity.this);
                    fatLayout.setId(R.id.fatText);
                    layoutParamsContainerFat.addRule(RelativeLayout.BELOW,carboLayout.getId());
                    fatLayout.setLayoutParams(layoutParamsContainerFat);

                    LinearLayout.LayoutParams layoutParamsFat = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);


                    layoutParamsFat.leftMargin = 100;
                    layoutParamsFat.rightMargin = 100;

                    TextInputEditText inputFat = new TextInputEditText(AddActivity.this);

                    inputFat.setId(R.id.fatInput);
                    inputFat.setHint("Grassi/100g");
                    inputFat.setTextColor(Color.BLACK);
                    inputFat.setHintTextColor(Color.GRAY);
                    inputFat.setTypeface(Typeface.DEFAULT_BOLD);
                    inputFat.setAllCaps(true);
                    inputFat.setLayoutParams(layoutParamsFat);

                    fatLayout.addView(inputFat);
                    layoutToAdd.addView(fatLayout);

                    /*********** END FAT INPUT ***********/

                }
                else{
                    //tolgo elements

                    switchChecked = false;
                    TextInputLayout toDelete = findViewById(R.id.fatText);
                    RelativeLayout layoutToDelete = findViewById(R.id.nutritionLayout);
                    layoutToDelete.removeView(toDelete);


                    toDelete = findViewById(R.id.carboText);
                    layoutToDelete.removeView(toDelete);

                    toDelete = findViewById(R.id.proteinText);
                    layoutToDelete.removeView(toDelete);

                    toDelete = findViewById(R.id.kcalText);
                    layoutToDelete.removeView(toDelete);

                }
            }
        });


        /********************** FINE SPINNER *****************************/



 /*************************** GESTIONE UPLOAD IMMAGINI **************************/
        final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
            imageView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /* Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE_FIRST);
*/              Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);

                checkToClean = true;
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_FIRST);
            }
        });

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);

                checkToClean = true;
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_SECOND);

            }
        });

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);

                checkToClean = true;
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_THIRD);

            }
        });




 /************************* FINE GESTIONE UPLOAD IMMAGINI ***********************/



 /******************* SALVATAGGIO *********************************************/

                Button btnSave = (Button) findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {


                        final GestioneDB db = new GestioneDB(AddActivity.this);
                        db.open();
                        TextInputEditText textName = (TextInputEditText) findViewById(R.id.recipeName);
                        final String recipeName = textName.getText().toString();

                        TextInputEditText textNumPeople = (TextInputEditText) findViewById(R.id.recipeNPeople);
                        final String recipeNPeople = textNumPeople.getText().toString();


                        Spinner mySpinner = (Spinner) findViewById(R.id.categorySpinner);
                        String recipeCategory = mySpinner.getSelectedItem().toString();


                        TextView textMultiIngredients = (TextView) findViewById(R.id.ingredientMultiLine);
                        String multiIngredients = textMultiIngredients.getText().toString();

                        TextView textMultiTags = (TextView) findViewById(R.id.tagsLine);
                        String multiTags = textMultiTags.getText().toString();

                        TextInputEditText textDescription = (TextInputEditText) findViewById(R.id.recipeDescription);
                        final String recipeDescription = textDescription.getText().toString();

                        String recipeKcal = "";
                        String recipeProtein = "";
                        String recipeCarbo = "";
                        String recipeFat = "";

                        if (switchChecked == true) {
                            TextInputEditText textKcal = (TextInputEditText) findViewById(R.id.kcalInput);
                            recipeKcal = textKcal.getText().toString();

                            TextInputEditText textProtein = (TextInputEditText) findViewById(R.id.proteinInput);
                            recipeProtein = textProtein.getText().toString();

                            TextInputEditText textCarbo = (TextInputEditText) findViewById(R.id.carboInput);
                            recipeCarbo = textCarbo.getText().toString();

                            TextInputEditText textFat = (TextInputEditText) findViewById(R.id.fatInput);
                            recipeFat = textFat.getText().toString();
                        }



                        String img1 = "";
                        String img2 = "";
                        String img3 = "";

                        if (imageUri1 != null) {
                            //img1 = imageUri1.toString();
                            img1 = imageUri1;
                        }
                        if (imageUri2 != null) {
                            img2 = imageUri2;
                        }
                        if (imageUri3 != null) {
                            img3 = imageUri3;
                        }

                        /*******************da modificare*********************/
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddActivity.this);

                        if (TextUtils.isEmpty(textName.getText())) {
                            textName.setError("Inserisci il nome");
                        }else if(TextUtils.isEmpty(textNumPeople.getText())){
                            textNumPeople.setError("Inserisci il numero di persone");
                        }else if(TextUtils.isEmpty(textMultiIngredients.getText())) {
                            textMultiIngredients.setError("Inserisci ingredienti");
                        }else if(TextUtils.isEmpty(textMultiTags.getText())) {
                            textMultiTags.setError("Inserisci tags");
                        }else if(TextUtils.isEmpty(textDescription.getText())) {
                            textDescription.setError("Inserisci la descrizione");
                        }else if(TextUtils.isEmpty(img1)) {
                            alertDialogBuilder.setTitle("Attenzione");

                            // set dialog message
                            alertDialogBuilder.setMessage("Almeno la prima foto va inserita!").setCancelable(false);



                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }else{
                            //riempio la tabella delle ricette
                            long id = (long) db.inserisciRicetta(recipeName, recipeCategory, multiIngredients, recipeDescription, recipeNPeople, recipeKcal, recipeCarbo, recipeProtein, recipeFat, img1, img2, img3);


                            int idToAddTags = 0;

                            Cursor mCursor = db.ottieniUltimaRicetta();
                            if (mCursor != null && mCursor.moveToFirst()) {
                                idToAddTags = mCursor.getInt(0);
                            }

                            String[] tagList = multiTags.split(",");

                            for (int i = 0; i < tagList.length; i++) {

                                id = (long) db.inserisciTags(tagList[i], idToAddTags);

                            }

                            id = (long) db.inserisciTags(recipeName, idToAddTags);


                            String[] ingredientList = multiIngredients.split("\n");

                            for (int i = 0; i < ingredientList.length; i++) {

                                id = (long) db.inserisciTags(ingredientList[i], idToAddTags);

                            }


                            id = (long) db.inserisciFoto(img1, idToAddTags);
                            id = (long) db.inserisciFoto(img2, idToAddTags);
                            id = (long) db.inserisciFoto(img3, idToAddTags);


                            //Aggiornato con tag nome e ingredienti
                            db.close();
                            //riempio la tabella dei tags (tag,nome,ingredienti)


                            Snackbar snackbar = Snackbar
                                    .make(v, "Ricetta salvata con successo!", Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {

                                    db.close();
                                    Intent intent = new Intent(AddActivity.this, MainActivity.class);

                              /*  TextInputEditText textName = (TextInputEditText) findViewById(R.id.recipeName);
                                textName.setText("");
*/

                                    startActivity(intent);
                                }
                            }, 2000);   //5 seconds
                            // Perform action on click


                        }
                    }
                    });



/***************************** FINE SALVATAGGIO   ***************************************/



/**************************** GESTIONE INGREDIENTI **********************************/

        Intent this_intent = getIntent();


        int width = this_intent.getIntExtra("width",0);
        int height = this_intent.getIntExtra("height",0);

        newWidth = width;
        newHeight = height;


        // Prima immagine
        String imgToAddFirst = this_intent.getStringExtra("imageFirstToAdd");

        if(imgToAddFirst != null) {
            UriFirstToSend = Uri.parse(imgToAddFirst);


            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(UriFirstToSend);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ImageView imageView = (ImageView) findViewById(R.id.imageView1);

            //compressing
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));

            imageUri1 = BitMapToString(compressedSelectedImage);

            Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
            imageView.setImageBitmap(resized);
        }

        // Seconda immagine
        String imgToAddSecond = this_intent.getStringExtra("imageSecondToAdd");

        Log.d("imgToAddSecond",""+imgToAddSecond);
        if(imgToAddSecond != null) {
            UriSecondToSend = Uri.parse(imgToAddSecond);


            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(UriSecondToSend);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);

            //compressing
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));

            imageUri2 = BitMapToString(compressedSelectedImage);

            Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
            imageView.setImageBitmap(resized);
        }

        // Terza immagine
        String imgToAddThird = this_intent.getStringExtra("imageThirdToAdd");

        if(imgToAddThird != null) {
            UriThirdToSend = Uri.parse(imgToAddThird);


            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(UriThirdToSend);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ImageView imageView = (ImageView) findViewById(R.id.imageView3);

            //compressing
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));

            imageUri3 = BitMapToString(compressedSelectedImage);

            Bitmap resized = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, true);
            imageView.setImageBitmap(resized);
        }


        String ingredientText = this_intent.getStringExtra("IngredientText");
      //  String ingredientQuantity = this_intent.getStringExtra("IngredientQuantity");

        if (ingredientText != null) {

            TextView textIngredients = (TextView) findViewById(R.id.ingredientMultiLine);
            textIngredients.setText(ingredientText);
        }

        ImageButton btnIngredients = (ImageButton) findViewById(R.id.ingredientsButton);
        btnIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AddIngredients.class);
                intent.putExtra("width",newWidth);
                intent.putExtra("height",newHeight);

                // da provare
                if(imageUri1 != null) {
                    //String image1 = imageUri1;
                    Log.d("uritosend",UriFirstToSend.toString());
                    intent.putExtra("imageUriFirst",UriFirstToSend.toString());
                }

                if(imageUri2 != null){
                    intent.putExtra("imageUriSecond",UriSecondToSend.toString());
                }

                if(imageUri3 != null){
                    intent.putExtra("imageUriThird",UriThirdToSend.toString());
                }


                TextView textIngredients = (TextView) findViewById(R.id.ingredientMultiLine);
                String saveOldIngredients = textIngredients.getText().toString();
                if(saveOldIngredients != null) {
                    intent.putExtra("oldIngredients", saveOldIngredients);
                }


                TextView textTags = (TextView) findViewById(R.id.tagsLine);
                String saveOldTags = textTags.getText().toString();
                if(saveOldTags != null) {
                    intent.putExtra("oldTags", saveOldTags);
                }

                checkToClean = true;
                startActivity(intent);

            }
        });
/****************************** FINE GESTIONE INGREDIENTI ****************************/


/******************************** GESTIONE TAGS **************************************/




        String tagsText = this_intent.getStringExtra("TagsText");
        //  String ingredientQuantity = this_intent.getStringExtra("IngredientQuantity");

        if (tagsText != null) {

            TextView textTags = (TextView) findViewById(R.id.tagsLine);
            textTags.setText(tagsText);
        }

        ImageButton btnTags = (ImageButton) findViewById(R.id.tagsButton);
        btnTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AddTags.class);
                TextView textTags = (TextView) findViewById(R.id.tagsLine);
                String saveOldTags = textTags.getText().toString();

                intent.putExtra("width",newWidth);
                intent.putExtra("height",newHeight);

                // da provare
                if(imageUri1 != null) {
                    //String image1 = imageUri1;
                    Log.d("uritosend",UriFirstToSend.toString());
                   intent.putExtra("imageUriFirst",UriFirstToSend.toString());
                }

                if(imageUri2 != null){
                    intent.putExtra("imageUriSecond",UriSecondToSend.toString());
                }

                if(imageUri3 != null){
                    intent.putExtra("imageUriThird",UriThirdToSend.toString());
                }

                if(saveOldTags != null) {
                    intent.putExtra("oldTags", saveOldTags);
                }

                TextView textIngredients = (TextView) findViewById(R.id.ingredientMultiLine);
                String saveOldIngredients = textIngredients.getText().toString();
                if(saveOldIngredients != null) {
                    intent.putExtra("oldIngredients", saveOldIngredients);

                }

                checkToClean = true;
                startActivity(intent);

            }
        });






/****************************** FINE GESTIONE TAGS ***********************************/


/****************************** Gestione immagini tra activity ***********************/


String imageToAdd = this_intent.getStringExtra("image1");

if(imageToAdd != null){
    ImageView imageView = (ImageView) findViewById(R.id.imageView1);
    Bitmap bitmapToAdd = StringToBitMap(imageToAdd);
    imageView.setImageBitmap(bitmapToAdd);
}



/****************************** Fine gestione immagini tra activity ******************/
    }


    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        // Necessary to clear first if we save preferences onPause.



        editor.clear();
        if(checkToClean == true) {
            TextInputEditText textName = (TextInputEditText) findViewById(R.id.recipeName);
            final String recipeName = textName.getText().toString();
            editor.putString("recipeName", recipeName);


            TextInputEditText textNumPeople = (TextInputEditText) findViewById(R.id.recipeNPeople);
            final String recipeNPeople = textNumPeople.getText().toString();
            editor.putString("recipeNumPeople", recipeNPeople);

            Spinner mySpinner = (Spinner) findViewById(R.id.categorySpinner);
            final String recipeCategory = mySpinner.getSelectedItem().toString();
            editor.putString("recipeCategory",recipeCategory);


            TextInputEditText textDescription = (TextInputEditText) findViewById(R.id.recipeDescription);
            final String recipeDescription = textDescription.getText().toString();
            editor.putString("recipeDescription",recipeDescription);


            String recipeKcal;
            String recipeProtein;
            String recipeCarbo;
            String recipeFat;

            if(switchChecked == true)
            {
                TextInputEditText textKcal = (TextInputEditText) findViewById(R.id.kcalText);
                recipeKcal = textKcal.getText().toString();
                editor.putString("recipeKcal",recipeKcal);

                TextInputEditText textProtein = (TextInputEditText) findViewById(R.id.proteinText);
                recipeProtein = textProtein.getText().toString();
                editor.putString("recipeProtein",recipeProtein);

                TextInputEditText textCarbo = (TextInputEditText) findViewById(R.id.carboText);
                recipeCarbo = textCarbo.getText().toString();
                editor.putString("recipeCarbo",recipeCarbo);

                TextInputEditText textFat = (TextInputEditText) findViewById(R.id.fatText);
                recipeFat = textFat.getText().toString();
                editor.putString("recipeFat",recipeFat);

                editor.putString("switchChecked","true");
            }else {
                editor.putString("switchChecked", "false");
            }
        }

        if(imageUri1 != null) {
            String image1 = imageUri1.toString();
            editor.putString("image1",image1);
        }else{
            String image1 = "";
            editor.putString("image1",image1);
        }

        if(imageUri2 != null){
            String image2 = imageUri2.toString();
            editor.putString("image2",image2);
        }else{
            String image2 = "";
            editor.putString("image2",image2);
        }

        if(imageUri3 != null){
            String image3 = imageUri3.toString();
            editor.putString("image3",image3);
        }else{
            String image3 = "";
            editor.putString("image3",image3);
        }





        editor.commit();


       // Log.d("State: : ","onPause");

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        String switchCheckedOnResume = settings.getString("switchChecked", "");
        String recipeNameAdd = settings.getString("recipeName", "");

        TextInputEditText textName = (TextInputEditText) findViewById(R.id.recipeName);
        textName.setText(recipeNameAdd);


        String recipeNPeople = settings.getString("recipeNumPeople", "");
        TextInputEditText textNumPeople = (TextInputEditText) findViewById(R.id.recipeNPeople);
        textNumPeople.setText(recipeNPeople);


        String recipeCategory = settings.getString("recipeCategory", "");
        Spinner mySpinner = (Spinner) findViewById(R.id.categorySpinner);

        if (recipeCategory == "Antipasti") {
            mySpinner.setSelection(0);
        } else if (recipeCategory == "Primi") {
            mySpinner.setSelection(1);
        } else if (recipeCategory == "Secondi") {
            mySpinner.setSelection(2);
        } else if (recipeCategory == "Contorni") {
            mySpinner.setSelection(3);
        } else if (recipeCategory == "Dolci") {
            mySpinner.setSelection(4);
        }


        String recipeDescription = settings.getString("recipeDescription", "");
        TextInputEditText textDescription = (TextInputEditText) findViewById(R.id.recipeDescription);
        textDescription.setText(recipeDescription);


        if (switchCheckedOnResume == "true") {

            switchChecked = true;

            Switch switchKcal = findViewById(R.id.switchKcal);
            switchKcal.setChecked(true);

            String recipeKcal = settings.getString("recipeKcal", "");
            TextInputEditText textKcal = (TextInputEditText) findViewById(R.id.kcalText);
            textKcal.setText(recipeKcal);

            String recipeProtein = settings.getString("recipeProtein", "");
            TextInputEditText textProtein = (TextInputEditText) findViewById(R.id.proteinText);
            textProtein.setText(recipeProtein);


            String recipeCarbo = settings.getString("recipeCarbo", "");
            TextInputEditText textCarbo = (TextInputEditText) findViewById(R.id.carboText);
            textCarbo.setText(recipeCarbo);

            String recipeFat = settings.getString("recipeFat", "");
            TextInputEditText textFat = (TextInputEditText) findViewById(R.id.fatText);
            textFat.setText(recipeFat);
        }

    }

}
