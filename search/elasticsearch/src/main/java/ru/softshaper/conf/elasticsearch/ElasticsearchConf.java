package ru.softshaper.conf.elasticsearch;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@ComponentScan(basePackages = { "ru.softshaper.search.elasticsearch" })
@PropertySource("classpath:application.properties")

public class ElasticsearchConf {

  private static final String ELASTICSEARCH_HOST = "elasticsearchHost";

  private static final String ELASTICSEARCH_PORT = "elasticsearchPort";

  /**
   * Environment
   */
  @Autowired
  private Environment env;

  // public static final String HOST = "192.168.99.100";
  // public static final int PORT = 9200;
  private String clusterName = "softshaper";
  private Boolean clientTransportSniff = true;
  private Boolean clientIgnoreClusterName = Boolean.FALSE;
  private String clientPingTimeout = "5s";
  private String clientNodesSamplerInterval = "5s";

  public Settings.Builder settings() {
    try {
      /*return Settings.builder().loadFromSource(XContentFactory.jsonBuilder().startObject().startObject("analysis").startObject("filter")
          .startObject("russian_stop").field("type", "stop").field("stopwords", "_russian_").endObject()
          *//*
           * .startObject("russian_keywords") .field("type", "keyword_marker")
           * .field("keywords", new String[]{}) .endObject()
           *//*
          .startObject("russian_stemmer").field("type", "stemmer").field("language", "russian").endObject().endObject().startObject("analyzer")
          .startObject("russian").field("type", "custom").field("tokenizer", "standard")
          .field("filter",
              new String[] { "lowercase",
                  *//* "russian_morphology", "english_morphology", *//* "russian_stop", *//* "russian_keywords", *//* "russian_stemmer" })
          .endObject().endObject().endObject().endObject().string());*/
      return Settings.builder().loadFromStream("elasticsearch-index-settings.json", getClass().getClassLoader().getResourceAsStream("elasticsearch-index-settings.json"));
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Bean
  public TransportClient transportClient() {
    String host = env.getProperty(ELASTICSEARCH_HOST);
    String port = env.getProperty(ELASTICSEARCH_PORT);
    TransportClient client = null;
    try {
      // settings().build()

      Settings settings = Settings.builder().put("cluster.name", "softshaper").build();

      client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.valueOf(port)));
    } catch (UnknownHostException e) {
      throw new RuntimeException("Unknown host" + host);
    }
    
    IndicesExistsResponse softshaper = client.admin().indices().prepareExists("softshaper").execute().actionGet();
    
    //  if (softshaper.isExists()) {
    //  client.admin().indices().prepareDelete("softshaper").execute().actionGet(
    //  ); }
     

    if (!softshaper.isExists()) {
      client.admin().indices().prepareCreate("softshaper").setSettings(settings()).execute().actionGet();
    }
    
   
    final ClusterHealthResponse res = client.admin().cluster().health(new ClusterHealthRequest()).actionGet();
    System.out.println(res.getClusterName());
    return client;
  }
}
