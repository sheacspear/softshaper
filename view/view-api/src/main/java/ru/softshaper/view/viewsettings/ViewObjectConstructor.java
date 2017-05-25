package ru.softshaper.view.viewsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;

import java.util.function.BinaryOperator;

/**
 * Created by Sunchise on 21.02.2017.
 */
@Component
public class ViewObjectConstructor {

  private final ViewSettingStore viewSettingStore;

  private final DataSourceStorage dataSourceStorage;

  @Autowired
  public ViewObjectConstructor(ViewSettingStore viewSettingStore, DataSourceStorage dataSourceStorage) {
    this.viewSettingStore = viewSettingStore;
    this.dataSourceStorage = dataSourceStorage;
  }

  public <T> ViewObject construct(MetaClass metaClass, final T object) {
    ContentDataSource<T> dataSource = (ContentDataSource<T>) dataSourceStorage.get(metaClass);
    final ObjectExtractor<T> objectExtractor = dataSource.getObjectExtractor();
    String id = objectExtractor.getId(object, metaClass);
    String title = "";
/*    LinkedHashMap<? extends MetaField, Object> values = metaClass.getFields()
        .stream()
        //.sorted(Comparator.<MetaField, Integer>comparing(o -> viewSettingStore.getView(o).getNumber()))
        .collect(Collectors.toMap(metaField -> metaField,
            metaField -> objectExtractor.getValue(object, metaField),
            throwingMerger(),
            LinkedHashMap::new));*/


    return null;//new ViewObjectImpl();
  }

  private static <T> BinaryOperator<T> throwingMerger() {
    return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
  }
}
