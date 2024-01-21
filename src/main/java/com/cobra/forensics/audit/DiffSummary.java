package com.cobra.forensics.audit;

public class DiffSummary {
    private int newItems = 0;
    private int deletedItems= 0;
    private int changeItems = 0;
    private int unchangeItems = 0;
    private String dirName;

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

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public  String toString(){
        return String.format("New items=%d, Changed items=%d, Deleted items=%d, Unchanged items=%d",newItems,changeItems,deletedItems,unchangeItems);
    }
}
