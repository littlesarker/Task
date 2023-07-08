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

public class accelerometerActivity extends AppCompatActivity {


    MyDbHelper3 mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;
    String[] columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        ListView List3 = (ListView) findViewById(R.id.listID2);
        mHelper = new MyDbHelper3(accelerometerActivity.this);
        mDb = mHelper.getWritableDatabase();


        String str="1";



        if(str.contains("1")){
            columns = new String[]{"_id", accelerometerActivity.MyDbHelper3.COL_NAME, accelerometerActivity.MyDbHelper3.COL_DATE};
            mCursor = mDb.query(accelerometerActivity.MyDbHelper3.TABLE_NAME, columns, null, null, null, null, null, null);

            String[] headers = new String[]{accelerometerActivity.MyDbHelper3.COL_NAME, accelerometerActivity.MyDbHelper3.COL_DATE};
            mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                    mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});
            List3.setAdapter(mAdapter);
            List3.deferNotifyDataSetChanged();
        }

    }
    public static class MyDbHelper3 extends SQLiteOpenHelper {

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "Sdata3";
        private static final String TABLE_NAME = "Data";
        private static final String KEY_ID = "id";
        public static final String COL_NAME = "DataProx";
        public static final String COL_DATE = "Date";


        public MyDbHelper3(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public MyDbHelper3(Context context) {
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

        void insertAcceloData(String data) {
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