package ru.softshaper.bean.file;

import java.util.Date;

import ru.softshaper.bean.file.FileObject;

/**
 * Created by Sunchise on 08.09.2016.
 */
public class FileObjectBean implements FileObject {

  private final String id;

  private final String name;

  private final Long size;

  private final Date modificationDate;

  private final String mimeType;

  public FileObjectBean(String id, String name, Long size, Date modificationDate, String mimeType) {
    this.id = id;
    this.name = name;
    this.size = size;
    this.modificationDate = modificationDate;
    this.mimeType = mimeType;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Long getSize() {
    return size;
  }

  @Override
  public Date getModificationDate() {
    return modificationDate;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }
}
