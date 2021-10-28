package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import comp5216.sydney.edu.au.checkme.utils.DateTimeUtils;

@Entity(tableName = "HighRiskAreaData")
public class HighRiskAreaData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id")
    private int dataId;

    @ColumnInfo(name = "event_id")
    private String eventId;

    @ColumnInfo(name = "event_address")
    private String eventAddress;

    @ColumnInfo(name = "high_risk_date")
    @TypeConverters(value = {DateTimeUtils.class})
    private Date highRiskDate;

    public HighRiskAreaData() {
    }

    public HighRiskAreaData(int dataId, String eventId, String eventAddress, Date highRiskDate) {
        this.dataId = dataId;
        this.eventId = eventId;
        this.eventAddress = eventAddress;
        this.highRiskDate = highRiskDate;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public Date getHighRiskDate() {
        return highRiskDate;
    }

    public void setHighRiskDate(Date highRiskDate) {
        this.highRiskDate = highRiskDate;
    }

    @Override
    public String toString() {
        return "HighRiskAreaData{" +
                "dataId=" + dataId +
                ", eventId='" + eventId + '\'' +
                ", eventAddress='" + eventAddress + '\'' +
                ", highRiskDate=" + highRiskDate +
                '}';
    }
}
