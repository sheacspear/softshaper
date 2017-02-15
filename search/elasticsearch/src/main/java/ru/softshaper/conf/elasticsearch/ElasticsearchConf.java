package ru.softshaper.conf.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@ComponentScan(basePackages = {"ru.softshaper.search.elasticsearch"})
public class ElasticsearchConf {

  public static final String HOST = "192.168.99.100";
  public static final int PORT = 9200;
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
    TransportClient transportClient = TransportClient.builder().settings(settings()).build();
    try {
      transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
      transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), 9300));
    } catch (UnknownHostException e) {
      throw new RuntimeException("Unknown host" + HOST);
    }
    return transportClient;
  }

}
