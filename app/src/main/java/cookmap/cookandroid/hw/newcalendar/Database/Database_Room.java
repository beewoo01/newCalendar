package cookmap.cookandroid.hw.newcalendar.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.db.Memo_Room;

@Database(entities = {Content_Room.class, Memo_Room.class}, version=1)
public abstract class Database_Room extends RoomDatabase {

    private static final String DB_NAME = "Content.db";
    private static volatile Database_Room instance;

    public static synchronized Database_Room getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static Database_Room create(final Context context){
        return Room.databaseBuilder(
                context,
                Database_Room.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract Room_DAO getDao();
}
