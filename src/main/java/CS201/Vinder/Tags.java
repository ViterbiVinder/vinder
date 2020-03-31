package CS201.Vinder;

import javax.annotation.*;
import javax.persistence.*;

@Entity
public class Tags {

    private String Name;

    public Tags() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

}