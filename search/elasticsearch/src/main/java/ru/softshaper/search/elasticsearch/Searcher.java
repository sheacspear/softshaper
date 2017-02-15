package ru.softshaper.search.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunchise on 15.02.2017.
 */
@Component
public class Searcher {

  private final Client client;

  @Autowired
  public Searcher(Client client) {
    this.client = client;
  }

  public List<String> search(String text) {
    SearchRequestBuilder searchRequestBuilder = SearchAction.INSTANCE.newRequestBuilder(client);
    SearchRequest searchRequest = searchRequestBuilder
        .setQuery(new SimpleQueryStringBuilder(text))
        .setSuggestText(text)
        .setIndices("zorb")
        .setTypes("titles")
        .addField("title")
        .request();
    ActionFuture<SearchResponse> search = client.search(searchRequest);
    SearchResponse searchResponse = search.actionGet();
    SearchHits hits = searchResponse.getHits();
    List<String> result = new ArrayList<>();
    for (SearchHit hit : hits) {
      result.add(hit.getSourceAsString());
    }
    return result;
  }

}
