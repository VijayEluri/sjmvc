/**
 * Copyright (c) 2010 Ignasi Barrera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.sjmvc.binding;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AbstractBinderBinder} class.
 * 
 * @author Ignasi Barrera
 */
public class AbstractBinderTest extends BindingTestSupport<BindPojo>
{
	@BeforeMethod
	public void setUp()
	{
		target = new BindPojo();
		binder = new AbstractBinder<BindPojo, Object>(target, null) {
			@Override
			protected void doBind()
			{
				// Do nothing. Not used in this test.
			}
		};
	}

	@Test
	public void testSetSimpleValue() throws Exception
	{
		// Strings
		checkSetValue("stringProperty", null);
		checkSetValue("stringProperty", "");
		checkSetValue("stringProperty", "test");

		// Numbers
		checkInvalidSetValue("integerProperty", "");
		checkInvalidSetValue("integerProperty", "test");
		checkSetValue("integerProperty", null);
		checkSetValue("integerProperty", "-1");
		checkSetValue("integerProperty", "0");
		checkSetValue("integerProperty", "17");
	}

	@Test
	public void testSetCollectionValue() throws Exception
	{
		// Strings
		checkSetCollectionValues("stringList", new String[] { null });
		checkSetCollectionValues("stringList", "");
		checkSetCollectionValues("stringList", null, null);
		checkSetCollectionValues("stringList", "elem1", "elem2", "elem3");

		// Numbers
		checkInvalidSetCollectionValues("integerList", "");
		checkInvalidSetCollectionValues("integerList", "test");
		checkSetCollectionValues("integerList", new String[] { null });
		checkSetCollectionValues("integerList", null, null);
		checkSetCollectionValues("integerList", "17");
		checkSetCollectionValues("integerList", "-1", "0", "17");
	}

	@Test
	public void testSetArrayValue() throws Exception
	{
		// Strings
		checkSetArrayValues("stringArray", new String[] { null });
		checkSetArrayValues("stringArray", "");
		checkSetArrayValues("stringArray", null, null);
		checkSetArrayValues("stringArray", "elem1", "elem2", "elem3");

		// Numbers
		checkInvalidSetArrayValues("integerArray", "");
		checkInvalidSetArrayValues("integerArray", "test");
		checkSetArrayValues("integerArray", new String[] { null });
		checkSetArrayValues("integerArray", null, null);
		checkSetArrayValues("integerArray", "17");
		checkSetArrayValues("integerArray", "-1", "0", "17");
	}

	@Test
	public void testBindSimpleField() throws Exception
	{
		// Strings
		checkBindSimpleField("stringProperty", new String[] { null });
		checkBindSimpleField("stringProperty", "");
		checkBindSimpleField("stringProperty", "test");
		checkBindSimpleField("stringProperty", null, null);
		checkBindSimpleField("stringProperty", "test", "test2");

		// Numbers
		checkInvalidBindSimpleField("integerProperty", "");
		checkInvalidBindSimpleField("integerProperty", "test");
		checkBindSimpleField("integerProperty", new String[] { null });
		checkBindSimpleField("integerProperty", null, null);
		checkBindSimpleField("integerProperty", "17");
		checkBindSimpleField("integerProperty", "-1", "0", "17");
	}

}
