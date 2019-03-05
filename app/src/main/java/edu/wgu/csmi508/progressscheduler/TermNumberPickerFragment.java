package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TermNumberPickerFragment extends DialogFragment {

    public static final String EXTRA_TERM_NUMBER =
            "edu.wgu.csmi508.progressscheduler.term_number";

    private static final String ARG_TERM_NUMBER = "term_number";

    private NumberPicker mTermNumberPicker;
    private int[] mTermNumberArray;

    public static TermNumberPickerFragment newInstance(int termNumber) {
        TermNumberPickerFragment fragment = new TermNumberPickerFragment();
        if (termNumber > 0 && termNumber < 21) {
            Bundle args = new Bundle();
            args.putInt(ARG_TERM_NUMBER, termNumber);
            fragment.setArguments(args);
        }
        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_term_number, null);

        Bundle arguments = getArguments();
        mTermNumberPicker = view.findViewById(R.id.dialog_term_number_picker);
        mTermNumberPicker.setMinValue(1);
        if (arguments != null && arguments.containsKey(ARG_TERM_NUMBER)) {
            int termNumber = getArguments().getInt(ARG_TERM_NUMBER);

            mTermNumberPicker.setValue(termNumber);
        } else {
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Term Number:")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newTermNumber = mTermNumberPicker.getValue();
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_TERM_NUMBER, newTermNumber);
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                            }
                        })
                .create();


    }
}
