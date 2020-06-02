package com.sev7e0.wow.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * Title:  WView.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-02 22:43
 **/

public class WView {

	private final File viewFile;

	public WView(File viewFile) {
		this.viewFile = viewFile;
	}

	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {

	}
}
