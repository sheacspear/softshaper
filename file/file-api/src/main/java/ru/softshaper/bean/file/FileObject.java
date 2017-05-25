package ru.softshaper.bean.file;

import java.util.Date;

/**
 * Created by Sunchise on 08.09.2016.
 */
public interface FileObject {
  
  String getId();

  String getName();

  Long getSize();

  Date getModificationDate();

  String getMimeType();
}
