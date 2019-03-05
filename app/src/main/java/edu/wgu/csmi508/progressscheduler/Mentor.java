package edu.wgu.csmi508.progressscheduler;

import java.util.UUID;

public class Mentor {
    private String mName;
    private String mPhoneNumber;
    private String mEmailAddress;

    private UUID mMentorId;


    public Mentor() {
        this(UUID.randomUUID());
    }

    public Mentor(UUID id) {
        mMentorId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public UUID getMentorId() {
        return mMentorId;
    }

    @Override
    public String toString() {
        return mName;
    }
}
