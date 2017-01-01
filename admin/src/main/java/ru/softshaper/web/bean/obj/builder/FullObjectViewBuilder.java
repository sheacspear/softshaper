package ru.softshaper.web.bean.obj.builder;

import com.google.common.collect.Lists;

import ru.softshaper.web.bean.obj.FieldObjectView;
import ru.softshaper.web.bean.obj.attr.BackLinkClassSettingFieldView;
import ru.softshaper.web.bean.obj.attr.LinkClassSettingFieldView;
import ru.softshaper.web.bean.obj.attr.VariantSettingFieldView;
import ru.softshaper.web.bean.obj.impl.FullObjectView;
import ru.softshaper.web.bean.objlist.ListObjectsView;
import ru.softshaper.web.view.bean.ViewSetting;

import java.util.Collections;
import java.util.List;

/**
 * FullObjectViewBuilder for create FullObjectView
 */
public class FullObjectViewBuilder {
	/**
	 * contentCode bissness object
	 */
	private final String contentCode;
	/**
	 * key bissness object
	 */
	private String key;
	/**
	 * title bissness object
	 */
	private String title;
	/**
	 * fields by bissness object
	 */
	private List<FieldObjectView<?>> fields = Lists.newArrayList();

	/**
	 * @param key
	 *          bissness object
	 */
	public FullObjectViewBuilder(String contentCode, String key) {
		this.contentCode = contentCode;
		this.key = key;
	}

	/**
	 * @param title
	 *          bissness object
	 * @return this
	 */
	public FullObjectViewBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public <T> FullObjectViewBuilder addField(FieldObjectView<T> field) {
		this.fields.add(field);
		return this;
	}

	/**
	 * add field
	 *
	 * @param fieldView
	 *          view
	 * @param value
	 * @return field
	 */
	public <T> FullObjectViewBuilder addField(ViewSetting fieldView, T value) {
		this.fields.add(new FieldObjectViewBuilder<T>(fieldView.getColumnContent(), fieldView.getTitle(), value)
				.map(fieldView).build());
		return this;
	}

	/**
	 * @param fieldView
	 * @param value
	 * @param linkMetaClass
	 * @param variants
	 * @return
	 */
	@Deprecated
	public <T> FullObjectViewBuilder addFieldVariantLinkClass(ViewSetting fieldView, T value, String linkMetaClass,
                                                            ListObjectsView variants) {
		this.fields.add(new FieldObjectViewBuilder<>(fieldView.getColumnContent(), fieldView.getTitle(), value)
				.map(fieldView).setSettings(new VariantSettingFieldView(linkMetaClass, variants)).build());
		return this;
	}

	/**
	 * @param fieldView
	 * @param value
	 * @param variants
	 * @return
	 */
	public <T> FullObjectViewBuilder addFieldVariant(ViewSetting fieldView, T value, ListObjectsView variants) {
    FieldObjectView<T> builder = new FieldObjectViewBuilder<>(fieldView.getColumnContent(), fieldView.getTitle(), value)
        .map(fieldView)
        .setSettings(new VariantSettingFieldView(null, variants))
        .build();
    this.fields.add(builder);
		return this;
	}

	/**
	 * @param fieldView
	 * @param value
	 * @param linkMetaClass
	 * @return
	 */
	public <T> FullObjectViewBuilder addFieldBackLinkClass(ViewSetting fieldView, T value, String linkMetaClass,
                                                         String backLinkAttr) {
		this.fields.add(new FieldObjectViewBuilder<T>(fieldView.getColumnContent(), fieldView.getTitle(), value)
				.map(fieldView).setSettings(new BackLinkClassSettingFieldView(linkMetaClass, backLinkAttr)).build());
		return this;
	}

	/**
	 * @param fieldView
	 * @param value
	 * @param linkMetaClass
	 * @return
	 */
	public <T> FullObjectViewBuilder addFieldLinkClass(ViewSetting fieldView, T value, String linkMetaClass) {
		this.fields.add(new FieldObjectViewBuilder<T>(fieldView.getColumnContent(), fieldView.getTitle(), value)
				.map(fieldView).setSettings(new LinkClassSettingFieldView(linkMetaClass)).build());
		return this;
	}

	/**
	 * @return object View for bissness object
	 */
	public FullObjectView build() {
		Collections.sort(fields, (o1, o2) -> {
      if ((o1 == null && o2 != null) || (o2 != null && o1.getNumber() < o2.getNumber())) {
        return -1;
      }
      if ((o2 == null && o1 != null) || (o1 != null && o2.getNumber() < o1.getNumber())) {
        return 1;
      }
      return 0;
    });
		return new FullObjectView(contentCode, key, title, fields);
	}
}