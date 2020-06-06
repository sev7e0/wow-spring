import com.sev7e0.wow.framework.utils.Strings;
import org.junit.Assert;
import org.junit.Test;

/**
 * Title:  StringsTest.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-02 22:53
 **/

public class StringsTest {

	@Test
	public void stringIsOrNoEmpty(){

		String empty = "";

		String nullString = null;

		String noEmpty = "noEmpty";

		Assert.assertTrue(Strings.isEmpty(empty));
		Assert.assertTrue(Strings.isEmpty(nullString));
		Assert.assertFalse(Strings.isEmpty(noEmpty));
		Assert.assertTrue(Strings.noEmpty(noEmpty));
		Assert.assertFalse(Strings.noEmpty(empty));


	}


}
