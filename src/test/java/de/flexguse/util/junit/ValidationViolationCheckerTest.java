/**
 * 
 */
package de.flexguse.util.junit;

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

	private ValidationViolationChecker<String> checker = new ValidationViolationChecker<String>();

	/**
	 * Tests if null input is handled correctly and does not cause an exception.
	 */
	@Test
	public void testNullInput() {

		checker.checkExpectedValidationViolations(null, null);
		checker.checkExpectedValidationViolations(
				new HashSet<ConstraintViolation<String>>(), null);
		checker.checkExpectedValidationViolations(null, new ArrayList<String>());

	}

	/**
	 * Tests if different sizes of expected and given violations are handled
	 * correctly.
	 */
	@Test
	public void testDifferentSizes() {

		try {
			checker.checkExpectedValidationViolations(
					new HashSet<ConstraintViolation<String>>(),
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
		Set<ConstraintViolation<String>> violations = new HashSet<ConstraintViolation<String>>();

		ConstraintViolation<String> mock1 = mock(ConstraintViolation.class);
		when(mock1.getMessageTemplate()).thenReturn("error 1");
		violations.add(mock1);

		ConstraintViolation<String> mock2 = mock(ConstraintViolation.class);
		when(mock2.getMessageTemplate()).thenReturn("error 3");
		violations.add(mock2);

		try {
			checker.checkExpectedValidationViolations(violations,
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
		Set<ConstraintViolation<String>> violations = new HashSet<ConstraintViolation<String>>();

		ConstraintViolation<String> mock1 = mock(ConstraintViolation.class);
		when(mock1.getMessageTemplate()).thenReturn("error 1");
		violations.add(mock1);

		ConstraintViolation<String> mock2 = mock(ConstraintViolation.class);
		when(mock2.getMessageTemplate()).thenReturn("error 2");
		violations.add(mock2);

		checker.checkExpectedValidationViolations(violations,
				expectedViolations);
	}

}
