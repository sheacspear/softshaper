package ru.zorb.security.dynamiccontent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.meta.conditions.Condition;
import ru.zorb.services.meta.conditions.ConditionManager;
import ru.zorb.services.meta.impl.GetObjectsParams;
import ru.zorb.services.meta.staticcontent.sec.SecUserStaticContent;
import ru.zorb.storage.jooq.tables.daos.DynamicContentSecurityDao;
import ru.zorb.storage.jooq.tables.pojos.DynamicContentSecurity;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Менеджер безопасности<br/>
 * Created by Sunchise on 12.07.2016.
 */
@Service
@ThreadSafe
public class DynamicContentSecurityManager implements ContentSecurityManager {

  /**
   * Настройки доступа
   */
  @Autowired
  private DynamicContentSecurityDao securityRepository;

  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> userDataSource;

  /**
   * DSLContext
   */
  @Autowired
  private DSLContext dslContext;

  @Autowired
  private ConditionManager conditionManager;

  @Autowired
  private MetaStorage metaStorage;

  /**
   * securityMap
   */
  private Map<RoleDynamicContentPair, DynamicContentSecurity> securityMap = Maps.newHashMap();

  /**
   *
   */
  public DynamicContentSecurityManager() {
  }

  /**
   * Инит
   */
  @PostConstruct
  public synchronized void init() {
    final Iterable<DynamicContentSecurity> dynamicContentSecurities = securityRepository.findAll();
    Map<RoleDynamicContentPair, DynamicContentSecurity> securityMap2 = Maps.newHashMap(securityMap);
    dynamicContentSecurities.forEach(dynamicContentSecurity -> {
      RoleDynamicContentPair key = new RoleDynamicContentPair(dynamicContentSecurity.getDynamicContentId().toString(), dynamicContentSecurity.getRole());
      securityMap2.put(key, dynamicContentSecurity);
      securityMap = securityMap2;
    });
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#canCreate(java.lang.Long)
   */
  @Override
  public boolean canCreate(final String dynamicContentId) {
    return checkAccessLevel(dynamicContentId, DynamicContentSecurity::getCanCreate);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#canRead(java.lang.Long)
   */
  @Override
  public boolean canRead(final String dynamicContentId) {
    return checkAccessLevel(dynamicContentId, DynamicContentSecurity::getCanRead);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#canUpdate(java.lang.Long)
   */
  @Override
  public boolean canUpdate(final String dynamicContentId) {
    return checkAccessLevel(dynamicContentId, DynamicContentSecurity::getCanUpdate);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#canDelete(java.lang.Long)
   */
  @Override
  public boolean canDelete(final String dynamicContentId) {
    return checkAccessLevel(dynamicContentId, DynamicContentSecurity::getCanDelete);
  }

  /**
   * @param dynamicContentId ID content
   * @param func тип проверки
   * @return
   */
  private boolean checkAccessLevel(final String dynamicContentId, Predicate<DynamicContentSecurity> func) {
    final Collection<String> roles = getCurrentUserRoles();
    for (String role : roles) {
      DynamicContentSecurity dynamicContentSecurity;
      dynamicContentSecurity = securityMap.get(new RoleDynamicContentPair(dynamicContentId, role));
      if (dynamicContentSecurity != null && func.test(dynamicContentSecurity)) {
        return true;
      }
    }
    return false;
  }



  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#getCurrentUserRoles()
   */
  @Override
  public Collection<String> getCurrentUserRoles() {
    SecurityContext context = SecurityContextHolder.getContext();
    Preconditions.checkNotNull(context);
    Authentication authentication = context.getAuthentication();
    Preconditions.checkNotNull(authentication);
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Preconditions.checkNotNull(authorities);
    final Collection<String> roles = Sets.newHashSet();
    authorities.forEach(grantedAuthority -> roles.add(grantedAuthority.getAuthority()));
    return roles;
  }

  @Override
  public Long getCurrentUserId() {
    MetaClass userClass = metaStorage.getMetaClass(SecUserStaticContent.META_CLASS);
    Condition condition = conditionManager.field(userClass.getField(SecUserStaticContent.Field.login)).equal(getCurrentUserLogin());
    Record user = userDataSource.getObj(GetObjectsParams.newBuilder(userClass).setCondition(condition).setLimit(1).build());
    Preconditions.checkNotNull(user);
    return (Long) user.get(userClass.getIdColumn());
  }

  @Override
  public String getCurrentUserLogin() {
    SecurityContext context = SecurityContextHolder.getContext();
    Preconditions.checkNotNull(context);
    Authentication authentication = context.getAuthentication();
    return authentication.getName();
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#addRight(ru.zorb.services.meta.MetaClass, java.lang.String, boolean, boolean, boolean, boolean)
   */
  @Override
  public void addRight(MetaClass metaClass, String role, boolean c, boolean r, boolean u, boolean d) {
    ru.zorb.storage.jooq.tables.DynamicContentSecurity table = ru.zorb.storage.jooq.tables.DynamicContentSecurity.DYNAMIC_CONTENT_SECURITY;
    if (!(c || r || u || d)) {
      dslContext.deleteFrom(table).where(table.DYNAMIC_CONTENT_ID.eq(Long.valueOf(metaClass.getId()))).and(table.ROLE.eq(role)).execute();
    } else {
      dslContext.update(table).set(table.CAN_CREATE, c).set(table.CAN_READ, r).set(table.CAN_UPDATE, u).set(table.CAN_DELETE, d)
          .where(table.DYNAMIC_CONTENT_ID.eq(Long.valueOf(metaClass.getId()))).and(table.ROLE.eq(role)).execute();
    }
    // todo: передалать на частичный инит
    init();
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.security.dynamiccontent.ContentSecurityManager#deleteAllRight(ru.zorb.services.meta.MetaClass)
   */
  @Override
  public void deleteAllRight(MetaClass metaClass) {
    ru.zorb.storage.jooq.tables.DynamicContentSecurity table = ru.zorb.storage.jooq.tables.DynamicContentSecurity.DYNAMIC_CONTENT_SECURITY;
    dslContext.deleteFrom(table).where(table.DYNAMIC_CONTENT_ID.eq(Long.valueOf(metaClass.getId()))).execute();
    // todo: передалать на частичный инит
    init();
  }

  /**
   *
   * Класс пара ID content - Роль
   *
   * @author ashek
   *
   */
  private static class RoleDynamicContentPair {
    /**
     * ID content
     */
    private final String dynamicContentId;
    /**
     * Роль
     */
    private final String role;

    /**
     * @param dynamicContentId ID content
     * @param role Роль
     */
    public RoleDynamicContentPair(String dynamicContentId, String role) {
      this.dynamicContentId = dynamicContentId;
      this.role = role;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      RoleDynamicContentPair that = (RoleDynamicContentPair) o;

      if (!dynamicContentId.equals(that.dynamicContentId))
        return false;
      return role.equals(that.role);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      int result = dynamicContentId.hashCode();
      result = 31 * result + role.hashCode();
      return result;
    }
  }
}
