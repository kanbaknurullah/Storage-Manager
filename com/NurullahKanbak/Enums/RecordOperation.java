package com.NurullahKanbak.Enums;

public enum RecordOperation implements Operation {
  CREATE("Create a new record"),
  DELETE("Delete record"),
  RETRIEVE("Retrieve a record"),
  LIST("List all records of a type");
  
  private String text;
  
  RecordOperation(String value) {
    this.text = value;
  }
  
  public String text() {
    return this.text;
  }
}
