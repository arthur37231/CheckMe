package comp5216.sydney.edu.au.checkme.activity;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import comp5216.sydney.edu.au.checkme.activity.utils.Tools;

/*
Represents a task with title and time
 */
public class Event implements Comparable<Event>{
    final String type = "checkMe";
    String eventId;
    String eventName;
    LatLng latLng;
    Date startTime;
    Date endTime;
    String qrCode;
    String coverImage;
    boolean active = true;


    public Event(String eventName, LatLng latLng, Date startTime, Date endTime) {
        this.eventName = eventName;
        this.latLng = latLng;
        this.startTime = startTime;
        this.endTime = endTime;
        //this.coverImage = coverImage;
    }
    public boolean getActive(){return this.active;}
    public void setActive(boolean state){this.active=state;}
    public Date getEndTime(){return this.endTime;}
    public Date getStartTime(){return this.startTime;}
    public String getType() {return  this.type;}
    public LatLng getLatLng()
    {
        return this.latLng;
    }
    public String getQrCode()
    {
        return this.qrCode;
    }
    public void setQrCode(String qrCode)
    {
        this.qrCode = qrCode;
    }
    public String getCoverImage()
    {
        return this.coverImage;
    }
    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }
    /*
    return task's title
     */
    public String getEventName() {
        return this.eventName;
    }
    /*
    set task's title
     */
    public void setTitle(String eventName) {
        this.eventName = eventName;
    }
    /*
    return task's date
     */
    public String getEventId() {
        return this.eventId;
    }
    /*
    set task's date
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getState(){return Tools.expireChekcer(this.endTime, this.startTime);}
    public long getRemainingTime()
    {
        long remainingTime = Tools.timeDiffCalculator(this.endTime,this.startTime);
        return remainingTime;
    }

    /*
    override the compareTo method so when sort an arraylist<Task>,
    the sorting will based on the date
     */
    @Override
    public int compareTo(Event task) {
        long currentRemainTime = getRemainingTime();
        long taskRemainTime = task.getRemainingTime();
        return Long.compare(taskRemainTime,currentRemainTime);
    }

}
