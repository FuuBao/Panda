package com.example.panda;

public class WidgetItem {
    int title;
    String content;

    public WidgetItem(int title, String content) {
        this.title = title;
        this.content = content;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
