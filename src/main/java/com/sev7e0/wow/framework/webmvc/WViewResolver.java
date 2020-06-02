package com.sev7e0.wow.framework.webmvc;

import com.sev7e0.wow.framework.Strings;

import java.io.File;
import java.util.Locale;

/**
 * Title:  WViewResolver.java
 * description: 主要用途就是将静态文件转变为动态文件
 * 根据用户传送不同的参数，产生不同的结果
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-30 22:34
 **/

public class WViewResolver {

	private final File templateRootDir;

	private static final String DEFAULT_TEMPLATE_SUFFIX = ".html";


	public WViewResolver(File templateDir) {
		this.templateRootDir = templateDir;
	}


	public WView resolveViewName(String viewName, Locale locale) {

		if (Strings.isEmpty(viewName)) return null;

		String templateName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : viewName + DEFAULT_TEMPLATE_SUFFIX;

		File file = new File((templateRootDir.getPath() + "/" + templateName).replace("/+", "/"));
		return new WView(file);
	}

}
