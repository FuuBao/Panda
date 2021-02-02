package com.example.panda;

public class WidgetItem {

    String content;
    int id;

    public WidgetItem(int id, String content) {
        this.id=id;
        this.content = content;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id=id; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
