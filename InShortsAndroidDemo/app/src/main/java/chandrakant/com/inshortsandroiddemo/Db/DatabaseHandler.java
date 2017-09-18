package chandrakant.com.inshortsandroiddemo.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import chandrakant.com.inshortsandroiddemo.model.ResponseAPI;

/**
 * Created by chandrakant on 16/9/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "News";

    // Contacts table name
    private static final String TABLE_CONTACTS = "NewsTable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NEWSID = "newsid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_PUBLISHER = "publisher";
    private static final String Key_TIME = "time";
    private static final String KEY_FAV = "like";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NEWSID + " TEXT," + KEY_TITLE + " TEXT," + KEY_URL + " TEXT,"
                + KEY_CATEGORY + " TEXT," + KEY_HOSTNAME + " TEXT," + KEY_PUBLISHER + " TEXT," + Key_TIME + " TEXT,"
                + KEY_FAV + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new Response
    public void addResponse(ResponseAPI responseAPI) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NEWSID, responseAPI.getID());
        values.put(KEY_TITLE, responseAPI.getTITLE());
        values.put(KEY_URL, responseAPI.getURL());
        values.put(KEY_CATEGORY, responseAPI.getCATEGORY());
        values.put(KEY_HOSTNAME, responseAPI.getHOSTNAME());
        values.put(KEY_PUBLISHER, responseAPI.getPUBLISHER());
        values.put(Key_TIME, responseAPI.getTIMESTAMP());
        values.put(KEY_FAV, responseAPI.isLike());


        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<ResponseAPI> getAllResponse() {
        List<ResponseAPI> contactList = new ArrayList<ResponseAPI>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ResponseAPI contact = new ResponseAPI();
                contact.setId(Integer.parseInt(cursor.getString(0)));

                contact.setID(Integer.parseInt(cursor.getString(1)));
                contact.setTITLE(cursor.getString(2));
                contact.setURL(cursor.getString(3));
                contact.setCATEGORY(cursor.getString(4));
                contact.setHOSTNAME(cursor.getString(5));
                contact.setPUBLISHER(cursor.getString(6));
                contact.setTIMESTAMP(cursor.getString(7));
                contact.setLike(cursor.getString(8));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(ResponseAPI contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FAV, contact.isLike());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
    }

    // Deleting single contact
    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    // Getting single contact
    public String getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NEWSID, KEY_TITLE, KEY_URL, KEY_CATEGORY, KEY_HOSTNAME, KEY_PUBLISHER, Key_TIME, KEY_FAV}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor != null)
            cursor.moveToFirst();
        // return contact
        return cursor.getString(8);
    }

}