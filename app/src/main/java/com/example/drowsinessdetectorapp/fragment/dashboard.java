package com.example.drowsinessdetectorapp.fragment;



import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drowsinessdetectorapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class dashboard extends Fragment {
    private static final String TAG = "dashboard";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"View is to be Created");
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        String fpath ="";
        try {
            File path = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            fpath = path + "/" + "drowsy_data" + ".txt";
        }catch (Exception e){

        }
        HashMap<String,Integer> map = new HashMap<>();

        //Toast.makeText(getContext(),fpath,Toast.LENGTH_LONG).show();
        ArrayList<String> content = new ArrayList<>();
        //read file content
        try {
            String filecontent = readFile(fpath);
            String[] arr = filecontent.split("/");
            for(String i:arr){
                content.add(i);
                //Toast.makeText(getContext(),i,Toast.LENGTH_LONG).show();


            }
        // separate date
        try {
            for ( int i=0;i<arr.length-1;i++) {

                   String[] day = arr[i].split("-");
                   //Toast.makeText(getContext(),day[0],Toast.LENGTH_SHORT).show();
                if(map.containsKey(day[0])) {

                    map.put(day[0], map.get(day[0]) + 1);
                    //Toast.makeText(getContext(), ""+day[0], Toast.LENGTH_SHORT).show();
                }
                else{
                    map.put(day[0],1);
                    //Toast.makeText(getContext(), ""+day[0], Toast.LENGTH_SHORT).show();
                }


            }

            //Toast.makeText(getContext(), " "+map.size(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //LineGraph
        try {
            TreeMap<String, Integer> sorted = new TreeMap<>(map);
            LineChart lineChart;
            LineData lineData;
            LineDataSet lineDataSet;
            lineChart = view.findViewById(R.id.lineChart);
            ArrayList lineEntries;
            lineEntries = new ArrayList<>();
            int key=0,val=0;
            for(Map.Entry<String, Integer> pair : sorted.entrySet()){
                key = Integer.parseInt(pair.getKey());
                val = pair.getValue();
                lineEntries.add(new Entry(key,val));
                //Toast.makeText(getContext(), ""+key+" "+val, Toast.LENGTH_SHORT).show();


            }


            lineDataSet = new LineDataSet(lineEntries, "count/day");
            lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            //lineChart.invalidate();
            lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            lineDataSet.setValueTextColor(Color.BLACK);
            lineDataSet.setValueTextSize(10f);
            XAxis xAxis = lineChart.getXAxis();
            //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //xAxis.setLabelCount(1);
            XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
            xAxis.setPosition(position);
            //xAxis.enableGridDashedLine(2f, 7f, 0f);
            xAxis.setAxisMaximum(31);
            xAxis.setAxisMinimum(1);
            xAxis.setLabelCount(16, true);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(7f);
            lineChart.setVisibleXRange(1,16);
            //xAxis.setLabelRotationAngle(315f);

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setEnabled(false);
            YAxis yAxisleft =lineChart.getAxisLeft();
            //yAxisleft.setEnabled(false);
            yAxisleft.setAxisMinimum(0);
            yAxisleft.setAxisMaximum(12);
           // lineChart.setVisibleYRangeMaximum(20, YAxis.AxisDependency.LEFT);

            sorted.clear();
            lineChart.setScaleEnabled(false);

            //lineEntries.clear();
            //lineDataSet.clear();





            }
        catch (Exception exception) {
            exception.printStackTrace();
        }


//        ratings
        RatingBar ratings;
        ratings = (RatingBar) view.findViewById(R.id.ratings);
        try {
            float avg_per_day_count = content.size() / map.size();
            float rating;

            if(avg_per_day_count<=3 && avg_per_day_count>=0) {
                rating = (float) (5.0f - (avg_per_day_count * 0.2));
                ratings.setRating(rating);
            }
            else if(avg_per_day_count>3 && avg_per_day_count<=9 ){
                rating = (float) (5.0f - (avg_per_day_count * 0.35));
                ratings.setRating(rating);
            }
            else{
                ratings.setRating(1.0f);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // suggestion
        try{
            map.clear();
            EditText suggestion;
            suggestion = (EditText) view.findViewById(R.id.suggestion);
            for ( int i=0;i<content.size()-1;i++) {

                String[] day = content.get(i).split("-");
                String[] hour = content.get(i).split(":");
                //Toast.makeText(getContext(),day[0]+" "+hour[1],Toast.LENGTH_SHORT).show();
                if (map.containsKey(day[0] + hour[1])) {

                    map.put(day[0]+hour[1], map.get(day[0] + hour[1]) + 1);
                    //Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                } else {
                    map.put(day[0] + hour[1], 1);
                    //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                }
            }
            Map.Entry<String, Integer> max = getMaxEntryInMapBasedOnValue(map);
            //Toast.makeText(getContext(), ""+max.getKey()+" "+max.getValue(), Toast.LENGTH_SHORT).show();
            String max_val = max.getKey().trim();

            String max_time = ""+max_val.charAt(2)+max_val.charAt(3);
            int max_time_val = Integer.parseInt(max_time);

            //Toast.makeText(getContext(), ""+max_time_val, Toast.LENGTH_SHORT).show();
            if (max_time_val<3){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 12 AM TO 3AM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");
            }
            else if(max_time_val>=3 && max_time_val<6){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 3 AM TO 6 AM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");
            }
            else if(max_time_val>=6 && max_time_val<9){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 6 AM TO 9 AM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");
            }
            else if(max_time_val>=9 && max_time_val<12){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 9 AM TO 12PM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");
            }
            else if(max_time_val>=12 && max_time_val<15){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 12 PM TO 3 PM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");

            }
            else if(max_time_val>=15 && max_time_val<18){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 3 PM TO 6 PM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");

            }
            else if(max_time_val>=18 && max_time_val<21){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1) +"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 6 PM TO 9 PM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");

            }
            else if(max_time_val>=21 && max_time_val<24){
                suggestion.setText("ALERT!!!.YOUR TOTAL DROWSINESS COUNT FOR THIS MONTH IS "+(content.size()-1)+"" +
                        ". YOU ARE FOUND MORE FREQUENTY DROWSY BETWEEN 9 PM TO 12 AM AND THE TOTAL COUNT OVER THAT PERIOD IS "+ max.getValue()+". SO STAY ALERT DURING THIS PERIOD.");

            }








        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        }
        finally {
            content.clear();
            map.clear();

        }


        return view;
    }

    public static <K, V extends Comparable<V> >
    Map.Entry<K, V>
    getMaxEntryInMapBasedOnValue(Map<K, V> map)
    {

        // To store the result
        Map.Entry<K, V> entryWithMaxValue = null;

        // Iterate in the map to find the required entry
        for (Map.Entry<K, V> currentEntry :
                map.entrySet()) {

            if (
                // If this is the first entry, set result as
                // this
                    entryWithMaxValue == null

                            // If this entry's value is more than the
                            // max value Set this entry as the max
                            || currentEntry.getValue().compareTo(
                            entryWithMaxValue.getValue())
                            > 0) {

                entryWithMaxValue = currentEntry;
            }
        }

        // Return the entry with highest value
        return entryWithMaxValue;
    }
    private String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }
    }

