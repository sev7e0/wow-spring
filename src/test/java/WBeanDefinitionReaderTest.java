import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import com.sev7e0.wow.framework.beans.support.WBeanDefinitionReader;
import org.junit.Test;

import java.util.List;

/**
 * Title:  WBeanDefinitionReaderTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-28 23:02
 **/

public class WBeanDefinitionReaderTest {
	String[] locations = {"classpath:application.properties"};
	@Test
	public void loadBeanDefinition(){
		WBeanDefinitionReader reader = new WBeanDefinitionReader(locations);
		assert reader.getProperties() != null;
		List<WBeanDefinition> wBeanDefinitions = reader.loadBeanDefinitions();
		assert wBeanDefinitions.size() > 0;
	}



}
