package ru.softshaper.conf.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@ComponentScan(basePackages = {"ru.softshaper.search.elasticsearch"})
@PropertySource("classpath:application.properties")
public class ElasticsearchConf {
  
  private static final String ELASTICSEARCH_HOST = "elasticsearchHost";
  
  private static final String ELASTICSEARCH_PORT = "elasticsearchPort";





  /**
  * Environment
  */
  @Autowired
  private Environment env;
	

  //public static final String HOST = "192.168.99.100";
  //public static final int PORT = 9200;
  private String clusterName = "elasticsearch";
  private Boolean clientTransportSniff = true;
  private Boolean clientIgnoreClusterName = Boolean.FALSE;
  private String clientPingTimeout = "5s";
  private String clientNodesSamplerInterval = "5s";

  public Settings settings() {
    return Settings.builder()
        .put("cluster.name", clusterName)
  /*      .put("client.transport.sniff", clientTransportSniff)
        .put("client.transport.ignore_cluster_name", clientIgnoreClusterName)
        .put("client.transport.ping_timeout", clientPingTimeout)
        .put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)*/
        .build();
  }

  @Bean
  public TransportClient transportClient() {    
    String host = env.getProperty(ELASTICSEARCH_HOST);
    String port = env.getProperty(ELASTICSEARCH_PORT);    
    TransportClient transportClient = TransportClient.builder().settings(settings()).build();
    try {

      transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.valueOf(port)));
      //transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), 9300));
    } catch (UnknownHostException e) {
      throw new RuntimeException("Unknown host" + host);
    }
    return transportClient;
  }

}
