package ensharp.ttalawa.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SpotsDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_SPOT_MAP_X="_spotMapX";
    public static final String KEY_SPOT_MAP_Y="_spotMapY";
    public static final String KEY_SPOT_TITLE="_spotTitle";
    public static final String KEY_SPOT_ADDRESS ="_spotAddress";

    private static final String TAG = "SpotsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_CREATE = "create table tourspot (_id integer primary key autoincrement, "
            + "_spotMapX text, _spotMapY text, _spotTitle text, _spotAddress text);";

    private static final String DATABASE_NAME = "tourSpotDb";
    private static final String DATABASE_TABLE = "tourspot";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS tourspot");
            onCreate(db);
        }

        //최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~투어 스팟 db 만들고 있습니당~~~~~~~~~~~~~~~~~~~~~~~");
            db.execSQL(DATABASE_CREATE);
        }

    }

    public SpotsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public SpotsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }



    //레코드 생성
    public long createSpot(String spotMapX, String spotMapY, String spotTitle, String spotAddress) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_SPOT_MAP_X, spotMapX);
        initialValues.put(KEY_SPOT_MAP_Y, spotMapY);
        initialValues.put(KEY_SPOT_TITLE, spotTitle);
        initialValues.put(KEY_SPOT_ADDRESS, spotAddress);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    //모든 레코드 반환
    public Cursor fetchAllSpots() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_SPOT_MAP_X, KEY_SPOT_MAP_Y,
                KEY_SPOT_TITLE, KEY_SPOT_ADDRESS}, null, null, null, null, null);
    }

    //특정 레코드 반환-NO USE
//    public Cursor fetchSpot(long rowId) throws SQLException {
//
//        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SPOT_MAP_X,
//                KEY_SPOT_MAP_Y, KEY_SPOT_TITLE, KEY_SPOT_ADDRESS}, KEY_ROWID
//                + "=" + rowId, null, null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }

    public Cursor fetchSpotByTitle(String spotTitle) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SPOT_MAP_X,
                KEY_SPOT_MAP_Y, KEY_SPOT_TITLE, KEY_SPOT_ADDRESS}, KEY_SPOT_TITLE
                + "= '" + spotTitle+"' ", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}