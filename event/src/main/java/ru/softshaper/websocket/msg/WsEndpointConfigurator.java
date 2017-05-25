package ru.softshaper.websocket.msg;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunchise on 04.04.2017.
 */
public class WsEndpointConfigurator extends ServerEndpointConfig.Configurator {
  @Override
  public void modifyHandshake(ServerEndpointConfig config,
                              HandshakeRequest request,
                              HandshakeResponse response)
  {
    Map<String,List<String>> headers = request.getHeaders();
    List<String> cookie = headers.get("cookie");
    String myresttoken_cookie = "myresttoken";
    if (cookie != null) {
      for (String cook : cookie) {
        if (cook.contains(";")) {
          String[] subcookies = cook.split(";");
          for (String subcookie : subcookies) {
            if (subcookie.startsWith(myresttoken_cookie)) {
              config.getUserProperties().put(myresttoken_cookie, subcookie.substring(myresttoken_cookie.length() + 1));
              break;
            }
          }
        } else {
          if (cook.startsWith(myresttoken_cookie)) {
            config.getUserProperties().put(myresttoken_cookie, cook.substring(myresttoken_cookie.length() + 1));
            break;
          }
        }
      }
    }
    config.getUserProperties().put("cookie", cookie);
  }
}
