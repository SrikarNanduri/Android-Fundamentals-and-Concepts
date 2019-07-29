package com.srikar.android.roomwordssample.model;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;



@Entity(tableName = "word_table")       // Each @Entity class represents an entity in a table. Annotate your class declaration to indicate that the class is an entity. Specify the name of the table if you want it to be different from the name of the class.
public class Word {

    @PrimaryKey(autoGenerate = true)     // Every entity needs a primary key. To keep things simple, each word in the RoomWordsSample app acts as its own primary key.
    private int id;

    @NonNull        // Denotes that a parameter, field, or method return value can never be null. The primary key should always use this annotation. Use this annotation for any mandatory fields in your rows.
    @ColumnInfo(name = "word")      // Specify the name of a column in the table, if you want the column name to be different from the name of the member variable.
    private String mWord;

    @Ignore
    public Word(int id, @NonNull String word) {
        this.id = id;
        this.mWord = word;
    }

    public Word(@NonNull String word){
        this.mWord = word;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getWord(){return this.mWord;}
}
