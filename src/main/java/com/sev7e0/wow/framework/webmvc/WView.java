package com.sev7e0.wow.framework.webmvc;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:  WView.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-02 22:43
 **/

public class WView {

	public final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

	private final File viewFile;

	public WView(File viewFile) {
		this.viewFile = viewFile;
	}

	/**
	 * 用来实现模板渲染，这其中我们可以自己定义占位符相关的
	 *
	 * @param model    返回的对象视图
	 * @param response 响应
	 */
	public void render(Map<String, ?> model, HttpServletResponse response) throws Exception {
		StringBuilder sb = new StringBuilder();

		RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");
		String line;
		while (null != (line = ra.readLine())) {
			line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				String paramName = matcher.group();
				paramName = paramName.replaceAll("￥\\{|\\}", "");
				Object paramValue = model.get(paramName);
				if (null == paramValue) {
					continue;
				}
				line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
				matcher = pattern.matcher(line);
			}
			sb.append(line);
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.getWriter().write(sb.toString());
	}


	//处理特殊字符
	public static String makeStringForRegExp(String str) {
		return str.replace("\\", "\\\\").replace("*", "\\*")
			.replace("+", "\\+").replace("|", "\\|")
			.replace("{", "\\{").replace("}", "\\}")
			.replace("(", "\\(").replace(")", "\\)")
			.replace("^", "\\^").replace("$", "\\$")
			.replace("[", "\\[").replace("]", "\\]")
			.replace("?", "\\?").replace(",", "\\,")
			.replace(".", "\\.").replace("&", "\\&");
	}
}