package com.NurullahKanbak;

public class PageHeader {
  private int pageID;
  
  private int pointerToNext;
  
  private int nOfRecords;
  
  private boolean isEmpty;
  
  private int maxSize = 0;
  
  public PageHeader(int pageID, int pointerToNext, int nOfRecords, boolean isEmpty) {
    this.pageID = pageID;
    this.pointerToNext = pointerToNext;
    this.nOfRecords = nOfRecords;
    this.isEmpty = isEmpty;
  }
  
  public PageHeader(String headerStr) {
    String[] parts = headerStr.split("-");
    this.pageID = Integer.valueOf(parts[0]).intValue();
    if (parts[1].isEmpty()) {
      this.pointerToNext = 0;
    } else {
      this.pointerToNext = Integer.valueOf(parts[1]).intValue();
    } 
    this.nOfRecords = Integer.valueOf(parts[2]).intValue();
    this.isEmpty = "1".equals(parts[3]);
  }
  
  public int getPageID() {
    return this.pageID;
  }
  
  public boolean getIsEmpty() {
    return this.isEmpty;
  }
  
  public void setIsEmpty(boolean isEmpty) {
    this.isEmpty = isEmpty;
  }
  
  public int getNofRecords() {
    return this.nOfRecords;
  }
  
  public void setNofRecords(int n) {
    this.nOfRecords = n;
  }
  
  public int getPointerToNext() {
    return this.pointerToNext;
  }
  
  public void setPointerToNext(int n) {
    this.pointerToNext = n;
  }
  
  public boolean increaseRecords() {
    this.nOfRecords++;
    return true;
  }
  
  public boolean decreaseRecords() {
    this.nOfRecords--;
    return true;
  }
  
  public String toString() {
    String[] fields = { Integer.toString(this.pageID), (this.pointerToNext == 0) ? "" : Integer.toString(this.pointerToNext), Integer.toString(this.nOfRecords), !this.isEmpty ? "0" : "1" };
    return String.join("-", (CharSequence[])fields);
  }
}
