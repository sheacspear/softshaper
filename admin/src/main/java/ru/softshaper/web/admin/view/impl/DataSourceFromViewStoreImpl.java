package ru.softshaper.web.admin.view.impl;

import com.google.common.collect.Maps;

import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;

import java.util.Map;

/**
 * Хранилище Источник данных для формы
 */
public class DataSourceFromViewStoreImpl implements DataSourceFromViewStore {

	/**
	 *
	 */
	private Map<String, DataSourceFromView> mappers = Maps.newHashMap();

	/**
	 * Источник данных для формы
	 */
	private DataSourceFromView defaultViewMapper;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.softshaper.web.view.DataSourceFromViewStore#registerMapper(java.lang.String,
	 * ru.softshaper.web.view.DataSourceFromView)
	 */
	@Override
	public synchronized void registerMapper(String contentCode, DataSourceFromView dataViewMapper) {
		Map<String, DataSourceFromView> mappers2 = Maps.newHashMap(mappers);
		mappers2.put(contentCode, dataViewMapper);
		mappers = mappers2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.softshaper.web.view.DataSourceFromViewStore#get(java.lang.String)
	 */
	@Override
	public DataSourceFromView get(String contentCode) {
		DataSourceFromView viewMapper = mappers.get(contentCode);
		return viewMapper == null ? defaultViewMapper : viewMapper;
	}

	/**
	 * @param defaultViewMapper
	 */
	public void setDefaultViewMapper(DataSourceFromView defaultViewMapper) {
		this.defaultViewMapper = defaultViewMapper;
	}
}
