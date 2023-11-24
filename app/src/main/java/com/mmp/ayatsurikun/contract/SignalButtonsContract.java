package com.mmp.ayatsurikun.contract;

public interface SignalButtonsContract {
    void startAddButtonDialog();
    void addButton(String text);
    void showToast(String text);
}
