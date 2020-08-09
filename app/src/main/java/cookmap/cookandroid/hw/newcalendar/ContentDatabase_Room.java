package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Content_Room.class, version=1)
public abstract class ContentDatabase_Room extends RoomDatabase {

    private static final String DB_NAME = "Content.db";
    private static volatile ContentDatabase_Room instance;

    static synchronized ContentDatabase_Room getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static ContentDatabase_Room create(final Context context){
        return Room.databaseBuilder(
                context,
                ContentDatabase_Room.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract ContentDao_Room getContentDao();
}
