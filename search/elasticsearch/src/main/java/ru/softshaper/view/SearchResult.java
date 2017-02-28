package ru.softshaper.view;

import java.util.Collection;

/**
 * Created by Sunchise on 28.02.2017.
 */
public class SearchResult {

  private final String id;

  private final String metaClassCode;

  private final String title;

  private final Collection<String> hits;

  public SearchResult(String id, String metaClassCode, String title, Collection<String> hits) {
    this.id = id;
    this.metaClassCode = metaClassCode;
    this.title = title;
    this.hits = hits;
  }

  public String getId() {
    return id;
  }

  public String getMetaClassCode() {
    return metaClassCode;
  }

  public String getTitle() {
    return title;
  }

  public Collection<String> getHits() {
    return hits;
  }
}
