package com.example.rranz.ricettario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.view.View;


public class GestioneDB {

    static final String KEY_RIGAID = "id";
    static final String CATEGORIA = "categoria";
    static final String NOME_RIC= "nome_ric";
    static final String INGREDIENTI = "ingredienti";
    static final String DESCRIZIONE = "descrizione";
    static final String NUM_PERSONE = "num_persone";
    static final String IMAGE_FIRST = "image1";
    static final String IMAGE_SECOND = "image2";
    static final String IMAGE_THIRD = "image3" ;
    static final String KCAL = "kcal";
    static final String PROTEIN = "Protein";
    static final String CARBO = "Carbo";
    static final String FAT = "Fat";

    static final String IMAGE_ID = "id";
    static final String IMAGE = "image";





    static final String NOME_TAG = "nome_tag";
    static final String ID_RICETTA = "id_ricetta";

    static final String TAG = "GestioneDB";
    static final String DATABASE_NOME = "RicetteDB";
    static final String DATABASE_TABELLA = "ricetta";
    static final String DATABASE_TABELLA_TAGS = "tag";
    static final String DATABASE_FOTO = "immagini_ricetta";

    static final int DATABASE_VERSIONE = 1;

    static final String DATABASE_CREAZIONE =
            "CREATE TABLE " + DATABASE_TABELLA + " (" +
                    KEY_RIGAID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORIA + " VARCHAR(50),"+
                    NOME_RIC + " VARCHAR(50),"+
                    INGREDIENTI + " VARCHAR(500),"+
                    DESCRIZIONE + " VARCHAR(500),"+
                    NUM_PERSONE + " VARCHAR(50),"+
                    IMAGE_FIRST + " VARCHAR(50),"+
                    IMAGE_SECOND + " VARCHAR(50),"+
                    IMAGE_THIRD + " VARCHAR(50),"+
                    KCAL + " VARCHAR(50),"+
                    PROTEIN + " VARCHAR(50),"+
                    CARBO + " VARCHAR(50),"+
                    FAT + " VARCHAR(50)"+
                    ");";

    static final String DATABASE_CREAZIONE_TAGS =
            "CREATE TABLE " + DATABASE_TABELLA_TAGS + " (" +
                    KEY_RIGAID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOME_TAG + " VARCHAR(50), " +
                    ID_RICETTA + " INTEGER"+
                    ");";

    static final String DATABASE_CREAZIONE_FOTO =
            "CREATE TABLE " + DATABASE_FOTO + " (" +
                    IMAGE_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IMAGE + " VARCHAR(50), " +
                    ID_RICETTA + " INTEGER"+
                    ");";




    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public GestioneDB(Context ctx)
    {
        this.context = (Context) ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NOME, null, DATABASE_VERSIONE);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {

            try {
                db.execSQL(DATABASE_CREAZIONE);
                db.execSQL(DATABASE_CREAZIONE_TAGS);
                db.execSQL(DATABASE_CREAZIONE_FOTO);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            Log.w(DatabaseHelper.class.getName(),"Aggiornamento database dalla versione " + oldVersion + " alla "
                    + newVersion + ". I dati esistenti verranno eliminati.");

            db.execSQL("DROP TABLE IF EXISTS ricetta");
            db.execSQL("DROP TABLE IF EXISTS tag");
            db.execSQL("DROP TABLE IF EXISTS immagini_ricetta");
            onCreate(db);
        }

    }


