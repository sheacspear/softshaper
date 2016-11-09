package ru.softshaper.beans.workflow;

import java.util.Date;

/**
 * Created by Sunchise on 09.10.2016.
 */
public class WFTask {

  public static WFTaskBuilder newBuilder() {
    return new WFTaskBuilder();
  }

  private final String id;
  private final String name;
  private final String description;
  private final int priority;
  private final String owner;
  private final String assignee;
  private final Date createTime;
  private final Date dueDate;
  private final Date followUpDate;
  private final boolean suspended;
  private final String linkedObject;

  public WFTask(String id, String name, String description, int priority, String owner, String assignee, String linkedObject,
                Date createTime, Date dueDate, Date followUpDate, boolean suspended) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.priority = priority;
    this.owner = owner;
    this.assignee = assignee;
    this.linkedObject = linkedObject;
    this.createTime = createTime;
    this.dueDate = dueDate;
    this.followUpDate = followUpDate;
    this.suspended = suspended;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getPriority() {
    return priority;
  }

  public String getOwner() {
    return owner;
  }

  public String getAssignee() {
    return assignee;
  }

  public String getLinkedObject() {
    return linkedObject;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public Date getFollowUpDate() {
    return followUpDate;
  }

  public boolean isSuspended() {
    return suspended;
  }
}
