package com.eda.estoque.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
public class Note implements Parcelable {

    //Dados de pesagem
    private String peso;
    private String musculo;
    private String bf;
    private String vf;
    private String cintura;
    private String braco;
    //Dados de musculacao
    private String malhacao_hora_inicio;
    private String malhacao_hora_final;
    private String malhacao_exercicio;
    //Dados Note
   // private @ServerTimestamp Date timestamp;
    private String note_id;
    private String user_id;


    //*******************************************************************************************
    //Construtores
    public Note(String peso, String musculo, String bf, String vf, String cintura, String braco, String malhacao_hora_inicio, String malhacao_hora_final, String malhacao_exercicio, Date timestamp, String note_id, String user_id) {
        this.peso = peso;
        this.musculo = musculo;
        this.bf = bf;
        this.vf = vf;
        this.cintura = cintura;
        this.braco = braco;
        this.malhacao_hora_inicio = malhacao_hora_inicio;
        this.malhacao_hora_final = malhacao_hora_final;
        this.malhacao_exercicio = malhacao_exercicio;
       // this.timestamp = timestamp;
        this.note_id = note_id;
        this.user_id = user_id;
    }
    public Note(){};
    protected Note(Parcel in) {
        peso = in.readString();
        musculo = in.readString();
        bf = in.readString();
        vf = in.readString();
        cintura = in.readString();
        braco = in.readString();
        malhacao_hora_inicio = in.readString();
        malhacao_hora_final = in.readString();
        malhacao_exercicio = in.readString();
        note_id = in.readString();
        user_id = in.readString();

    }

    //*******************************************************************************************
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
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
        parcel.writeString(peso);
        parcel.writeString(musculo);
        parcel.writeString(bf);
        parcel.writeString(vf);
        parcel.writeString(cintura);
        parcel.writeString(braco);
        parcel.writeString(malhacao_hora_inicio);
        parcel.writeString(malhacao_hora_final);
        parcel.writeString(malhacao_exercicio);
        parcel.writeString(note_id);
        parcel.writeString(user_id);
    }

    //*******************************************************************************************
    //GETs e SETs

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getMusculo() {
        return musculo;
    }

    public void setMusculo(String musculo) {
        this.musculo = musculo;
    }

    public String getBf() {
        return bf;
    }

    public void setBf(String bf) {
        this.bf = bf;
    }

    public String getVf() {
        return vf;
    }

    public void setVf(String vf) {
        this.vf = vf;
    }

    public String getCintura() {
        return cintura;
    }

    public void setCintura(String cintura) {
        this.cintura = cintura;
    }

    public String getBraco() {
        return braco;
    }

    public void setBraco(String braco) {
        this.braco = braco;
    }

    public String getMalhacao_hora_inicio() {
        return malhacao_hora_inicio;
    }

    public void setMalhacao_hora_inicio(String malhacao_hora_inicio) {
        this.malhacao_hora_inicio = malhacao_hora_inicio;
    }

    public String getMalhacao_hora_final() {
        return malhacao_hora_final;
    }

    public void setMalhacao_hora_final(String malhacao_hora_final) {
        this.malhacao_hora_final = malhacao_hora_final;
    }

    public String getMalhacao_exercicio() {
        return malhacao_exercicio;
    }

    public void setMalhacao_exercicio(String malhacao_exercicio) {
        this.malhacao_exercicio = malhacao_exercicio;
    }
//
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
