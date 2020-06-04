package mvc;

import com.sev7e0.wow.framework.webmvc.WView;
import com.sev7e0.wow.framework.webmvc.WViewResolver;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Title:  WViewResolverTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 10:20
 **/

public class WViewResolverTest {

	@Test
	public void resolveViewName(){
		File file = new File("src/test/resources/layouts");
		WViewResolver resolver = new WViewResolver(file);
		WView wView = resolver.resolveViewName("index.html", null);
		Assert.assertNotNull(wView);
		WView index = resolver.resolveViewName("index", null);
		Assert.assertNotNull(index);
		WView inde = resolver.resolveViewName("inde", null);
		Assert.assertNull(inde);

	}

}
