package com.example.panda;

public class Memo {
    private String title;
    private String contents;
    private String pw;

    public Memo(String title, String contents) { //비밀번호 없음
        this.title=title;
        this.contents=contents;
    }

    public Memo(String title, String contents, String pw) { //비밀번호 있음
        this.title=title;
        this.contents=contents;
        this.pw=pw;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setContents(String contents) {
        this.contents=contents;
    }

    public void setPw(String pw) {
        this.pw=pw;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public String getPw() {
        return this.pw;
    }
}
