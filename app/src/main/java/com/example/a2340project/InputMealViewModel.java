package com.example.a2340project;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

public class InputMealViewModel extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        List<DataEntry> list = new ArrayList<>();

        list.add(new ValueDataEntry("emmet", 299));
        list.add(new ValueDataEntry("john", 100));
        list.add(new ValueDataEntry("sin", 32));

    }
}