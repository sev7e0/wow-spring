package mvc;

import org.junit.Assert;
import org.junit.Test;

/**
 * Title:  WHandlerAdapterTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-03 22:50
 **/

public class WHandlerAdapterTest {

	@Test
	public void replaceText(){

		String text = "[aaa]";

		Assert.assertEquals("aaa", text.replaceAll("^\\[*|\\]*$", ""));
		Assert.assertEquals("aaa", text.replaceAll("\\[|\\]", ""));

	}

}
