package ru.softshaper.services.drools.bean;

public class Data {

  private String fileName;

  private byte[] data;

  public Data(String fileName, byte[] data) {
    this.fileName = fileName;
    this.data = data;
  }

  @Override
  public String toString() {
    return fileName ;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
