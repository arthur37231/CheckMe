package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author tyson
 * Created 2021/10/25 at 3:06 PM
 */
@Entity(tableName = "HistoryItemList")
public class HistoryItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "c")
    private int ID;

    @ColumnInfo(name = "eventId")
    private String eventId;

    @ColumnInfo(name = "visitingTime")
    private String visitingTime;

    @ColumnInfo(name = "eventAddr")
    private String eventAddr;

    @ColumnInfo(name = "riskLevel")
    private String riskLevel;


    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public String getEventId() {
        return eventId;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public String getEventAddr() {
        return eventAddr;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public HistoryItem(String eventId, String visitingTime, String eventAddr, String riskLevel){
        this.eventId = eventId;
        this.visitingTime = visitingTime;
        this.eventAddr = eventAddr;
        this.riskLevel = riskLevel;
    }

}