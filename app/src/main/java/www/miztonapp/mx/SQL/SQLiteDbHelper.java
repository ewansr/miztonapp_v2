package www.miztonapp.mx.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saulo on 18/07/2016.
 */
public class SQLiteDbHelper  extends SQLiteOpenHelper {
    public SQLiteDbHelper ( Context context ){
        super( context, SQLiteDBConfig.DATABASE_NAME, null, SQLiteDBConfig.DATABASE_VERSION );
    }


    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( SQLiteDBDataSource.CREATE_USER );
        db.execSQL(SQLiteDBDataSource.user_seeder);
    }


    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( SQLiteDBDataSource.CREATE_USER);
    }


}