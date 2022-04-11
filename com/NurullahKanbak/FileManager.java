package com.NurullahKanbak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {
  static String[] oldPages;
  
  public static void setOldPages(String[] olds) {
    oldPages = (String[])olds.clone();
  }
  
  public static String[] getPages(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    if ((line = br.readLine()) != null)
      return line.split("&"); 
    throw new IOException();
  }
  
  public static void writePages(File file, String[] pages) {
    for (int i = 0; i < pages.length && i < oldPages.length; i++) {
      if (!pages[i].equalsIgnoreCase(oldPages[i]))
        Logger.access("Writing " + i + " page"); 
    } 
    oldPages = (String[])pages.clone();
    try (PrintWriter out = new PrintWriter(file)) {
      String data = String.join("&", (CharSequence[])pages);
      out.print(data);
      out.close();
    } catch (IOException e) {
      Logger.error("Couldn't write the pages to file");
    } 
  }
}
