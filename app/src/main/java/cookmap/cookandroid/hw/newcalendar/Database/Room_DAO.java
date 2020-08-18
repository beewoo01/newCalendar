package cookmap.cookandroid.hw.newcalendar.Database;

import android.util.Pair;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Room_DAO {

    @Query("SELECT * FROM Content_Room")
    List<Content_Room> getAllContents();


    /*@Query("SELECT c.title, c.description, c.main_Img, c.img, c.label FROM Content_Room AS c JOIN"+
            " Memo_Room AS m ON m.contents_id = c.id")
    List<Content_Room> getClickMemo(String date);*/

    @Query("SELECT * FROM Content_Room " +
            "INNER JOIN Memo_Room  ON Content_Room.id = Memo_Room.contents_id " +
            "WHERE Memo_Room.date = :date")
    List<Pair<Content_Room, Memo_Room>> getClickMemo(String date);

    @Query("SELECT Memo_Room.date FROM Memo_Room JOIN Content_Room ON Memo_Room.contents_id = Content_Room.id")
    List<String> getMemo();

    @Query("SELECT COUNT(*) FROM Memo_Room WHERE Memo_Room.date= :date")
    int getMemoCount(String date);

    @Insert
    List<Long> content_insert(Content_Room... content_room);

    @Insert
    void memo_Insert(Memo_Room memo_room);

    @Update
    void update(Content_Room content_room);

    @Delete
    void delete(Content_Room content_room);

}
