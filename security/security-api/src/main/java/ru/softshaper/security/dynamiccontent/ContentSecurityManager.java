package ru.softshaper.security.dynamiccontent;

import ru.softshaper.services.meta.MetaClass;

import java.util.Collection;

/**
 * Менеджер безопасности<br/>
 *
 * @author ashek
 *
 */
public interface ContentSecurityManager {

  /**
   * Проверяет право
   *
   * @param dynamicContentId ID content
   * @return право
   */
  boolean canCreate(String dynamicContentId);

  /**
   * Проверяет право
   *
   * @param dynamicContentId ID content
   * @return право
   */
  boolean canRead(String dynamicContentId);

  /**
   * Проверяет право
   *
   * @param dynamicContentId ID content
   * @return право
   */
  boolean canUpdate(String dynamicContentId);

  /**
   * Проверяет право
   *
   * @param dynamicContentId ID content
   * @return право
   */
  boolean canDelete(String dynamicContentId);

  /**
   * @return текущие роли
   */
  Collection<String> getCurrentUserRoles();

  /**
   * @return идентификатор текущего пользователя
   */
  Long getCurrentUserId();

  String getCurrentUserLogin();

  /**
   * Дать права
   * @param metaClass
   * @param role
   * @param c
   * @param r
   * @param u
   * @param d
   */
  void addRight(MetaClass metaClass, String role, boolean c, boolean r, boolean u, boolean d);

  /**
   * Удалить все права
   * @param metaClass
   */
  void deleteAllRight(MetaClass metaClass);

}