package ru.zorb.web.bean.obj.attr;

import ru.zorb.web.bean.obj.TitleObjectView;
import ru.zorb.web.bean.objlist.ListObjectsView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author ashek
 *
 */
public class VariantSettingFieldView extends LinkClassSettingFieldView {

  /**
  *
  */
  private final List<TitleObjectView> variants;

  /**
   * @param linkMetaClass
   * @param variants
   */
  public VariantSettingFieldView(String linkMetaClass, ListObjectsView variants) {
    super(linkMetaClass);
    this.variants = new ArrayList<>();

    if (variants != null) {
      this.variants.add(new TitleObjectView("null", null, "---"));
      this.variants.addAll(variants.getObjects());
    }
    Collections.sort(this.variants, (o1, o2) -> o1 != null ? o1.getTitle().compareTo(o2.getTitle()) : -1);
  }

  /**
   * @return the variants
   */
  public Collection<TitleObjectView> getVariants() {
    return variants;
  }
}
