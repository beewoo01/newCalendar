package cookmap.cookandroid.hw.newcalendar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Content_Room {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String main_Img;
    private String img;
    private String start_date;
    private String end_date;
    private String label;

    public Content_Room(String title, String description, String main_Img, String img, String start_date, String end_date, String label) {
        this.title = title;
        this.description = description;
        this.main_Img = main_Img;
        this.img = img;
        this.start_date = start_date;
        this.end_date = end_date;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain_Img() {
        return main_Img;
    }

    public void setMain_Img(String main_Img) {
        this.main_Img = main_Img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
