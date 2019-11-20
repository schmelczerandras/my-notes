package hu.bme.mynotes.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "notes")
public class Note {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "tags")
    public List<String> tags;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "title")
    public String title;
}