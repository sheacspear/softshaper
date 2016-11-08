package ru.softshaper.web.msg.websocket;

import java.io.IOException;

import javax.websocket.Session;

import com.google.common.eventbus.Subscribe;

public class WebSocketMsgLisiner {
  private final Session session;

  public WebSocketMsgLisiner(Session session) {
    this.session = session;
  }

  @Subscribe
  public void postMsg(MsgBean msg) {
    try {
      session.getBasicRemote().sendText(msg.getMsg());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
