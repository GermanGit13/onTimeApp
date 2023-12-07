package com.svalero.ontimeapp.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePicker {

    public void timePickerTextView(TextView textView, TextView textViewDos,Context context) {

        textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
            final Calendar getTime = Calendar.getInstance();
                TimePickerDialog timePicker = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(android.widget.TimePicker view, int sHour, int sMinute) {
                                getTime.set(Calendar.HOUR_OF_DAY, sHour);
                                getTime.set(Calendar.MINUTE, sMinute);
                                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                                String formatedDate = timeformat.format(getTime.getTime());

                                textViewDos.setText(formatedDate);
                            }
                        }, getTime.get(Calendar.HOUR_OF_DAY), getTime.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });
    }
}
