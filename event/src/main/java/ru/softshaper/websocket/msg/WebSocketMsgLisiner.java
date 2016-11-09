package ru.softshaper.websocket.msg;

import java.io.IOException;

import javax.websocket.Session;

import com.google.common.eventbus.Subscribe;

import ru.softshaper.websocket.msg.bean.MsgBean;

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
