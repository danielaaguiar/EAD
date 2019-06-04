package com.eda.pg2_v1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
public class Skill implements Parcelable {

    //Dados de pesagem
    private String skill_name;
    private String skill_number;
    private String skill_description;
    //Dados Skill
    private @ServerTimestamp Date timestamp;
    private String skill_id;
    //private String user_id;


    //*******************************************************************************************
    //Construtores
    public Skill(String skill_name, String skill_number, String skill_description,Date timestamp, String skill_id, String user_id) {
        this.skill_name = skill_name;
        this.skill_number= skill_number;
        this.skill_description = skill_description;
        this.timestamp = timestamp;
        this.skill_id = skill_id;
       // this.user_id = user_id;
    }
    public Skill(){};
    protected Skill(Parcel in) {
        skill_name = in.readString();
        skill_number = in.readString();
        skill_description = in.readString();
        skill_id = in.readString();
       // user_id = in.readString();

    }

    //*******************************************************************************************
    public static final Creator<Skill> CREATOR = new Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel in) {
            return new Skill(in);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
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
        parcel.writeString(skill_name);
        parcel.writeString(skill_number);
        parcel.writeString(skill_description);
        parcel.writeString(skill_id);
        //parcel.writeString(user_id);
    }

    //*******************************************************************************************
    //GETs e SETs

    public String getskill_name() {
        return skill_name;
    }

    public void setskill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getskill_number() {
        return skill_number;
    }

    public void setskill_number(String skill_number) {
        this.skill_number = skill_number;
    }

    public static Creator<Skill> getCREATOR() {
        return CREATOR;
    }

    public String getskill_description() {
        return skill_description;
    }

    public void setskill_description(String skill_description) {
        this.skill_description = skill_description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getskill_id() {
        return skill_id;
    }

    public void setskill_id(String skill_id) {
        this.skill_id = skill_id;
    }

   // public String getUser_id() {return user_id;}

    //public void setUser_id(String user_id) {this.user_id = user_id; }


}
