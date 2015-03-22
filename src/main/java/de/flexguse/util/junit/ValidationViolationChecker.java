/**
 * 
 */
package de.flexguse.util.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

/**
 * The ValidationViolationChecker is a little tool which helps to check Bean
 * Validation errors in JUnit tests.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class ValidationViolationChecker<T> {

	private static final String LIST_DELIMITER = ", ";

	/**
	 * This method checks if the validation violations matches the expected
	 * violations.
	 * 
	 * @param violations
	 * @param expectedValidationViolations
	 */
	public void checkExpectedValidationViolations(
			Set<ConstraintViolation<T>> violations,
			List<String> expectedValidationViolations) {

		if (violations != null && expectedValidationViolations != null) {

			List<String> givenViolations = new ArrayList<String>();
			for (ConstraintViolation<T> violation : violations) {
				givenViolations.add(violation.getMessageTemplate());
			}

			/*
			 * check if number of expected violations matches the given
			 * violations
			 */
			Assert.assertEquals(
					String.format(
							"number of expected validation violations (%s) does not match the number of given violations (%s)",
							StringUtils.join(expectedValidationViolations,
									LIST_DELIMITER), StringUtils.join(
									givenViolations, LIST_DELIMITER)),
					expectedValidationViolations.size(), violations.size());

			/*
			 * check if the set of given violations matches the expected
			 * violations
			 */
			boolean listsAreCongruent = true;

			List<String> givenButNotExpected = new ArrayList<String>();

			for (String givenViolation : givenViolations) {

				if (!expectedValidationViolations.contains(givenViolation)) {
					listsAreCongruent = false;
					givenButNotExpected.add(givenViolation);
				}
			}

			Assert.assertTrue(
					String.format(
							"the violations (%s) where given but not expected: all given violations (%s), all expected violations (%s)",
							StringUtils.join(givenButNotExpected,
									LIST_DELIMITER), StringUtils.join(
									givenViolations, LIST_DELIMITER),
							StringUtils.join(expectedValidationViolations,
									LIST_DELIMITER)), listsAreCongruent);

		}

	}
}
