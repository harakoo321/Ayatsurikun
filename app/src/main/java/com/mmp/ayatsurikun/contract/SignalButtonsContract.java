package com.mmp.ayatsurikun.contract;

public interface SignalButtonsContract {
    String getEditedText();
    void startAddButtonDialog();
    void addText(String str);
    void addButton(String text);
}
