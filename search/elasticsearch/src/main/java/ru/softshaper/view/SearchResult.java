package ru.softshaper.view;

import java.util.Collection;

/**
 * Created by Sunchise on 28.02.2017.
 */
public class SearchResult {

  private final String id;

  private final String contentCode;

  private final String title;

  private final Collection<String> hits;

  public SearchResult(String id, String contentCode, String title, Collection<String> hits) {
    this.id = id;
    this.contentCode = contentCode;
    this.title = title;
    this.hits = hits;
  }

  public String getId() {
    return id;
  }

  public String getContentCode() {
    return contentCode;
  }

  public String getTitle() {
    return title;
  }

  public Collection<String> getHits() {
    return hits;
  }
}
