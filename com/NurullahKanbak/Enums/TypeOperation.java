package com.NurullahKanbak.Enums;

public enum TypeOperation implements Operation {
  CREATE("Create a new type"),
  DELETE("Delete type"),
  LIST("List all types");
  
  private String text;
  
  TypeOperation(String value) {
    this.text = value;
  }
  
  public String text() {
    return this.text;
  }
}
