package com.example.brzostek.project1;

import android.graphics.Color;

public enum TextColor {
    BLACK("Domyślny", Color.BLACK),
    RED("Czerwony", Color.RED),
    GREEN("Zielony", Color.GREEN),
    BLUE("Niebieski", Color.BLUE);

    private String displayableText;
    private int androidColor;

    TextColor(String displayableText, int androidColor) {
        this.displayableText = displayableText;
        this.androidColor = androidColor;
    }

    public int toAndroidColor() {
        return androidColor;
    }

    @Override
    public String toString() {
        return displayableText;
    }
}
