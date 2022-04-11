package com.NurullahKanbak.Enums;

public enum FileOperation implements Operation {
  OVERWRITE("Overwrite old database file with new one (You lose your data)"),
  USE_OLD("Use old database file");
  
  private String text;
  
  FileOperation(String value) {
    this.text = value;
  }
  
  public String text() {
    return this.text;
  }
}
