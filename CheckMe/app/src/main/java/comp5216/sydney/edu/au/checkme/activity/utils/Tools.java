package comp5216.sydney.edu.au.checkme.activity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import comp5216.sydney.edu.au.checkme.activity.Event;

public class Tools {
    public static int id;

    public static void setId(int num)
    {
        id = num;
    }

    public static int getId()
    {
        return id+=1;
    }

    public static final double EARTH_RADIUS = 6378137;

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String taskToString (Event task)
    {
        Gson gson = new Gson();
        String serializeTask = gson.toJson(task);
        return  serializeTask;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = Float.valueOf(width) / Float.valueOf(height);

        width = maxSize;
        height = (int) (width / bitmapRatio);
        if (bitmapRatio <= 1) {
            height = maxSize;
            width = (int) (height * bitmapRatio);

        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    /*
    A static method transforms received String to Task object
     */
    public static Event stringToTask (String serializeTask)
    {
        Event convetedTask = new Gson().fromJson(serializeTask, Event.class);
        return convetedTask;
    }

    public static HashMap<String, String> CoordinateToAddress(LatLng latLng, Context context)
    {
        List<Address> addresses;
        HashMap<String,String> addressBook = new HashMap<String,String>();
        Geocoder geocoder;
        geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0).split(",")[0];

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            String others = city+", "+state +", "+postalCode;
            addressBook.put("address", address);
            addressBook.put("others", others);

            return addressBook;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String timeToString(Date date)
    {
        String pattern = "MM/dd/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(date);
        return todayAsString;

    }


    public static String expireChekcer(Date end, Date start)
    {
        Instant dateOneInstant = end.toInstant();
        ZonedDateTime zoneTimeOne = dateOneInstant.atZone(ZoneId.systemDefault());

        Instant dateTwoInstant = start.toInstant();
        ZonedDateTime zoneTimeTwo = dateTwoInstant.atZone(ZoneId.systemDefault());


        long duration = 0;
        duration= Duration.between(zoneTimeTwo, zoneTimeOne).toMinutes();
        /*
        The below if statement responsible for the case that
        the user input a date which is before the current date
         */
        if (start.after(end))
        {
            duration = 0-duration;
            String remaintime = "Expired";
            return  remaintime;
        }

        String remainTime = duration/(24*60)+"days "+duration/60%24+"hours "+duration%60+"minutes";
        return remainTime;

    }

    public static double rad(double d){
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        return s; // in meters
    }


}
