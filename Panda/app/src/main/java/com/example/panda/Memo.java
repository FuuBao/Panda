package com.example.panda;

public class Memo {
    int seq;
    String maintext; //메모
    String contenttext;//내용

    public Memo(int seq, String maintext, String subtext, int isdone, String contenttext) {
        this.seq = seq;
        this.maintext = maintext;
        this.contenttext = contenttext;
    }

    public Memo(String maintext, String subtext, int isdone) {
        this.maintext = maintext;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getMaintext() {
        return maintext;
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }

    public String getContenttext() {
        return contenttext;
    }

    public void setContenttext(String contenttext) {
        this.contenttext = contenttext;
    }
}
