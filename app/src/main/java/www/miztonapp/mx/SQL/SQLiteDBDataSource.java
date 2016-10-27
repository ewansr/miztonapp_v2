package www.miztonapp.mx.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Saulo on 18/07/2016.
 */
public class SQLiteDBDataSource extends SQLiteDBConfig{
    private SQLiteDbHelper openHelper;
    private SQLiteDatabase database;

    public SQLiteDBDataSource(Context context) {
        openHelper = new SQLiteDbHelper( context );
        database = openHelper.getWritableDatabase();
    }

    public static void insert(Context context, ContentValues values, String tableName ){
        SQLiteDbHelper dbh = new SQLiteDbHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.insert( tableName, null, values );
    }

    public static void deleteAll(Context context, String tableName){
        SQLiteDbHelper dbh = new SQLiteDbHelper( context );
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.execSQL( "delete from " + tableName );
    }

    public static long recordCount(Context context, String tableName) {
        SQLiteDbHelper dbh = new SQLiteDbHelper( context );
        SQLiteDatabase db = dbh.getWritableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries( db, tableName );
        return cnt;
    }

    @SuppressLint("LongLogTag")
    public static Cursor selectValue(Context context, String tableName){
        SQLiteDbHelper dbh = new SQLiteDbHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, tableName);
        Log.v("Total de registros leidos: ", String.valueOf(cnt).toString());
        Cursor cursor = db.rawQuery(" SELECT * FROM " + tableName, null);
        return cursor;
    }
}