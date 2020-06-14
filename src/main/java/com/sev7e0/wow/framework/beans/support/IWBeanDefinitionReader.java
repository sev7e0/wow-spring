package com.sev7e0.wow.framework.beans.support;

import com.sev7e0.wow.framework.beans.config.WBeanDefinition;

import java.util.List;
import java.util.Properties;

/**
 * Title:  IWBeanDefinitionReader.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 10:53
 **/

public interface IWBeanDefinitionReader {

	List<WBeanDefinition> loadBeanDefinitions();

	Properties getProperties();
}
