package pt.cm.challenge_3.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "points")
public class Point {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "timestamp")
    public String timestamp;

    @ColumnInfo(name = "temperature")
    public float temperature;

    @ColumnInfo(name = "humidity")
    public float humidity;
    
}
