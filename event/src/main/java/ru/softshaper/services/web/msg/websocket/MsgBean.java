package ru.softshaper.web.msg.websocket;

public class MsgBean {

  private final String msg;
  private final String user;

  public MsgBean(String msg, String user) {
    super();
    this.msg = msg;
    this.user = user;
  }

  /**
   * @return the msg
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @return the user
   */
  public String getUser() {
    return user;
  }

}
