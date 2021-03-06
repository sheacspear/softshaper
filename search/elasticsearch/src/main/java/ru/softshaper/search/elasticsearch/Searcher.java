package ru.softshaper.search.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.view.SearchResult;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Searcher {

  private final Client client;

  @Autowired
  private MetaStorage metaStorage;

  @Autowired
  private ViewSettingStore viewSetting;

  @Autowired
  public Searcher(Client client) {
    this.client = client;
  }

  public List<SearchResult> search(String text) {
    SearchRequestBuilder searchRequestBuilder = SearchAction.INSTANCE.newRequestBuilder(client);
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    String[] words = text.split("[-., ;:?!]");
    for (String word : words) {
      queryBuilder.must(QueryBuilders.queryStringQuery("*" + word + "*").analyzer("russian"));
    }
    SearchRequest searchRequest = searchRequestBuilder
        .setIndices("softshaper")
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        .setQuery(queryBuilder)
        .suggest(new SuggestBuilder().setGlobalText(text))
        .request();
    ActionFuture<SearchResponse> search = client.search(searchRequest);
    SearchResponse searchResponse = search.actionGet();
    SearchHits hits = searchResponse.getHits();
    List<SearchResult> result = new ArrayList<>();
    for (SearchHit hit : hits) {
      String type = hit.getType();
      String id = hit.getId();
      Map<String, Object> source = hit.getSource();
      MetaClass metaClass = metaStorage.getMetaClass(type);
      String title = new LinkedList<>(metaClass.getFields())
          .stream()
          .filter(metaField -> viewSetting.getView(metaField).isTitleField())
          .sorted(getMetaFieldComparator())
          .map(field -> {
            Object o = source.get(field.getCode());
            return o == null ? "" : o.toString();
          })
          .reduce((o, o2) -> o + " " + o2)
          .orElse("");
      List<String> hits1 = source.values().stream()
          .map(o -> o == null ? null : o.toString())
          .collect(Collectors.toList());
      result.add(new SearchResult(id, type, title, hits1));
    }
    return result;
  }

  private Comparator<MetaField> getMetaFieldComparator() {
    return (field1, field2) -> {
      ViewSetting o1 = viewSetting.getView(field1);
      ViewSetting o2 = viewSetting.getView(field2);
      if ((o1 == null && o2 != null) || (o2 != null && o1.getNumber() < o2.getNumber())) {
        return -1;
      }
      if ((o2 == null && o1 != null) || (o1 != null && o2.getNumber() < o1.getNumber())) {
        return 1;
      }
      return 0;
    };
  }

}
