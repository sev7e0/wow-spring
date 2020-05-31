package mvc;

import com.sev7e0.wow.framework.context.WApplicationContext;
import com.sev7e0.wow.framework.webmvc.servlet.WDispatcherServlet;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Title:  WDispatcherServletTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-31 23:28
 **/

public class WDispatcherServletTest {
	@Test
	public void verifyPath() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

		WDispatcherServlet servlet = new WDispatcherServlet();
		Method verifyPath = servlet.getClass().getDeclaredMethod("verifyPath", StringBuilder.class, String.class);
		verifyPath.setAccessible(true);
		StringBuilder stringBuilder = new StringBuilder();
		Object[] objects = {stringBuilder, "test/controller"};
		verifyPath.invoke(servlet, objects);
		Assert.assertEquals(stringBuilder.toString(), "/test/controller");

		Object[] objects2 = {stringBuilder, "test/controller"};
		verifyPath.invoke(servlet, objects2);
		Assert.assertEquals(stringBuilder.toString(), "/test/controller/test/controller");
	}

}
