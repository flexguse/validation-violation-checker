/**
 * 
 */
package de.flexguse.util.junit;

/*
 * #%L
 * validation-violation-checker
 * %%
 * Copyright (C) 2015 flexguse
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;

/**
 * Standard JUnit testcases for the {@link ValidationViolationChecker}.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class ValidationViolationCheckerTest {

	/**
	 * Tests if null input is handled correctly and does not cause an exception.
	 */
	@Test
	public void testNullInput() {

	    ValidationViolationChecker.checkExpectedValidationViolations(null, null);
	    ValidationViolationChecker.checkExpectedValidationViolations(
				new HashSet<ConstraintViolation<?>>(), null);
	    ValidationViolationChecker.checkExpectedValidationViolations(null, new ArrayList<String>());

	}

	/**
	 * Tests if different sizes of expected and given violations are handled
	 * correctly.
	 */
	@Test
	public void testDifferentSizes() {

		try {
		    ValidationViolationChecker.checkExpectedValidationViolations(
					new HashSet<ConstraintViolation<?>>(),
					Arrays.asList("error 1", "error 2"));
			fail("assertionError expected");
		} catch (AssertionError e) {

			assertEquals(
					"number of expected validation violations (error 1, error 2) does not match the number of given violations () expected:<2> but was:<0>",
					e.getMessage());
		}

	}

	/**
	 * Tests if the content of expected and given violations is handled
	 * correctly.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testNonCongruentValidationViolations() {

		// expected violations
		List<String> expectedViolations = Arrays.asList("error 1", "error 2");

		// create set containing mocked ConstraintViolations
		Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

		ConstraintViolation<String> mock1 = mock(ConstraintViolation.class);
		when(mock1.getMessageTemplate()).thenReturn("error 1");
		violations.add(mock1);

		ConstraintViolation<String> mock2 = mock(ConstraintViolation.class);
		when(mock2.getMessageTemplate()).thenReturn("error 3");
		violations.add(mock2);

		try {
		    ValidationViolationChecker.checkExpectedValidationViolations(violations,
					expectedViolations);
			fail("assertionError expected");
		} catch (AssertionError e) {

			/*
			 * As the used HashSet is not sorted one of the expected AssertionErrors must be given.
			 */
			try {
				assertEquals(
						"the violations (error 3) where given but not expected: all given violations (error 3, error 1), all expected violations (error 1, error 2)",
						e.getMessage());

			} catch (AssertionError e1) {
				assertEquals(
						"the violations (error 3) where given but not expected: all given violations (error 1, error 3), all expected violations (error 1, error 2)",
						e.getMessage());

			}

		}

	}

	/**
	 * Tests if matching expected and given violations do not cause an assertion
	 * error.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testMatchingExpectations() {

		// expected violations
		List<String> expectedViolations = Arrays.asList("error 1", "error 2");

		// create set containing mocked ConstraintViolations
		Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

		ConstraintViolation<String> mock1 = mock(ConstraintViolation.class);
		when(mock1.getMessageTemplate()).thenReturn("error 1");
		violations.add(mock1);

		ConstraintViolation<String> mock2 = mock(ConstraintViolation.class);
		when(mock2.getMessageTemplate()).thenReturn("error 2");
		violations.add(mock2);

		ValidationViolationChecker.checkExpectedValidationViolations(violations,
				expectedViolations);
	}

}
