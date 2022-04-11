package com.NurullahKanbak;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Record {
  static int lastID = 0;
  
  static Scanner keyboard = new Scanner(System.in);
  
  int recordID;
  
  boolean isEmpty;
  
  String[] fields;
  
  Type type;
  
  public Record(Type type, String[] fields) {
    this.recordID = ++lastID;
    this.isEmpty = false;
    this.type = type;
    this.fields = (String[])fields.clone();
  }
  
  public Record(Type type, String typeStr) {
    String[] parts = typeStr.split("-");
    this.recordID = Integer.valueOf(parts[0]).intValue();
    this.isEmpty = "1".equals(parts[1]);
    this.type = type;
    this.fields = new String[type.getNOfFields()];
    for (int i = 2; i < parts.length; i++)
      this.fields[i - 2] = parts[i]; 
  }
  
  public boolean getIsEmpty() {
    return this.isEmpty;
  }
  
  public void setIsEmpty(boolean isEmpty) {
    this.isEmpty = isEmpty;
  }
  
  public String[] getFields() {
    return this.fields;
  }
  
  public static void create(File file) {
    try {
      ArrayList<Type> types = Type.getTypeList(file);
      for (int i = 0; i < types.size(); i++) {
        Logger.print("\t[" + (i + 1) + "] " + ((Type)types.get(i)).name);
        Logger.print("\t\t Number of Fields: " + ((Type)types.get(i)).nOfFields);
        Logger.print("\t\t Fields: " + String.join(" / ", (CharSequence[])((Type)types.get(i)).fields));
      } 
      Logger.input("Select the type that you wish to create new record [1-" + types.size() + "]");
      int typeID = keyboard.nextInt();
      Type type = types.get(typeID - 1);
      Logger.print("You are creating record for type " + type.getName());
      String[] fields = new String[type.getNOfFields()];
      for (int j = 0; j < type.getNOfFields(); j++) {
        Logger.input("Enter data for " + type.getFields()[j]);
        fields[j] = keyboard.next();
      } 
      Record record = new Record(type, fields);
      String[] pages = FileManager.getPages(file);
      DiscDirectory discDir = new DiscDirectory(pages[0]);
      int address = discDir.getAddress(type.getName() + ".txt");
      RecordPage rPage = new RecordPage(pages[address]);
      while (rPage.pageHeader.getPointerToNext() != 0) {
        address = rPage.pageHeader.getPointerToNext();
        rPage = new RecordPage(pages[address]);
      } 
      if (rPage.pageHeader.getNofRecords() == 30) {
        int freeAddress = discDir.getFreeAddress();
        pages[0] = discDir.toString();
        FileManager.writePages(file, pages);
        rPage.pageHeader.setPointerToNext(freeAddress);
        pages[address] = rPage.toString();
        FileManager.writePages(file, pages);
        rPage = new RecordPage(pages[freeAddress]);
        rPage.pageHeader.setIsEmpty(false);
        rPage.pageHeader.increaseRecords();
        rPage.records.add(record.toString());
        pages[freeAddress] = rPage.toString();
        FileManager.writePages(file, pages);
      } else {
        rPage.records.add(record.toString());
        rPage.pageHeader.increaseRecords();
        pages[address] = rPage.toString();
        FileManager.writePages(file, pages);
      } 
      Logger.print("Record for type \"" + type.getName() + "\" is added to database successfully.");
    } catch (IOException e) {
      Logger.error("Couldn't read line from text");
      System.exit(0);
    } 
  }
  
  public static void delete(File file) {
    try {
      Logger.newline();
      ArrayList<Type> types = Type.getTypeList(file);
      for (int i = 0; i < types.size(); i++) {
        Logger.print("\t[" + (i + 1) + "] " + ((Type)types.get(i)).name);
        Logger.print("\t\t Number of Fields: " + ((Type)types.get(i)).nOfFields);
        Logger.print("\t\t Fields: " + String.join(" / ", (CharSequence[])((Type)types.get(i)).fields));
      } 
      Logger.input("Select the type that you wish to delete a record [1-" + types.size() + "]");
      int typeID = keyboard.nextInt();
      Type type = types.get(typeID - 1);
      Logger.newline();
      Logger.info("Records for type" + type.getName());
      ArrayList<Record> records = getRecords(file, type);
      for (int j = 0; j < records.size(); j++) {
        Record record1 = records.get(j);
        ArrayList<String> parts = new ArrayList<>();
        for (int k = 0; k < record1.type.getNOfFields(); k++)
          parts.add(record1.type.fields[k] + ": " + record1.fields[k]); 
        Logger.print("\t[" + (j + 1) + "] => " + String.join(" / ", (Iterable)parts));
      } 
      Logger.input("Select the record that you wish to delete [1-" + records.size() + "]");
      int recordID = keyboard.nextInt();
      Record record = records.get(recordID - 1);
      Logger.info("Looking for: " + record.toString());
      String[] pages = FileManager.getPages(file);
      DiscDirectory discDir = new DiscDirectory(pages[0]);
      int address = discDir.getAddress(type.getName() + ".txt");
      RecordPage rPage = new RecordPage(pages[address]);
      rPage = new RecordPage(pages[address]);
      int loc = -1;
      while (loc == -1) {
        for (int k = 0; k < rPage.records.size(); k++) {
          Logger.info("Tring for: " + (String)rPage.records.get(k));
          if (((String)rPage.records.get(k)).equalsIgnoreCase(record.toString())) {
            loc = k;
            break;
          } 
        } 
        if (rPage.pageHeader.getPointerToNext() == 0)
          break; 
        address = rPage.pageHeader.getPointerToNext();
        rPage = new RecordPage(pages[address]);
      } 
      if (loc == -1) {
        Logger.error("Couldn't find record");
      } else {
        record.setIsEmpty(true);
        rPage.records.set(loc, record.toString());
        rPage.pageHeader.decreaseRecords();
        pages[address] = rPage.toString();
        FileManager.writePages(file, pages);
        Logger.newline();
        Logger.info("Record is deleted from database successfully.");
      } 
    } catch (IOException e) {
      Logger.error("Couldn't read line from text");
      System.exit(0);
    } 
  }
  
  public static void retrieve(File file) {
    ArrayList<Type> types = Type.getTypeList(file);
    for (int i = 0; i < types.size(); i++) {
      Logger.print("\t[" + (i + 1) + "] " + ((Type)types.get(i)).name);
      Logger.print("\t\t Number of Fields: " + ((Type)types.get(i)).nOfFields);
      Logger.print("\t\t Fields: " + String.join(" / ", (CharSequence[])((Type)types.get(i)).fields));
    } 
    Logger.input("Select the type that you wish to retrieve record [1-" + types.size() + "]");
    int typeID = keyboard.nextInt();
    Type type = types.get(typeID - 1);
    Logger.print("Primary key for type " + type.getName() + " is " + type.getFields()[0]);
    Logger.input("Enter " + type.getFields()[0] + " for retrieve record");
    String pk = keyboard.next();
    ArrayList<Record> records = getRecords(file, type);
    Record found = null;
    for (Record record : records) {
      if (pk.equalsIgnoreCase(record.fields[0])) {
        found = record;
        break;
      } 
    } 
    if (found != null) {
      Logger.print("The record is found");
      Logger.print("\t[" + found.recordID + "] Type " + found.type.getName());
      ArrayList<String> parts = new ArrayList<>();
      for (int j = 0; j < found.type.getNOfFields(); j++)
        parts.add(found.type.fields[j] + ": " + found.fields[j]); 
      Logger.print("\t\t" + String.join(" / ", (Iterable)parts));
    } else {
      Logger.error("The record that has given primary key couldn't found");
    } 
  }
  
  public static void list(File file) {
    Logger.newline();
    ArrayList<Type> types = Type.getTypeList(file);
    for (int i = 0; i < types.size(); i++) {
      Logger.print("\t[" + (i + 1) + "] " + ((Type)types.get(i)).name);
      Logger.print("\t\t Number of Fields: " + ((Type)types.get(i)).nOfFields);
      Logger.print("\t\t Fields: " + String.join(" / ", (CharSequence[])((Type)types.get(i)).fields));
    } 
    Logger.input("Select the type that you wish to list records [1-" + types.size() + "]");
    int typeID = keyboard.nextInt();
    Type type = types.get(typeID - 1);
    Logger.newline();
    Logger.info("Records for type " + type.getName());
    ArrayList<Record> records = getRecords(file, type);
    for (int j = 0; j < records.size(); j++) {
      Record record = records.get(j);
      ArrayList<String> parts = new ArrayList<>();
      for (int k = 0; k < record.type.getNOfFields(); k++)
        parts.add(record.type.fields[k] + ": " + record.fields[k]); 
      Logger.print("\t[" + record.recordID + "] => " + String.join(" / ", (Iterable)parts));
    } 
  }
  
  public static ArrayList<Record> getRecords(File file, Type type) {
    ArrayList<Record> records = new ArrayList<>();
    try {
      String[] pages = FileManager.getPages(file);
      DiscDirectory discDir = new DiscDirectory(pages[0]);
      int address = discDir.getAddress(type.getName() + ".txt");
      RecordPage rPage = new RecordPage(pages[address]);
      for (String str : rPage.records) {
        Record record = new Record(type, str);
        if (!record.getIsEmpty())
          records.add(record); 
      } 
      while (rPage.pageHeader.getPointerToNext() != 0) {
        rPage = new RecordPage(pages[rPage.pageHeader.getPointerToNext()]);
        for (String str : rPage.records) {
          Record record = new Record(type, str);
          if (!record.getIsEmpty())
            records.add(record); 
        } 
      } 
    } catch (IOException e) {
      Logger.error("Couldn't read line from text");
      System.exit(0);
    } 
    return records;
  }
  
  public String toString() {
    ArrayList<String> parts = new ArrayList<>();
    parts.add(Integer.toString(this.recordID));
    if (this.isEmpty) {
      parts.add("1");
    } else {
      parts.add("0");
    } 
    parts.addAll(Arrays.asList(this.fields));
    return String.join("-", (Iterable)parts);
  }
}
