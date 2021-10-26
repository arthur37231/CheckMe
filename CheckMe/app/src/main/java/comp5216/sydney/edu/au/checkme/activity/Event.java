package comp5216.sydney.edu.au.checkme.activity;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/*
Represents a task with title and time
 */
public class Event implements Comparable<Event>{
    String eventId;
    String eventName;
    LatLng latLng;
    Date startTime;
    Date endTime;
    String qrCode;
    String coverImage;


    public Event(String eventName, LatLng latLng, Date startTime, Date endTime) {
        this.eventName = eventName;
        this.latLng = latLng;
        this.startTime = startTime;
        this.endTime = endTime;
        //this.coverImage = coverImage;
    }
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
    public String getState(){return timeDiffCalculator(this.endTime, this.startTime);}

    /*
    override the compareTo method so when sort an arraylist<Task>,
    the sorting will based on the date
     */
    @Override
    public int compareTo(Event code) {
        return 0;
    }
    public String timeDiffCalculator(Date d1, Date d2)
    {
        Instant dateOneInstant = d1.toInstant();
        ZonedDateTime zoneTimeOne = dateOneInstant.atZone(ZoneId.systemDefault());

        Instant dateTwoInstant = d2.toInstant();
        ZonedDateTime zoneTimeTwo = dateTwoInstant.atZone(ZoneId.systemDefault());


        long duration = 0;
        duration= Duration.between(zoneTimeTwo, zoneTimeOne).toMinutes();
        /*
        The below if statement responsible for the case that
        the user input a date which is before the current date
         */
        if (!d1.after(d2))
        {
            duration = 0-duration;
            return  "expired";
        }
        return "active";



    }
}
