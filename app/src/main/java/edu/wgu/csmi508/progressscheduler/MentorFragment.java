package edu.wgu.csmi508.progressscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

public class MentorFragment extends Fragment {

    private static final String ARG_MENTOR_ID = "mentor_id";


    private Mentor mMentor;
    private EditText mNameEditText;
    private EditText mPhoneNumberEditText;
    private EditText mEmailAddressEditText;

    public static MentorFragment newInstance(UUID mentorId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENTOR_ID, mentorId);

        MentorFragment fragment = new MentorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID mentorId = (UUID) getArguments().getSerializable(ARG_MENTOR_ID);
        mMentor = MentorList.getMentorList(getActivity()).getMentor(mentorId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        MentorList.getMentorList(getActivity()).updateMentor(mMentor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentor, container, false);

        mNameEditText = view.findViewById(R.id.mentor_name_edittext);
        mPhoneNumberEditText = view.findViewById(R.id.mentor_phone_number_edittext);
        mEmailAddressEditText = view.findViewById(R.id.mentor_email_edittext);

        if (mMentor != null) {
            if (mMentor.getName() != null) {
                mNameEditText.setText(mMentor.getName());
            }
            if (mMentor.getPhoneNumber() != null) {
                mPhoneNumberEditText.setText(mMentor.getPhoneNumber());
            }
            if (mMentor.getEmailAddress() != null) {
                mEmailAddressEditText.setText(mMentor.getEmailAddress());
            }
        }

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMentor.setName(s.toString());
                MentorList.getMentorList(getActivity()).updateMentor(mMentor);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMentor.setPhoneNumber(s.toString());
                MentorList.getMentorList(getActivity()).updateMentor(mMentor);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mEmailAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMentor.setEmailAddress(s.toString());
                MentorList.getMentorList(getActivity()).updateMentor(mMentor);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mentor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_mentor:
                MentorList mentorList = MentorList.getMentorList(getActivity());
                mentorList.removeMentor(mMentor);
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
