package cookmap.cookandroid.hw.newcalendar.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memo_Room {

    @PrimaryKey(autoGenerate = true)
    private int memo_idx;

    private int contents_id;
    private String date;

    public Memo_Room(int contents_id, String date) {
        this.contents_id = contents_id;
        this.date = date;
    }

    public void setMemo_idx(int memo_idx) {
        this.memo_idx = memo_idx;
    }

    public int getMemo_idx() {
        return memo_idx;
    }

    public int getContents_id() {
        return contents_id;
    }

    public String getDate() {
        return date;
    }
}
