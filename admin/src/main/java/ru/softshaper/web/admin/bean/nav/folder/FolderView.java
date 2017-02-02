package ru.softshaper.web.admin.bean.nav.folder;

import java.util.List;

/**
 *
 */
public class FolderView {

	/**
	 *
	 */
	private String name;

	/**
	 *
	 */
	private String type;

	/**
	 *
	 */
	private String id;
	 /**
  *
  */
 private String cnt;


	/**
	 *
	 */
	private List<FolderView> folders;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the folders
	 */
	public List<FolderView> getFolders() {
		return folders;
	}

	/**
	 * @param folders
	 *            the folders to set
	 */
	public void setFolders(List<FolderView> folders) {
		this.folders = folders;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

  /**
   * @return the cnt
   */
  public String getCnt() {
    return cnt;
  }

  /**
   * @param cnt the cnt to set
   */
  public void setCnt(String cnt) {
    this.cnt = cnt;
  }

}
