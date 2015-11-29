package knowyc.com.knowyc.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sumod on 29-Nov-15.
 */
public class DBHandler extends SQLiteOpenHelper{

    //DATABASE VERSION and  NAME :
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "KnowYC.db";
    private static final int zero = 0;

    //Incident Table:
    public static final String TABLE_CHATS = "Chats";

    public static final String KEY_ID = "ID";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String KEY_READ = "READ";
    public static final String KEY_OUTGOING = "OUTGOING";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHATS = "CREATE TABLE " + TABLE_CHATS  + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + KEY_MESSAGE + " VARCHAR(255) , "
                + KEY_OUTGOING + " INTEGER "
                + KEY_READ + " INTEGER, "
                + ")";
        db.execSQL(CREATE_TABLE_CHATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);

        onCreate(db);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
}
