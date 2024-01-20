package com.cobra.tracker.forensics;

public class DiffSummary {
    private int newItems = 0;
    private int deletedItems= 0;
    private int changeItems = 0;
    private int unchangeItems = 0;

    public void incrementNewItems(){
        newItems++;
    }

    public void incrementDeletedItems(){
        deletedItems++;
    }

    public void incrementChangedItems(){
        changeItems++;
    }

    public void incrementUnchangedItems(){
        unchangeItems++;
    }


    public int getNewItems() {
        return newItems;
    }

    public int getDeletedItems() {
        return deletedItems;
    }

    public int getChangeItems() {
        return changeItems;
    }

    public int getUnchangeItems() {
        return unchangeItems;
    }

    public  String toString(){
        return String.format("New items=%d, Changed items=%d, Deleted items=%d, Unchanged items=%d",newItems,changeItems,deletedItems,unchangeItems);
    }
}
