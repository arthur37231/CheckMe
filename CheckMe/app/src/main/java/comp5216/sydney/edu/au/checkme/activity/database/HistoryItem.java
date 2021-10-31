package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import java.util.Date;

import comp5216.sydney.edu.au.checkme.utils.DateTimeUtils;

/**
 * @author tyson
 * Created 2021/10/25 at 3:06 PM
 */
@Entity(tableName = "HistoryItemList")
public class HistoryItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int ID;

    @ColumnInfo(name = "eventId")
    private String eventId;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "visitingTime")
    private String visitingTime;

    @ColumnInfo(name = "eventAddr")
    private String eventAddr;

    @ColumnInfo(name = "riskLevel")
    private String riskLevel;

    @ColumnInfo(name = "visitingDate")
    @TypeConverters(value = {DateTimeUtils.class})
    private Date visitingDate;

    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() { return eventName; }

    public String getVisitingTime() {
        return visitingTime;
    }

    public String getEventAddr() { return eventAddr; }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setVisitingTime(String visitingTime) {
        this.visitingTime = visitingTime;
    }

    public void setEventAddr(String eventAddr) {
        this.eventAddr = eventAddr;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Date getVisitingDate() {
        return visitingDate;
    }

    public void setVisitingDate(Date visitingDate) {
        this.visitingDate = visitingDate;
    }

    public HistoryItem(String eventId, String eventName, String visitingTime, String eventAddr, String riskLevel, Date visitingDate){
        this.eventId = eventId;
        this.eventName = eventName;
        this.visitingTime = visitingTime;
        this.eventAddr = eventAddr;
        this.riskLevel = riskLevel;
        this.visitingDate = visitingDate;
    }

    public HistoryItem() {
    }

    @Override
    public String toString() {
        return "HistoryItem{" +
                "ID=" + ID +
                ", eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", visitingTime='" + visitingTime + '\'' +
                ", eventAddr='" + eventAddr + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                '}';
    }
}
