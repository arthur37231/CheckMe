package comp5216.sydney.edu.au.checkme.activity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
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
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String address = addresses.get(0).getAddressLine(0).split(",")[0];
            //String others = addresses.get(0).getAddressLine(0).split(",")[1];

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String others = city+", "+state +", "+postalCode;
            addressBook.put("address", address);
            addressBook.put("others", others);
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            //createAddress.setText(address);
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



}
