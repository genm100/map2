package com.example.kma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kma.loginandregisteractivity.LoginActivity;
import com.example.kma.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calendar extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    public static TextView sktext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        sktext = (TextView) findViewById(R.id.sktext);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        Event ev1 = new Event(Color.GREEN, 1625936400000L, "sự kiện 1");
        Event ev2 = new Event(Color.GREEN, 1625850000000L, "Sự kiện 2");
        Event ev3 = new Event(Color.YELLOW, 1626454800000L, "Sự kiện 3");

        compactCalendar.addEvent(ev1);
        compactCalendar.addEvent(ev2);
        compactCalendar.addEvent(ev3);


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();


                if (dateClicked.toString().compareTo("Sun Jan 09 00:00:00 GMT+07:00 2022") == 0) {
                    String data = "18h-21h: Thể chất L02, Lập trình L04, Triết L05";
                    Calendar.sktext.setText(data);

                } else if (dateClicked.toString().compareTo("Sat Jan 08 00:00:00 GMT+07:00 2022") == 0) {
                    String data = "7h-9h: Thi kết thúc học phần Lập trình mạng!";
                    Calendar.sktext.setText(data);

                } else if (dateClicked.toString().compareTo("Mon Jan 10 00:00:00 GMT+07:00 2022") == 0) {
                    String data = "15h-21h: Thể chất L07, Lập trình L06, Triết L02";
                    Calendar.sktext.setText(data);

                } else {
                    String data = "Chưa có lịch!";
                    Calendar.sktext.setText(data);
                }


            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }

        });
    }
}
