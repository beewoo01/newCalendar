package cookmap.cookandroid.hw.newcalendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContentDao_Room {

    @Query("SELECT * FROM Content_Room")
    List<Content_Room> getAllContents();

    @Query("SELECT * FROM Content_Room WHERE start_date = :startdate" +" OR end_date = :enddate ")
    List<Content_Room> getMemo(String startdate, String enddate);

    @Insert
    void insert(Content_Room content_room);

    @Update
    void update(Content_Room content_room);

    @Delete
    void delete(Content_Room content_room);

}
