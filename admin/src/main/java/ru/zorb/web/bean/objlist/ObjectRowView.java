package ru.zorb.web.bean.objlist;

import java.util.List;

/**
 *
 */
public class ObjectRowView {

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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the data
	 */
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

	public String getTitle() {
		return title;
	}
}