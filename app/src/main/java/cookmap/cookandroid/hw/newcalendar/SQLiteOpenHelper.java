package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    public static final String table_Name = "contents";
    public static final String ID = "id";
    //SQLite (id, 제목, 내용, 그림 , 날짜) 순으로 생성 바람


    public SQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "CREATE TABLE " + table_Name + "(name text)";;
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
