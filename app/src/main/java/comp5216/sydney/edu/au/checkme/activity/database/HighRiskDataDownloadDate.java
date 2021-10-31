package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import comp5216.sydney.edu.au.checkme.utils.DateTimeUtils;

@Entity(tableName = "HighRiskDataDownloadDate")
public class HighRiskDataDownloadDate {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "download_id")
    private int downloadId;

    @ColumnInfo(name = "download_date")
    @TypeConverters(value = {DateTimeUtils.class})
    private Date downloadDate;

    public HighRiskDataDownloadDate() {
    }

    public HighRiskDataDownloadDate(int downloadId, Date downloadDate) {
        this.downloadId = downloadId;
        this.downloadDate = downloadDate;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }
}
