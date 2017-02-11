package ru.softshaper.staticcontent.file.extractors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.staticcontent.meta.extractors.AbstractObjectExtractor;

@Component
@Qualifier(FileObjectStaticContent.META_CLASS)
public class FileObjectExtractor extends AbstractObjectExtractor<FileObject> {

  public FileObjectExtractor() {
    registerFieldExtractor(FileObjectStaticContent.Field.mimeType, FileObject::getMimeType);
    registerFieldExtractor(FileObjectStaticContent.Field.name, FileObject::getName);
    registerFieldExtractor(FileObjectStaticContent.Field.modificationDate, FileObject::getModificationDate);
    registerFieldExtractor(FileObjectStaticContent.Field.size, FileObject::getSize);
  }

  @Override
  public String getId(FileObject obj, MetaClass metaClass) {
    return obj.getId();
  }
}
