package com.NurullahKanbak;

public class DiscDirectory extends Page {
  public DiscDirectory() {}
  
  public DiscDirectory(PageHeader pageHeader) {
    super(pageHeader);
  }
  
  public DiscDirectory(String pageString) {
    super(pageString);
  }
  
  public int addFile(String fileName) {
    int address = getFreeAddress();
    this.records.add(fileName + "-" + address);
    return address;
  }
  
  public void addFile(String fileName, int pageID) {
    this.records.add(fileName + "-" + pageID);
  }
  
  public int removeFile(String fileName) {
    int address = 0;
    for (int i = 0; i < this.records.size(); i++) {
      String[] fields = ((String)this.records.get(i)).split("-");
      if (fields[0].equalsIgnoreCase(fileName)) {
        address = Integer.valueOf(fields[1]).intValue();
        this.records.remove(i);
        break;
      } 
    } 
    return address;
  }
  
  public int getAddress(String fileName) {
    for (String record : this.records) {
      String[] fields = record.split("-");
      if (fields[0].equalsIgnoreCase(fileName))
        return Integer.valueOf(fields[1]).intValue(); 
    } 
    return 0;
  }
  
  public int getSysCatAddress() {
    return getAddress("SysCat.txt");
  }
  
  public int getFreeAddress() {
    int freeAddress = getAddress("FreeSpace");
    setFreeAddress(freeAddress + 1);
    return freeAddress;
  }
  
  public void setFreeAddress(int address) {
    for (int i = 0; i < this.records.size(); i++) {
      String[] fields = ((String)this.records.get(i)).split("-");
      if (fields[0].equalsIgnoreCase("FreeSpace"))
        this.records.set(i, fields[0] + "-" + address); 
    } 
  }
}
