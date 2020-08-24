package cookmap.cookandroid.hw.newcalendar.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Room_DAO {

    @Query("SELECT * FROM Content_Room")
    List<Content_Room> getAllContents();

    @Query("SELECT C.id, C.title, C.description, C.main_Img, C.img, C.label FROM Content_Room as C " +
            "INNER JOIN Memo_Room as M  ON C.id = M.contents_id " +
            "WHERE M.date = :date ORDER BY C.id ASC")
    List<Content_Room> getClickMemo(String date);

    @Query("SELECT MIN(Memo_Room.date) as start_day, MAX(Memo_Room.date) as end_day from  Memo_Room where Memo_Room.contents_id = :id")
    Memo_Date getXN(int id);

    @Query("SELECT * FROM Content_Room WHERE Content_Room.id = :id")
    Content_Room getOneItem(int id);

    @Query("SELECT COUNT(*) FROM Memo_Room WHERE Memo_Room.date= :date")
    int getMemoCount(String date);

    @Insert
    long content_insert(Content_Room content_room);

    @Insert
    void memo_Insert(Memo_Room memo_room);

    @Query("UPDATE Content_Room SET title = :title, description = :dcrt," +
            " main_Img = :main_Img, img = :img, label = :label WHERE id =:id")
    void content_update(String title, String dcrt, String main_Img, String img, String label,  int id);

    @Query("DELETE FROM Content_Room WHERE id = :id")
    void content_delete(int id);

    @Query("DELETE FROM Memo_Room WHERE contents_id = :id")
    int memo_delete(int id);

}