    public GestioneDB open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        //DBHelper.onUpgrade(db,1,2);
        return this;
    }


    public void close()
    {
        DBHelper.close();
    }




    public long inserisciRicetta(String nome, String categoria, String ingredienti,String descrizione, String n_persone, String kcal,String carbo , String protein, String fat, String image1, String image2, String image3)
    {
        ContentValues initialValues = new ContentValues();

        String image1String;
        String image2String;
        String image3String;



        initialValues.put(NOME_RIC, nome);
        initialValues.put(CATEGORIA, categoria);
        initialValues.put(INGREDIENTI, ingredienti);
        initialValues.put(DESCRIZIONE, descrizione);
        initialValues.put(NUM_PERSONE, n_persone);
        initialValues.put(KCAL, kcal);
        initialValues.put(PROTEIN, protein);
        initialValues.put(CARBO, carbo);
        initialValues.put(FAT, fat);
        initialValues.put(IMAGE_FIRST,image1);
        initialValues.put(IMAGE_SECOND,"");
        initialValues.put(IMAGE_THIRD,"");

        return db.insert(DATABASE_TABELLA, null, initialValues);
    }


    public Cursor ottieniUltimaRicetta() throws SQLException
    {
        String query = "SELECT id FROM ricetta ORDER BY id DESC LIMIT 1";
        Log.d(TAG, query);
        return db.rawQuery(query,null);
    }

    public long inserisciFoto(String foto,int id_ric)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IMAGE,foto);
        initialValues.put(ID_RICETTA,id_ric);

        return db.insert(DATABASE_FOTO,null,initialValues);
    }


    public Cursor ottieniFoto(int id_ric) throws SQLException
    {
        //per stringe, attenzione all'apice singolo
        String query = "SELECT * FROM immagini_ricetta WHERE id_ricetta " +  " = '" + id_ric + "'";
        Log.d(TAG, query);
        return db.rawQuery(query,null);
        // return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE}, CATEGORIA + "=" + category, null, null, null, null);
    }


    public long inserisciTags(String tags,int id_ric)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOME_TAG,tags);
        initialValues.put(ID_RICETTA,id_ric);

        return db.insert(DATABASE_TABELLA_TAGS,null,initialValues);
    }


    public Cursor ottieniTags(int id_ric) throws SQLException
    {
        //per stringe, attenzione all'apice singolo
        String query = "SELECT * FROM tag WHERE id_ricetta " +  " = '" + id_ric + "'";
        Log.d(TAG, query);
        return db.rawQuery(query,null);
        // return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE}, CATEGORIA + "=" + category, null, null, null, null);
    }

    public Cursor ottieniRicettaByTag(String tag) throws SQLException
    {
        //per stringe, attenzione all'apice singolo
       // String query = "SELECT * FROM tag WHERE nome_tag " +  " = '" + tag + "'";
        //String query = "SELECT * FROM tag WHERE nome_tag  LIKE ?", new String[] { "%" + tag + "%" };
        //Log.d(TAG, query);
        return db.rawQuery("select * from " + DATABASE_TABELLA_TAGS + " where " + NOME_TAG + " like ? GROUP BY id_ricetta", new String[] { "%" + tag + "%" });
        // return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE}, CATEGORIA + "=" + category, null, null, null, null);
    }

    public boolean cancellaRicetta(long rigaId)
    {
        return db.delete(DATABASE_TABELLA, KEY_RIGAID + "=" + rigaId, null) > 0;
    }


    public Cursor ottieniTutteRicette() throws SQLException
    {
        //return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE}, null, null, null, null, null);
        //per stringe, attenzione all'apice singolo
        String query = "SELECT * FROM ricetta";
        Log.d(TAG, query);
        return db.rawQuery(query,null);
    }

    public Cursor ottieniTutteRicetteByCategory(String category) throws SQLException
    {
        //per stringe, attenzione all'apice singolo
        String query = "SELECT * FROM ricetta WHERE CATEGORIA " +  " = '" + category + "'";
        Log.d(TAG, query);
        return db.rawQuery(query,null);
       // return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE}, CATEGORIA + "=" + category, null, null, null, null);
    }

    public Cursor ottieniRicetta(long rigaId) throws SQLException
    {
        String query ="SELECT * FROM ricetta WHERE id" + " = '" + rigaId +"'";
      //  Cursor mCursore = db.query(true, DATABASE_TABELLA, new String[] {KEY_RIGAID, NOME_RIC, CATEGORIA, INGREDIENTI,  NUM_PERSONE, IMAGE_FIRST, IMAGE_SECOND, IMAGE_THIRD}, KEY_RIGAID + "=" + rigaId, null, null, null, null, null);
        Cursor mCursore = db.rawQuery(query,null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }


    public boolean aggiornaRicetta(long rigaId, String nome, String categoria, String ingredienti,String n_persone, String image1, String image2, String image3)
    {
        ContentValues args = new ContentValues();
        args.put(NOME_RIC, nome);
        args.put(CATEGORIA, categoria);
        args.put(INGREDIENTI, ingredienti);
        //   args.put(DESCRIZIONE, descrizione);
        args.put(NUM_PERSONE, n_persone);
        args.put(IMAGE_FIRST, image1);
        args.put(IMAGE_SECOND, image2);
        args.put(IMAGE_THIRD, image3);
        return db.update(DATABASE_TABELLA, args, KEY_RIGAID + "=" + rigaId, null) > 0;
    }

}

