package ru.softshaper.services.meta.impl.loader;

import com.google.common.collect.Maps;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.softshaper.bean.meta.MetaClassMutableBean;
import ru.softshaper.bean.meta.MetaFieldMutableBean;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.*;
import ru.softshaper.storage.jooq.tables.DynamicContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Sunchise on 10.08.2016.
 */
public class DynamicContentLoader implements MetaLoader {
	private static final Logger log = LoggerFactory.getLogger(DynamicContentLoader.class);
	/**
	 * DSLContext
	 */
	private final DSLContext dslContext;

	/**
	 * DataSource<Record>
	 */
	private final ContentDataSource<Record> dataSource;

	/**
	 * @param dslContext
	 * @param dataSource
	 */
	public DynamicContentLoader(DSLContext dslContext, ContentDataSource<Record> dataSource) {
		this.dslContext = dslContext;
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.softshaper.services.meta.MetaLoader#loadClasses()
	 */
	@Override
	public Map<MetaClassMutable, ContentDataSource<?>> loadClasses() {
		Map<MetaClassMutable, ContentDataSource<?>> resultMap = Maps.newHashMap();
		Collection<MetaClassMutable> result = new ArrayList<>();
		DynamicContent dcTable = DynamicContent.DYNAMIC_CONTENT;
		Result<Record> records = dslContext.select().from(dcTable).where(dcTable.FIXED.equal(false)).fetch();
		records.forEach(dynCont -> {
			final MetaClassMutable clazz = convert(dynCont);
			result.add(clazz);
			resultMap.put(clazz, dataSource);
		});
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.softshaper.services.meta.MetaLoader#loadFields()
	 */
	@Override
	public Collection<MetaFieldMutable> loadFields(Map<String, MetaClassMutable> clazzCode,
			Map<String, MetaClassMutable> clazzId) {
		DynamicContent dcTable = DynamicContent.DYNAMIC_CONTENT;
		ru.softshaper.storage.jooq.tables.DynamicField dfTable = ru.softshaper.storage.jooq.tables.DynamicField.DYNAMIC_FIELD;
		Result<Record> records = dslContext.select().from(dfTable).innerJoin(dcTable)
				.on(dcTable.ID.equal(dfTable.DYNAMIC_CONTENT_ID)).where(dcTable.FIXED.equal(false)).fetch();
		if (records == null) {
			return Collections.emptySet();
		}
		Collection<MetaFieldMutable> result = new ArrayList<>();
		Map<Long, MetaField> fieldById = Maps.newHashMap();
		for (Record field : records) {
			// Линкованный класс
			MetaClass linkedMetaClass = clazzId
					.get(String.valueOf(field.get(dfTable.LINK_TO_DYNAMIC_CONTENT, Long.class)));

			// Preconditions.checkNotNull(linkedMetaClass);
			MetaField backReferenceField = null;
			if (field.get(dfTable.LINK_TO_DYNAMIC_CONTENT) != null) {
				if (linkedMetaClass == null) {
					log.error("class not found for ID"
							+ String.valueOf(field.get(dfTable.LINK_TO_DYNAMIC_CONTENT, Long.class)));
					continue;
				}
				// Линкованный поле
				Collection<? extends MetaField> fields = linkedMetaClass.getFields();
				if (fields != null) {
					backReferenceField = fields.stream().filter(o -> {
						Long brfId = field.get(dfTable.BACK_REFERENCE_FIELD);
						return brfId != null && o.getId().equals(brfId.toString());
					}).findAny().orElse(null);
				}
			}
			MetaClassMutable metaClassById = clazzId.get(field.get(dfTable.DYNAMIC_CONTENT_ID).toString());
			MetaFieldMutableBean dynamicFieldBean = new MetaFieldMutableBean(field.get(dfTable.ID).toString(),
					metaClassById, field.get(dfTable.NAME), field.get(dfTable.CODE), field.get(dfTable.COLUMN_FIELD),
					FieldType.getFieldType(field.get(dfTable.TYPE1)), linkedMetaClass, backReferenceField, null);
			result.add(dynamicFieldBean);
			fieldById.put(Long.valueOf(dynamicFieldBean.getId()), dynamicFieldBean);
		}
		return result;
	}

	/**
	 * сщтмуке Record -> MetaClass
	 *
	 * @param dynCont
	 *            Record
	 * @return MetaClass
	 */
	protected MetaClassMutable convert(Record dynCont) {
		DynamicContent dcTable = DynamicContent.DYNAMIC_CONTENT;
		String id = dynCont.get(dcTable.ID).toString();
		String code = dynCont.get(dcTable.CODE);
		String name = dynCont.get(dcTable.NAME);
		String table = dynCont.get(dcTable.TABLE_CONTENT);
		boolean checkSecurity = dynCont.get(dcTable.CHECKSECURITY);
		boolean checkObjectSecurity = dynCont.get(dcTable.CHECKOBJECTSECURITY);
		return new MetaClassMutableBean(id, code, name, table, null, checkSecurity, checkObjectSecurity);
	}
}
