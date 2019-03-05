package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "edu.wgu.csmi508.progressscheduler.start_date";

    private static final String ARG_DATE = "date";
    // consider aligning the following with the request codes from CourseEditFragment
    private static int START_DATE = 0;
    private static int END_DATE = 1;

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {

        DatePickerFragment fragment = new DatePickerFragment();
        if (date != null) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_DATE, date);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        Bundle arguments = getArguments();
        mDatePicker = view.findViewById(R.id.dialog_date_picker);
        if (arguments != null && arguments.containsKey(ARG_DATE)) {
            Date date = (Date) getArguments().getSerializable(ARG_DATE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            mDatePicker.init(year, month, day, null);
        }



        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getTitle())
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private int getTitle() {
        return R.string.oops;
    }
}
