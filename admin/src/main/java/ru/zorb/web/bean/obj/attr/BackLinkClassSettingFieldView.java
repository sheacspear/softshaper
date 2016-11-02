package ru.zorb.web.bean.obj.attr;

public class BackLinkClassSettingFieldView extends LinkClassSettingFieldView {

	private final String backLinkAttr;

	public BackLinkClassSettingFieldView(String linkMetaClass, String backLinkAttr) {
		super(linkMetaClass);
		this.backLinkAttr = backLinkAttr;
	}

	public String getBackLinkAttr() {
		return backLinkAttr;
	}

}
