package com.sev7e0.wow.framework.beans.support;

import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Title:  WBeanDefinitionReader.java
 * description: 本例中主要使用{@link WBeanDefinitionReader}实现对配置文件进行读取，
 * 将读取到的扫描路径进行扫描，加载信息封装成{@link com.sev7e0.wow.framework.beans.config.WBeanDefinition}
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 23:38
 **/

@Slf4j
public class WBeanDefinitionReader implements IWBeanDefinitionReader{

	private final Properties properties = new Properties();

	private final List<String> registryBeanClasses = new ArrayList<>();

	/**
	 * 提供一个参数为数组的构造方法，不过当前仅支持一种类型的配置文件，
	 * 后边可以根据需求拓展出不同类型，多个配置文件一起的实现。
	 *
	 * @param locations 文件位置数组，当前默认取第一个
	 */
	public WBeanDefinitionReader(String... locations) {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""))) {
			properties.load(inputStream);
		} catch (IOException e) {
			log.error("read config file error: {}", e.getMessage());
			e.printStackTrace();
		}
		scanPackage(properties.getProperty("scanPackage"));
	}


	/**
	 * 扫描指定路径下的文件信息，如果当前为文件夹，那么就将其名字拼接上
	 * 继续下下一层遍历。而后将取到的文件名字进行替换、截取组成全类名。
	 *
	 * @param scan 要扫描的路径，当前不支持多个路径
	 */
	private void scanPackage(String scan) {
		Objects.requireNonNull(scan);
		String scanPath = scan.replace(".", "/");
		URL resource = this.getClass().getClassLoader().getResource(scanPath);
		Objects.requireNonNull(resource);
		File classFile = new File(resource.getFile());
		File[] files = classFile.listFiles();
		Objects.requireNonNull(files);
		for (File file : files) {
			if (file.isDirectory()) {
				scanPackage(scanPath + "/" + file.getName());
			} else if (file.getName().endsWith(".class")) {
				String className = scan + "." + file.getName().replace(".class", "");
				log.debug("Scan new class name: {}", className);
				registryBeanClasses.add(className.replace("/", "."));
			}
		}
	}

	/**
	 * 获取配置文件读取到的信息
	 *
	 * @return 配置文件
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * 通过反射获取类的相关信息，包括类名、父接口名等进行封装
	 *
	 * @return WBeanDefinition列表，保存里Bean类的相关信息
	 */
	public List<WBeanDefinition> loadBeanDefinitions() {
		ArrayList<WBeanDefinition> beanDefinitions = new ArrayList<>();
		for (String className : registryBeanClasses) {
			try {
				//通过全类名进行反射获取到类对象，此后根据此类能够获取到接口名字等
				Class<?> beanClass = Class.forName(className);
				if (beanClass.isInterface()) continue;
				beanDefinitions.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), className));
				//对其实现的接口进行统一处理，当前没有考虑到统一接口多个实现的问题，例如在spring中可以根据不同的
				//名字进行注入，当前只根据类型。
				for (Class<?> interfaceName : beanClass.getInterfaces()) {
					beanDefinitions.add(doCreateBeanDefinition(toLowerFirstCase(interfaceName.getSimpleName()), className));
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return beanDefinitions;
	}

	/**
	 * 创建{@link WBeanDefinition}对象，根据提供的相关名字进行填充，不过当前还没有读取`lazyInit`属性值，
	 * 无法接收设置
	 *
	 * @param factoryBeanName 在IoC容器中的名字
	 * @param beanClassName   bean类的全类名
	 * @return WBeanDefinition对象
	 */
	private WBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
		WBeanDefinition beanDefinition = new WBeanDefinition();
		beanDefinition.setBeanFactoryName(factoryBeanName);
		beanDefinition.setBeanClassName(beanClassName);
		//TODO 需要处理设置LazyInit属性
		beanDefinition.setLazyInit(false);
		return beanDefinition;
	}

	/**
	 * 根据类名将第一个字母转化为小写，BeanName -> beanName
	 *
	 * @param simpleName 类名
	 * @return 首字母小写类名
	 */
	private String toLowerFirstCase(String simpleName) {
		char[] chars = simpleName.toCharArray();
		chars[0] += 32;
		return String.valueOf(chars);
	}

}
