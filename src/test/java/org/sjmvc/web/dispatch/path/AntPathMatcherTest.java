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

package org.sjmvc.web.dispatch.path;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AntPathMatcher} class.
 * 
 * @author Ignasi Barrera
 * 
 */
public class AntPathMatcherTest
{
	/** The matcher to test. */
	private AntPathMatcher matcher;

	@BeforeMethod
	public void setUp()
	{
		matcher = new AntPathMatcher();
	}

	@Test
	public void testMatches()
	{
		// Match success
		assertTrue(matcher.matches("", ""));
		assertTrue(matcher.matches("/api", "/api"));
		assertTrue(matcher.matches("/api", "/api/"));
		assertTrue(matcher.matches("/api/", "/api"));
		assertTrue(matcher.matches("/api", "/api/test"));
		assertTrue(matcher.matches("/api/*", "/api/test"));
		assertTrue(matcher.matches("/api/**", "/api/test"));
		assertTrue(matcher.matches("/api/*/", "/api/test/"));
		assertTrue(matcher.matches("/api/**/", "/api/test/"));
		assertTrue(matcher.matches("/api/**/test", "/api/test"));
		assertTrue(matcher.matches("/api/sub?ath/test", "/api/subpath/test"));
		assertTrue(matcher.matches("/api/**/anotherpath",
				"/api/test/subpath/anotherpath"));

		// Match failure
		assertFalse(matcher.matches("/api", "/hola"));
		assertFalse(matcher.matches("/api/*/test", "/api/test"));
	}

}