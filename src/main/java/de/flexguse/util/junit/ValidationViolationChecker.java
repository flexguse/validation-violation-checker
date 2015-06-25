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
public class ValidationViolationChecker {

    private static final String LIST_DELIMITER = ", ";

    /**
     * This method checks if the validation violations matches the expected
     * violations.
     * 
     * @param violations
     * @param expectedValidationViolations
     */
    public static void checkExpectedValidationViolations(
            Set<ConstraintViolation<?>> violations,
            List<String> expectedValidationViolations) {

        if (violations != null && expectedValidationViolations != null) {

            List<String> givenViolations = new ArrayList<String>();
            for (ConstraintViolation<?> violation : violations) {
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
