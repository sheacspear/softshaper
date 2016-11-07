package ru.zorb.services.meta.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;

import java.util.Collection;
import java.util.Map;

/**
 * MetaStorage Created by Sunchise on 10.08.2016.
 */
//@Service
@Component
public class MetaStorageImpl implements MetaStorage {


  /**
   * metaClassByTable
   */
  private Map<String, MetaClass> metaClassByTable = Maps.newHashMap();

	/**
	 * metaClassByCode
	 */
	private Map<String, MetaClass> metaClassByCode = Maps.newHashMap();

	/**
	 * metaClassById
	 */
	private Map<String, MetaClass> metaClassById = Maps.newHashMap();

	/**
	 * metaFieldById
	 */
	private Map<String, MetaField> metaFieldById = Maps.newHashMap();

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#register(java.util.Collection)
	 */
	@Override
	public synchronized void register(Collection<? extends MetaClass> metas) {
		Map<String, MetaClass> metaClassByCode2 = Maps.newHashMap(metaClassByCode);
    Map<String, MetaClass> metaClassByTable2 = Maps.newHashMap(metaClassByTable);
		Map<String, MetaClass> metaClassById2 = Maps.newHashMap(metaClassById);
		Map<String, MetaField> metaFieldById2 = Maps.newHashMap(metaFieldById);
		for (MetaClass meta : metas) {

		  metaClassByTable2.put(meta.getTable(), meta);
			metaClassByCode2.put(meta.getCode(), meta);
			metaClassById2.put(meta.getId(), meta);
			meta.getFields().forEach(metaField -> metaFieldById2.put(metaField.getId(), metaField));
		}
		metaClassByCode = metaClassByCode2;
		metaClassByTable = metaClassByTable2;
		metaClassById = metaClassById2;
		metaFieldById = metaFieldById2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#getMetaClass(java.lang.String)
	 */
	@Override
	public MetaClass getMetaClass(String code) {
		return StringUtils.isEmpty(code) ? null : metaClassByCode.get(code);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#getMetaClassById(java.lang.Long)
	 */
	@Override
	public MetaClass getMetaClassById(String id) {
		return id == null ? null : metaClassById.get(id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#getAllMetaClasses()
	 */
	@Override
	public Collection<MetaClass> getAllMetaClasses() {
		return metaClassByCode.values();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#getMetaField(java.lang.Long)
	 */
	@Override
	public MetaField getMetaField(String id) {
		return metaFieldById.get(id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.zorb.services.meta.MetaStorage#getMetaFields()
	 */
	@Override
	public Collection<MetaField> getMetaFields() {
		return metaFieldById.values();
	}

  /* (non-Javadoc)
   * @see ru.zorb.services.meta.MetaStorage#getMetaClassByTable(java.lang.String)
   */
  @Override
  public MetaClass getMetaClassByTable(String name) {
    return metaClassByTable.get(name);
  }
}
