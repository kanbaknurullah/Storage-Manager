package com.NurullahKanbak.Enums;

public enum CategoryOperation implements Operation {
  TYPE("Type operations (DDL)"),
  RECORD("Record operations (DML)"),
  QUIT("Quit the program");
  
  private String text;
  
  CategoryOperation(String value) {
    this.text = value;
  }
  
  public String text() {
    return this.text;
  }
}
