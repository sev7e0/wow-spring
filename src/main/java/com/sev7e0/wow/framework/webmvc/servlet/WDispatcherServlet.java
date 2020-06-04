package com.sev7e0.wow.framework.webmvc.servlet;

import com.sev7e0.wow.framework.annotation.WController;
import com.sev7e0.wow.framework.annotation.WRequestMapping;
import com.sev7e0.wow.framework.beans.WBeanWrapper;
import com.sev7e0.wow.framework.context.WApplicationContext;
import com.sev7e0.wow.framework.webmvc.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:  WDispatcherServlet.java
 * description: mvc启动如口
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-26 23:57
 **/

@Slf4j
public class WDispatcherServlet extends HttpServlet {

	private final static String LOCATION = "defaultConfig";
	private final static String PATH_SEPARATOR = "/";

	private final List<WHandlerMapping> handlerMappings = new ArrayList<>();

	private final Map<WHandlerMapping, WHandlerAdapter> handlerAdapters = new HashMap<>();

	private final List<WViewResolver> viewResolvers = new ArrayList<>();

	private WApplicationContext context;

	/*========================处理请求========================*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

		try {
			doDispatch(req, resp);
		} catch (Exception e) {
			try {
				processDispatchResult(new WModelAndView("500",null), resp);
			} catch (Exception exception) {
				log.error("render 500 page error:{}",exception.getMessage());
			}
			e.printStackTrace();
		}
	}

	private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

		WHandlerMapping handler = getHandler(request);

		if (Objects.isNull(handler)) {
			try {
				processDispatchResult(new WModelAndView("404",null), response);
			} catch (Exception exception) {
				log.error("render 404 page error:{}",exception.getMessage());
			}
			return;
		}
		WHandlerAdapter adapter = getHandlerAdapter(handler);
		WModelAndView modelAndView = adapter.handler(request, response, handler);
		//根据对象视图进行页面渲染
		processDispatchResult(modelAndView, response);

	}

	private void processDispatchResult(WModelAndView modelAndView, HttpServletResponse response) throws Exception {

		if (Objects.isNull(modelAndView)) return;
		if (this.viewResolvers.isEmpty()) return;
		for (WViewResolver resolver : this.viewResolvers) {
			WView view = resolver.resolveViewName(modelAndView.getViewName(), null);
			if (Objects.nonNull(view)) {
				view.render(modelAndView.getModel(), response);
				return;
			}
			log.debug("No corresponding view found：{}", modelAndView.getViewName());
		}
	}

	private WHandlerAdapter getHandlerAdapter(WHandlerMapping mapping) {
		if (this.handlerAdapters.isEmpty()) return null;
		WHandlerAdapter handlerAdapter = this.handlerAdapters.get(mapping);
		if (handlerAdapter.support(mapping)) {
			return handlerAdapter;
		}
		return null;
	}

	/**
	 * 根据url获取对应的handler
	 *
	 * @param request 请求
	 * @return 对应的WHandlerMapping
	 */
	private WHandlerMapping getHandler(HttpServletRequest request) {
		if (this.handlerMappings.isEmpty()) return null;
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		requestURI = requestURI.replaceFirst(contextPath, "").replace("/+", "/");

		for (WHandlerMapping mapping : this.handlerMappings) {

			Matcher matcher = mapping.getPattern().matcher(requestURI);
			if (matcher.matches()) {
				return mapping;
			}
		}
		return null;

	}


	/*========================初始化相关========================*/

	/**
	 * 初始化方法，其主要工作就是将IoC容器初始化和mvc组件初始化
	 *
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
	 * <p>
	 * 针对每一个用户的请求，都会经过一些策略处理，最终才能有结果输出
	 *
	 * @param context
	 */
	private void initStrategies(WApplicationContext context) {
		//每一种都是一种单独的策略
		initMultipartResolver(context);

		initLocalResolver(context);

		initThemeResolver(context);

		//先初始化HandlerMappings
		initHandlerMappings(context);

		//再为每一个Handler初始化Adapters
		initHandlerAdapters(context);

		initHandlerExceptionResolver(context);

		initRequestToViewNAmeTranslator(context);

		initViewResolvers(context);

		initFlashMapManager(context);

	}

