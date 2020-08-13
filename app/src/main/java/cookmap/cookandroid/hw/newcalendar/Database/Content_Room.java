package cookmap.cookandroid.hw.newcalendar.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Content_Room {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String main_Img;
    private String img;
    private String label;

    public Content_Room(String title, String description, String main_Img, String img, String label) {
        this.title = title;
        this.description = description;
        this.main_Img = main_Img;
        this.img = img;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
