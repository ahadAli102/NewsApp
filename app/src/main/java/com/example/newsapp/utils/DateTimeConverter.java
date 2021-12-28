package com.example.newsapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class DateTimeConverter {

    public static long convert(String dateTime){
        //String dateTime = "2021-11-22 18:57:16";
        String[] date = dateTime.split(" ");
        date[0] = date[0].replace("-","");
        date[1] = date[1].replace(":","");
        String finalDate = date[0]+date[1];
        return Long.parseLong(finalDate);
    }

    public static String convert(String dateTime,int a) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
             date = dateFormatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        assert date != null;
        String displayValue = timeFormatter.format(date);
        StringBuilder finalDate = new StringBuilder();
        String[] dates = dateTime.split(" ")[0].split("-");
        int day = Integer.parseInt(dates[2]);
        if(day == 1){
            finalDate.append(day+ "'st of ");
        }
        else if(day == 2){
            finalDate.append(day+ "'nd of ");

        }else if(day == 3){
            finalDate.append(day+ "'rd of ");
        }
        else {
            finalDate.append(day+"th ");
        }

        int month = Integer.parseInt(dates[1]);

        if(month == 1){
            finalDate.append("January");
        }else if(month == 2){
            finalDate.append("February");
        }else if(month == 3){
            finalDate.append("March");
        }else if(month == 4){
            finalDate.append("April");
        }else if(month == 5){
            finalDate.append("May");
        }else if(month == 6){
            finalDate.append("June");
        }else if(month == 7){
            finalDate.append("July");
        }else if(month == 8){
            finalDate.append("August");
        }else if(month == 9){
            finalDate.append("September");
        }else if(month == 10){
            finalDate.append("October");
        }else if(month == 11){
            finalDate.append("November");
        }else if(month == 12){
            finalDate.append("December");
        }
        finalDate.append(" at "+dates[0]);

        return displayValue+finalDate.toString();
    }



}
