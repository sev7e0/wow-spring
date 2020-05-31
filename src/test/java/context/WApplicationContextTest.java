package context;

import com.sev7e0.wow.framework.context.WApplicationContext;
import org.junit.Test;

/**
 * Title:  WApplicationContextTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-31 22:26
 **/

public class WApplicationContextTest {
	String[] locations = {"classpath:application.properties"};

	@Test
	public void getBeanDefinitionNames(){

		WApplicationContext context = new WApplicationContext(locations);

		String[] beanDefinitionNames = context.getBeanDefinitionNames();

		assert beanDefinitionNames.length > 0;

		for (String name : beanDefinitionNames){
			System.out.println(name);
		}


	}



}
