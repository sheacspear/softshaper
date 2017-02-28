package ru.softshaper.web.admin.bean.objlist.impl;

import java.util.List;

import ru.softshaper.web.admin.bean.objlist.IObjectRowView;

/**
 *
 */
public class ObjectRowView implements IObjectRowView {

	/**
	 *
	 */
	private final String id;

	/**
	 *
	 */
	private final String title;
	/**
	 *
	 */
	private final List<Object> data;

	/**
	 * @param id
	 * @param data
	 */
	public ObjectRowView(String id, List<Object> data, String title) {
		super();
		this.id = id;
		this.data = data;
		this.title = title;
	}

	/* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IObjectRowView#getId()
   */
	@Override
  public String getId() {
		return id;
	}

	/* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IObjectRowView#getData()
   */
	@Override
  public List<Object> getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObjectRowView [id=" + id + ", data=" + data + "]";
	}

	/* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IObjectRowView#getTitle()
   */
	@Override
  public String getTitle() {
		return title;
	}
}