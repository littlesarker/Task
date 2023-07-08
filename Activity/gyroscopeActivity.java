package gub.app.task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class gyroscopeActivity extends AppCompatActivity {


    MyDbHelper4 mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;
    String[] columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        ListView Listgyr = (ListView) findViewById(R.id.listID4);
        mHelper = new MyDbHelper4(this);
        mDb = mHelper.getWritableDatabase();

        String str="1";



        if(str.contains("1")){
            columns = new String[]{"_id", gyroscopeActivity.MyDbHelper4.COL_NAME, gyroscopeActivity.MyDbHelper4.COL_DATE};
            mCursor = mDb.query(gyroscopeActivity.MyDbHelper4.TABLE_NAME, columns, null, null, null, null, null, null);

            String[] headers = new String[]{gyroscopeActivity.MyDbHelper4.COL_NAME, gyroscopeActivity.MyDbHelper4.COL_DATE};
            mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                    mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});
            Listgyr.setAdapter(mAdapter);
            Listgyr.deferNotifyDataSetChanged();
        }


    }
    public static class MyDbHelper4 extends SQLiteOpenHelper {

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "Sdatagy";
        private static final String TABLE_NAME = "Data";
        private static final String KEY_ID = "id";
        public static final String COL_NAME = "DataProx";
        public static final String COL_DATE = "Date";


        public MyDbHelper4(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public MyDbHelper4(Context context) {
            super(context,DB_NAME,null,DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT, " + COL_DATE + " DATE);";
            db.execSQL(STRING_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        void insertLightData(String data) {
            //Get the Data Repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();
            //Create a new map of values, where column names are the keys
            ContentValues cValues = new ContentValues();
            cValues.put(COL_NAME, data);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
            cValues.put(COL_DATE, dateFormat.format(new Date()));

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_NAME, null, cValues);

        }

    }
}