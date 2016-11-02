package ru.zorb.web.bean.nav;

import java.util.List;

import ru.zorb.web.bean.nav.folder.FolderView;

/**
 * @author ashek
 *
 */
public class NavigatorView {

  /**
   *
   */
  private String title;

  /**
   *
   */
  private List<FolderView> folders;

  /**
   * @return the folders
   */
  public List<FolderView> getFolders() {
    return folders;
  }

  /**
   * @param folders the folders to set
   */
  public void setFolders(List<FolderView> folders) {
    this.folders = folders;
  }

  /**
   * @return
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }
}
