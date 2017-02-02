package ru.softshaper.datasource.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import java.util.*;

/**
 * Created by Sunchise on 11.10.2016.
 */
@Component
@Qualifier("fieldTypeView")
public class FieldTypeViewDataSource extends AbstractCustomDataSource<FieldTypeView> {

  @Autowired
  public FieldTypeViewDataSource(@Qualifier(FieldTypeViewStaticContent.META_CLASS) ObjectExtractor<FieldTypeView> objectExtractor) {
    super(objectExtractor);
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(String contentCode, String id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    return FieldTypeView.getAll().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  @Override
  protected Collection<FieldTypeView> getAllObjects(GetObjectsParams params) {
    return FieldTypeView.getAll();
  }
}
