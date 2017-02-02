package ru.softshaper.web.admin.view;

/**
 * Хранилище Источник данных для формы
 *
 * @author asheknew
 *
 */
public interface DataSourceFromViewStore {

	/**
	 * @param contentCode
	 * @return
	 */
	DataSourceFromView get(String contentCode);

	/**
	 * @param contentCode
	 * @param dataViewMapper
	 */
	void registerMapper(String contentCode, DataSourceFromView dataViewMapper);
}
