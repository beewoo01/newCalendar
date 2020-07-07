package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String TAG = "sqlite";
    public static final String table_Name = "contents";
    public static final String ID = "id";
    //SQLite (id, 제목, 내용, 그림 , 날짜) 순으로 생성 바람


    public SQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table contents" +
                "(_id integer primary key autoincrement," +
                "title text, description text, main_Img text, img text, s_date text, e_date text, label text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading from version " + oldVersion + " to " + newVersion);
        if (oldVersion < 2){
            try {
                db.beginTransaction();
                db.execSQL("ALTER TABLE " + table_Name + " ADD COLUMN "+ "label text");
                db.setTransactionSuccessful();
            }catch (IllegalStateException e){
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }finally {
                db.endTransaction();
            }
        }
        /*String sql = "drop table if exists contents;";
        db.execSQL(sql);
        onCreate(db)*/; // 다시 테이블 생성
    }
}