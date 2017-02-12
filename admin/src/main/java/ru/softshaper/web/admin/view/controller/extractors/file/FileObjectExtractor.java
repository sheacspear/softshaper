package ru.softshaper.web.admin.view.controller.extractors.file;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(FileObjectStaticContent.META_CLASS)
public class FileObjectExtractor extends AbstractObjectExtractor<FileObject> {

  @Autowired
  private FileObjectDataSource fileObjectDataSource;


  @PostConstruct
  private void init() {
    registerFieldExtractor(FileObjectStaticContent.Field.mimeType, FileObject::getMimeType);
    registerFieldExtractor(FileObjectStaticContent.Field.name, FileObject::getName);
    registerFieldExtractor(FileObjectStaticContent.Field.modificationDate, FileObject::getModificationDate);
    registerFieldExtractor(FileObjectStaticContent.Field.size, FileObject::getSize);
    store.register(FileObjectStaticContent.META_CLASS, new DataSourceFromViewImpl<>(viewObjectController, fileObjectDataSource, this));
  }

  /* (non-Javadoc)
   * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object, ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(FileObject obj, MetaClass metaClass) {
    return obj.getId();
  }
}
