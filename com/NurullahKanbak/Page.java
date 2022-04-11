package com.NurullahKanbak;

import java.util.ArrayList;
import java.util.Arrays;

public class Page {
  PageHeader pageHeader;
  
  ArrayList<String> records = new ArrayList<>();
  
  public Page() {
    this.pageHeader = new PageHeader(0, 0, 0, false);
  }
  
  public Page(PageHeader pageHeader) {
    this.pageHeader = pageHeader;
  }
  
  public Page(String pageString) {
    String[] parts = pageString.split("!");
    this.pageHeader = new PageHeader(parts[0]);
    Logger.access("Reading " + this.pageHeader.getPageID() + " page");
    this.records = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
  }
  
  public String toString() {
    String result = this.pageHeader.toString();
    if (!this.records.isEmpty()) {
      result = result + "!";
      result = result + String.join("!", (Iterable)this.records);
    } 
    return result;
  }
}
