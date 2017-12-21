package com.example.user.barcode;

        import android.content.ClipData;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class UserDb extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "barcode";

    // Contacts table name
    private static final String TABLE_USER = "USERS";

    // Contacts Table Columns names
    private static final String ID = "ID";
    private static final String NAME  = "NAME";
    private static final String SURNAME  = "SURNAME";
    private static final String EMAIL  = "EMAIL";
    private static final String PASSWORD  = "PASSWORD";

    public UserDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("error", "ilk");
    }

    public void CreateTable(){
        Log.e("error", "oncreare");
        SQLiteDatabase db = getWritableDatabase();

      //  String DELETE_VOCABULARY_TABLE ="DROP TABLE IF EXISTS "+TABLE_USER;
       // db.execSQL(DELETE_VOCABULARY_TABLE);

        String CREATE_USERS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_USER
                        + "("
                        + NAME + " TEXT,"
                        + SURNAME + " TEXT,"
                        + EMAIL + " TEXT,"
                        + PASSWORD +" TEXT"
                        + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }



    public boolean Register(String name,String surname, String email, String password){

        Log.e("error", "insert");
        SQLiteDatabase db=getWritableDatabase();
        //Check email is stored
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+EMAIL+" = '"+email+"'",null);
        Log.e("log",""+c.getCount());

        if(c.getCount()==1){
            return false;
        }

        String INSERT_VOCABULARY="INSERT INTO "+TABLE_USER+" VALUES('"+name+"','"+surname+"','"+email+"','"+password+"')";
        db.execSQL(INSERT_VOCABULARY);
        return true;
    }

    public boolean ChangePassword(String email, String password,String newPass){

        Log.e("db", "change");
        SQLiteDatabase db=getWritableDatabase();

        Log.e("log","read2");
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+EMAIL+" = '"+email+"' AND "+PASSWORD+"= '"+password+"'",null);
        Log.e("log",""+c.getCount());

        if(c.getCount()==0){
            return false;
        }

        Log.e("db","update");
        String UPDATE="UPDATE "+TABLE_USER+" SET "+PASSWORD+"='"+newPass+"' WHERE "+EMAIL+"='"+email+"'";
        db.execSQL(UPDATE);
        Log.e("db","update succ");
        return true;
    }



    public StringBuilder Logint(String email,String password) {
        Log.e("log","read");
        SQLiteDatabase db=getWritableDatabase();
        Log.e("log","read2");
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+EMAIL+" = '"+email+"' AND "+PASSWORD+"= '"+password+"'",null);
        Log.e("log",""+c.getCount());
        StringBuilder test=new StringBuilder();


        if (c != null ) {
            Log.e("error","if");
            if  (c.moveToFirst()) {
                Log.e("error","if 2");
                do {
                    Log.e("error","do");
                    String dir = c.getString(c.getColumnIndex("NAME"));
                    test.append("" + dir);
                }while (c.moveToNext());
            }
        }

        return test;
    }

    public boolean Login(String email,String password) {
        Log.e("log","read");
        SQLiteDatabase db=getWritableDatabase();
        Log.e("log","read2");
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+EMAIL+" = '"+email+"' AND "+PASSWORD+"= '"+password+"'",null);
        Log.e("log",""+c.getCount());

        if(c.getCount()==1){
            return true;
        }

        return false;
    }




    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }
}