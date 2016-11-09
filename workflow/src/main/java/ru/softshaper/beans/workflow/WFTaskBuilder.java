package ru.softshaper.beans.workflow;

import java.util.Date;

public class WFTaskBuilder {
  private String id;
  private String name;
  private String description;
  private int priority;
  private String owner;
  private String assignee;
  private String linkedObject;
  private Date createTime;
  private Date dueDate;
  private Date followUpDate;
  private boolean suspended;

  public WFTaskBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public WFTaskBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public WFTaskBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public WFTaskBuilder setPriority(int priority) {
    this.priority = priority;
    return this;
  }

  public WFTaskBuilder setOwner(String owner) {
    this.owner = owner;
    return this;
  }

  public WFTaskBuilder setAssignee(String assignee) {
    this.assignee = assignee;
    return this;
  }

  public WFTaskBuilder setLinkedObject(String linkedObject) {
    this.linkedObject = linkedObject;
    return this;
  }

  public WFTaskBuilder setCreateTime(Date createTime) {
    this.createTime = createTime;
    return this;
  }

  public WFTaskBuilder setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public WFTaskBuilder setFollowUpDate(Date followUpDate) {
    this.followUpDate = followUpDate;
    return this;
  }

  public WFTaskBuilder setSuspended(boolean suspended) {
    this.suspended = suspended;
    return this;
  }

  public WFTask build() {
    return new WFTask(id, name, description, priority, owner, assignee, linkedObject, createTime, dueDate, followUpDate, suspended);
  }
}