package ru.softshaper.services.meta.impl.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClassMutable;
import ru.softshaper.services.meta.MetaFieldMutable;
import ru.softshaper.services.meta.MetaLoader;
import ru.softshaper.services.meta.StaticContent;

/**
 * Created by Sunchise on 11.08.2016.
 */
@Component
@Qualifier("StaticContentLoader")
public class StaticContentLoader implements MetaLoader {

  private Collection<StaticContent> staticContents = new ArrayList<StaticContent>();

  public void register(StaticContent staticContent) {
    staticContents.add(staticContent);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaLoader#loadClasses()
   */
  @Override
  public Map<MetaClassMutable, ContentDataSource<?>> loadClasses() {
    Map<MetaClassMutable, ContentDataSource<?>> result = Maps.newHashMap();
    for (StaticContent staticContent : staticContents) {
      result.put(staticContent.getMetaClass(), staticContent.getContentDataSource());
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaLoader#loadFields(java.util.Map, java.util.Map)
   */
  @Override
  public Collection<MetaFieldMutable> loadFields(Map<String, MetaClassMutable> clazzCode, Map<String, MetaClassMutable> clazzId) {
    Collection<MetaFieldMutable> result = Lists.newArrayList();
    for (StaticContent staticContent : staticContents) {
      result.addAll(staticContent.loadFields(clazzCode));
    }
    return result;
  }
}
