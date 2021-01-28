package com.example.panda;

public class Memo {
    private String title;
    private String contents;
    private boolean pw;
    private String d;

    public Memo() {}

    public Memo(String title, String d) { //리스트에 보여질 제목과 날짜만 저장
        this.title=title;
        this.d=d;
    }

    public Memo(String title, String contents, boolean pw, String d) { //db에 저장
        this.title=title;
        this.contents=contents;
        this.pw=pw;
        this.d=d;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setContents(String contents) {
        this.contents=contents;
    }

    public void setD(String d) { this.d=d; }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public boolean getPw() {
        return this.pw;
    }

    public String getD() { return this.d; }
}
