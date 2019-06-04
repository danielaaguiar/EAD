package com.eda.ead.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Lesson implements Parcelable {

    //Dados de pesagem
    private String lesson_name;
    private String lesson_number;
    private String lesson_youtube;
    private String lesson_description;
    private String lesson_pdf;
    //Dados Lesson
    private @ServerTimestamp Date timestamp;
    private String lesson_id;
    //private String user_id;


    //*******************************************************************************************
    //Construtores
    public Lesson(String lesson_name, String lesson_number, String lesson_description, String lesson_youtube, String lesson_pdf, Date timestamp, String lesson_id, String user_id) {
        this.lesson_name = lesson_name;
        this.lesson_number= lesson_number;
        this.lesson_youtube= lesson_youtube;
        this.lesson_pdf = lesson_pdf;
        this.lesson_description = lesson_description;
        this.timestamp = timestamp;
        this.lesson_id = lesson_id;
       // this.user_id = user_id;
    }
    public Lesson(){};
    protected Lesson(Parcel in) {
        lesson_name = in.readString();
        lesson_number = in.readString();
        lesson_description = in.readString();
        lesson_youtube = in.readString();
        lesson_pdf = in.readString();
        lesson_id = in.readString();
       // user_id = in.readString();

    }

    //*******************************************************************************************
    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    //*******************************************************************************************
    @Override
    public int describeContents() {
        return 0;
    }

    //*******************************************************************************************
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lesson_name);
        parcel.writeString(lesson_number);
        parcel.writeString(lesson_description);
        parcel.writeString(lesson_youtube);
        parcel.writeString(lesson_pdf);
        parcel.writeString(lesson_id);
        //parcel.writeString(user_id);
    }

    //*******************************************************************************************
    //GETs e SETs

    public String getlesson_name() {
        return lesson_name;
    }

    public void setlesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String getlesson_number() {
        return lesson_number;
    }

    public void setlesson_number(String lesson_number) {
        this.lesson_number = lesson_number;
    }

    public static Creator<Lesson> getCREATOR() {
        return CREATOR;
    }

    public String getlesson_description() {
        return lesson_description;
    }

    public void setlesson_description(String lesson_description) {
        this.lesson_description = lesson_description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getlesson_id() {
        return lesson_id;
    }

    public void setlesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getLesson_youtube() {
        return lesson_youtube;
    }

    public void setLesson_youtube(String lesson_youtube) {
        this.lesson_youtube = lesson_youtube;
    }

    // public String getUser_id() {return user_id;}

    //public void setUser_id(String user_id) {this.user_id = user_id; }


    public String getLesson_pdf() {
        return lesson_pdf;
    }

    public void setLesson_pdf(String lesson_pdf) {
        this.lesson_pdf = lesson_pdf;
    }
}
