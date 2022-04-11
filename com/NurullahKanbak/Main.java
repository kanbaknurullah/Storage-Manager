package com.NurullahKanbak;

import com.NurullahKanbak.Enums.CategoryOperation;
import com.NurullahKanbak.Enums.FileOperation;
import com.NurullahKanbak.Enums.Operation;
import com.NurullahKanbak.Enums.RecordOperation;
import com.NurullahKanbak.Enums.TypeOperation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
  static File dbFile;
  
  static Scanner keyboard = new Scanner(System.in);
  
  public static void main(String[] args) {
    Logger.print("");
    Logger.print("======= WELCOME TO KANBAK'S STORAGE MANAGER =======");
    checkDatabaseFile();
    while (true) {
      CategoryOperation category = (CategoryOperation)chooseOperation((Operation[])CategoryOperation.values(), "Please select an operation");
      switch (category) {
        case TYPE:
          handleTypeOperation();
        case RECORD:
          handleRecordOperation();
        case QUIT:
          Logger.print("Thanks for using our storage manager");
          System.exit(0);
      } 
    } 
  }
  
  public static void checkDatabaseFile() {
    File db = new File("database.txt");
    if (db.exists()) {
      FileOperation choice = (FileOperation)chooseOperation((Operation[])FileOperation.values(), "You have old database file. Please select an operation");
      switch (choice) {
        case OVERWRITE:
          createDatabaseFile();
          break;
        case USE_OLD:
          dbFile = db;
          Logger.print("");
          Logger.info("Old database file is used");
          break;
      } 
    } else {
      Logger.print("");
      Logger.warning("Old database file couldn't found");
      createDatabaseFile();
    } 
    try {
      FileManager.setOldPages(FileManager.getPages(dbFile));
    } catch (IOException e) {
      Logger.error("Couldn't gel old pages");
    } 
  }
  
  public static void createDatabaseFile() {
    dbFile = new File("database.txt");
    try (PrintWriter out = new PrintWriter(dbFile)) {
      DiscDirectory discDirectory = new DiscDirectory();
      discDirectory.addFile("FreeSpace", 2);
      discDirectory.addFile("SysCat.txt", 1);
      out.print(discDirectory);
      int totalPageCount = 714;
      for (int i = 1; i < totalPageCount; i++) {
        out.print("&");
        out.print(i);
        out.print("-");
        out.print("");
        out.print("-");
        out.print("0");
        out.print("-");
        if (i == 1) {
          out.print("0");
        } else {
          out.print("1");
        } 
      } 
      out.close();
    } catch (IOException e) {
      Logger.print("exception");
    } 
    Logger.info("database.txt is created.");
  }
  
  public static boolean isBetween(int number, int min, int max) {
    return (number > min && number < max);
  }
  
  public static Operation chooseOperation(Operation[] ops, String message) {
    Logger.print("");
    int choice = 0;
    while (true) {
      for (int i = 0; i < ops.length; i++)
        Logger.print("\t[" + (i + 1) + "] " + ops[i].text()); 
      Logger.input(message);
      choice = keyboard.nextInt();
      if (isBetween(choice, 0, ops.length + 1))
        return ops[choice - 1]; 
      Logger.warning("Please select operation between [1-" + ops.length + "]");
    } 
  }
  
  public static void handleTypeOperation() {
    TypeOperation operation = (TypeOperation)chooseOperation((Operation[])TypeOperation.values(), "Please select an type operation");
    switch (operation) {
      case CREATE:
        Type.create(dbFile);
        break;
      case DELETE:
        Type.delete(dbFile);
        break;
      case LIST:
        Type.list(dbFile);
        break;
    } 
  }
  
  public static void handleRecordOperation() {
    RecordOperation operation = (RecordOperation)chooseOperation((Operation[])RecordOperation.values(), "Please select an record operation");
    switch (operation) {
      case CREATE:
        Record.create(dbFile);
        break;
      case DELETE:
        Record.delete(dbFile);
        break;
      case RETRIEVE:
        Record.retrieve(dbFile);
        break;
      case LIST:
        Record.list(dbFile);
        break;
    } 
  }
}
