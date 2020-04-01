package CS201.Vinder.models;

import java.sql.Date;

import CS201.Vinder.models.Tag;

public class Post {
    public int ID;
    public Date Date;
    public String Author;
    public String Content;
    public Tag[] Tags;
}