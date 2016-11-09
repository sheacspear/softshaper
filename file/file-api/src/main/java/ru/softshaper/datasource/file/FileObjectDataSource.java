package ru.softshaper.datasource.file;

import java.io.InputStream;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.datasource.meta.ContentDataSource;

/**
 * Файловое хранилище
 */
public interface FileObjectDataSource extends ContentDataSource<FileObject> {

  /**
   * Создание файла
   *
   * @param fileObject описание создающегося файла
   * @param data данные файла                     *
   * @return идентифкатор
   */
  String createFile(FileObject fileObject, InputStream data);

  /**
   * Получение информации о файле файла
   *
   * @param id идентификатор
   * @return информация о файле
   */
  FileObject getFileInfo(String id);

  /**
   * Получение потока с данными файла
   *
   * @param id идентификатор
   * @return поток данных
   */
  InputStream getFileData(String id);

}
