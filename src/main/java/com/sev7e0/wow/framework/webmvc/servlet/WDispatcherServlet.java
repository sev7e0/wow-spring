package com.sev7e0.wow.framework.webmvc.servlet;

import com.sev7e0.wow.framework.context.WApplicationContext;
import com.sev7e0.wow.framework.webmvc.WHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title:  WDispatcherServlet.java
 * description: mvc启动如口
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-26 23:57
 **/

public class WDispatcherServlet extends HttpServlet {

	private final static String LOCATION = "contextConfigLocation";

	private List<WHandlerMapping> handlerMappings = new ArrayList<>();

	private Map<WHandlerMapping, WHandlerAdapter> handlerAdapters = new HashMap<>();

	private List<WViewResolver> viewResolvers = new ArrayList<>();

	private WApplicationContext context;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	/**
	 * 初始化方法，其主要工作就是将IoC容器初始化和mvc组件初始化
	 * @param config web.xml中配置的初始化参数<init-param></init-param>
	 * @throws ServletException e
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		//初始化容器容器
		context = new WApplicationContext(config.getInitParameter(LOCATION));
		//初始化mvc主要组件
		initStrategies(context);
	}

	/**
	 * 初始化mvc的主要组件，这里参照spring mvc 提供九种组件
	 *
	 * 针对每一个用户的请求，都会经过一些策略处理，最终才能有结果输出
	 * @param context
	 */
	private void initStrategies(WApplicationContext context) {
		//每一种都是一种单独的策略
		initMultipartResolver(context);

		initLocalResolver(context);

		initThemeResolver(context);

		initHandlerMappings(context);

		initHandlerAdaters(context);

		initHandlerExceptionResolver(context);

		initRequestToViewNAmeTranslator(context);

		initViewResolvers(context);

		initFlashMapManager(context);

	}

	private void initViewResolvers(WApplicationContext context) {

	}

	private void initHandlerAdaters(WApplicationContext context) {


	}

	private void initHandlerMappings(WApplicationContext context) {

	}

	private void initFlashMapManager(WApplicationContext context){

	}

	private void initRequestToViewNAmeTranslator(WApplicationContext context){

	}

	private void initHandlerExceptionResolver(WApplicationContext context){

	}

	private void initThemeResolver(WApplicationContext context){

	}

	private void initLocalResolver(WApplicationContext context){

	}


	private void initMultipartResolver(WApplicationContext context){

	}






	@Override
	public void destroy() {
		super.destroy();
	}
}
