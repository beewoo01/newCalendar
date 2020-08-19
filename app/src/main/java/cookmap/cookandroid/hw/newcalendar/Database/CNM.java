package cookmap.cookandroid.hw.newcalendar.Database;

import androidx.room.Embedded;

public class CNM {
    @Embedded
    Content_Room content_room;

    @Embedded
    Memo_Room memo_room;

    public CNM(Content_Room content_room, Memo_Room memo_room) {
        this.content_room = content_room;
        this.memo_room = memo_room;
    }


    public Content_Room getContent_room() {
        return content_room;
    }

    public void setContent_room(Content_Room content_room) {
        this.content_room = content_room;
    }

    public Memo_Room getMemo_room() {
        return memo_room;
    }

    public void setMemo_room(Memo_Room memo_room) {
        this.memo_room = memo_room;
    }
}
