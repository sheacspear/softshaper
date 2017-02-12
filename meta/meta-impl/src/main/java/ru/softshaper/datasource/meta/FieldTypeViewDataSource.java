package ru.softshaper.datasource.meta;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.MetaFieldDataSourceImpl.MetaFieldObjectExtractor;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;

/**
 * Created by Sunchise on 11.10.2016.
 */
@Component
@Qualifier("fieldTypeView")
public class FieldTypeViewDataSource extends AbstractCustomDataSource<FieldTypeView> {

  private final static ObjectExtractor<FieldTypeView> objectExtractor = new FieldTypeViewExtractor();
  
  public FieldTypeViewDataSource() {
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

  @Override
  public ObjectExtractor<FieldTypeView> getObjectExtractor() {
    return objectExtractor;
  }

  public static class FieldTypeViewExtractor extends AbstractObjectExtractor<FieldTypeView> {

    private FieldTypeViewExtractor() {
      registerFieldExtractor(FieldTypeViewStaticContent.Field.code, FieldTypeView::getCode);
      registerFieldExtractor(FieldTypeViewStaticContent.Field.name, FieldTypeView::getName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(FieldTypeView obj, MetaClass metaClass) {
      return obj.getCode();
    }
  }

}