	/**
	 * 策略模式的应用，用于对输入url到指定放法的映射
	 *
	 * @param context IoC容器
	 */
	private void initHandlerMappings(WApplicationContext context) {

		//从容器种获取到所有的定义
		String[] names = context.getBeanDefinitionNames();

		try {
			for (String name : names) {
				WBeanWrapper beanWrapper = (WBeanWrapper) context.getBean(name);

				//对没有添加`@WController`注解的不进行映射
				if (!beanWrapper.getWrappedClass().isAnnotationPresent(WController.class)) continue;

				StringBuilder baseUrl = new StringBuilder();

				if (beanWrapper.getWrappedClass().isAnnotationPresent(WRequestMapping.class)) {
					WRequestMapping requestMapping = beanWrapper.getWrappedClass().getAnnotation(WRequestMapping.class);
					verifyPath(baseUrl, requestMapping.value());
				}

				Method[] beanClassFields = beanWrapper.getWrappedClass().getMethods();
				//对内部方法进行判断
				for (Method method : beanClassFields) {
					//跳过没有使用注解的方法
					if (!method.isAnnotationPresent(WRequestMapping.class)) continue;

					WRequestMapping requestMapping = method.getAnnotation(WRequestMapping.class);
					verifyPath(baseUrl, requestMapping.value());
					String regex = baseUrl.toString().replace("\\*", ".*").replace("/+", "/");
					Pattern compile = Pattern.compile(regex);
					this.handlerMappings.add(new WHandlerMapping(beanWrapper.getWrappedInstance(), method, compile));
				}


			}
		} catch (Exception e) {
			log.error("Init handlerMapping error: {}", e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 用于验证生成完整的url路径
	 *
	 * @param sb    StringBuilder
	 * @param value 要拼接的路径
	 */
	private void verifyPath(StringBuilder sb, String value) {
		if (sb.toString().endsWith(PATH_SEPARATOR)) {
			if (value.startsWith(PATH_SEPARATOR)) {
				String replaceFirst = value.replaceFirst(PATH_SEPARATOR, "");
				sb.append(replaceFirst);
			} else {
				sb.append(value);
			}
		} else {
			if (value.startsWith(PATH_SEPARATOR)) {
				sb.append(value);
			} else {
				sb.append(PATH_SEPARATOR).append(value);
			}
		}
	}


	/**
	 * 主要作用是加载视图路径下所有的视图文件，例如html文件。
	 * 将其根据名字映射起来，这样在返回视图时能够根据其名字找到对应的视图进行渲染。
	 *
	 * @param context IoC容器对象
	 */
	private void initViewResolvers(WApplicationContext context) {
		String templateRoot = context.getProperties().getProperty("templateRoot");
		URL resource = this.getClass().getClassLoader().getResource(templateRoot);
		Objects.requireNonNull(resource);
		String fileString = resource.getFile();
		File file = new File(fileString);
		this.viewResolvers.add(new WViewResolver(file));
		for (File template : Objects.requireNonNull(file.listFiles())) {
			//遍历时只将资源文件夹进行解析保存，具体原因参看`WViewResolver.resolveViewName(String)`方法
			if (template.isDirectory()) {
				this.viewResolvers.add(new WViewResolver(template));
			}
		}


	}

	/**
	 * 适配器模式的主要应用，主要是为了解决输入参数都为字符串类型，将其适配
	 * 为方法对应的参数类型。
	 *
	 * @param context 容器对象
	 */
	private void initHandlerAdapters(WApplicationContext context) {

		for (WHandlerMapping handlerMapping : this.handlerMappings) {
			this.handlerAdapters.put(handlerMapping, new WHandlerAdapter());
		}
	}


	private void initFlashMapManager(WApplicationContext context) {

	}

	private void initRequestToViewNAmeTranslator(WApplicationContext context) {

	}

	private void initHandlerExceptionResolver(WApplicationContext context) {

	}

	private void initThemeResolver(WApplicationContext context) {

	}

	private void initLocalResolver(WApplicationContext context) {

	}


	private void initMultipartResolver(WApplicationContext context) {

	}


	@Override
	public void destroy() {
		super.destroy();
	}
}
