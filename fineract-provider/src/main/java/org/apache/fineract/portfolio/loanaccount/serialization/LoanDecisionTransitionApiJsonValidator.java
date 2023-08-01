/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.serialization;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.loanaccount.api.LoanApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class LoanDecisionTransitionApiJsonValidator {

    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public LoanDecisionTransitionApiJsonValidator(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }
    }

    public void validateApplicationReview(final String json) {

        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Set<String> disbursementParameters = new HashSet<>(
                Arrays.asList(LoanApiConstants.loanId, LoanApiConstants.loanReviewOnDateParameterName, LoanApiConstants.noteParameterName,
                        LoanApiConstants.localeParameterName, LoanApiConstants.dateFormatParameterName));

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, disbursementParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("loanDecisionEngine");

        final JsonElement element = this.fromApiJsonHelper.parse(json);

        final LocalDate loanReviewOnDate = this.fromApiJsonHelper.extractLocalDateNamed(LoanApiConstants.loanReviewOnDateParameterName,
                element);
        baseDataValidator.reset().parameter(LoanApiConstants.loanReviewOnDateParameterName).value(loanReviewOnDate).notNull();

        final String note = this.fromApiJsonHelper.extractStringNamed(LoanApiConstants.noteParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.noteParameterName).value(note).notExceedingLengthOf(1000).notNull();

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateDueDiligence(final String json) {

        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Set<String> disbursementParameters = new HashSet<>(Arrays.asList(LoanApiConstants.loanId,
                LoanApiConstants.loanReviewOnDateParameterName, LoanApiConstants.noteParameterName, LoanApiConstants.localeParameterName,
                LoanApiConstants.dateFormatParameterName, LoanApiConstants.dueDiligenceOnDateParameterName,
                LoanApiConstants.surveyNameParameterName, LoanApiConstants.startDateParameterName, LoanApiConstants.endDateParameterName,
                LoanApiConstants.surveyLocationParameterName, LoanApiConstants.programParameterName, LoanApiConstants.countryParameterName,
                LoanApiConstants.cohortParameterName));

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, disbursementParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("loanDecisionEngine");

        final JsonElement element = this.fromApiJsonHelper.parse(json);

        final LocalDate dueDiligenceOn = this.fromApiJsonHelper.extractLocalDateNamed(LoanApiConstants.dueDiligenceOnDateParameterName,
                element);
        baseDataValidator.reset().parameter(LoanApiConstants.loanReviewOnDateParameterName).value(dueDiligenceOn).notNull();

        final String note = this.fromApiJsonHelper.extractStringNamed(LoanApiConstants.noteParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.noteParameterName).value(note).notExceedingLengthOf(1000).notNull();

        final String surveyName = this.fromApiJsonHelper.extractStringNamed(LoanApiConstants.surveyNameParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.surveyNameParameterName).value(surveyName).notExceedingLengthOf(200).notNull();

        final LocalDate startDate = this.fromApiJsonHelper.extractLocalDateNamed(LoanApiConstants.startDateParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.startDateParameterName).value(startDate).notNull();

        final LocalDate endDate = this.fromApiJsonHelper.extractLocalDateNamed(LoanApiConstants.endDateParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.endDateParameterName).value(endDate).notNull();

        final Long surveyLocation = this.fromApiJsonHelper.extractLongNamed(LoanApiConstants.surveyLocationParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.surveyLocationParameterName).value(surveyLocation).notNull()
                .integerGreaterThanZero();

        final Long cohort = this.fromApiJsonHelper.extractLongNamed(LoanApiConstants.cohortParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.cohortParameterName).value(cohort).notNull().integerGreaterThanZero();

        final Long program = this.fromApiJsonHelper.extractLongNamed(LoanApiConstants.programParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.programParameterName).value(program).notNull().integerGreaterThanZero();

        final Long country = this.fromApiJsonHelper.extractLongNamed(LoanApiConstants.countryParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.countryParameterName).value(country).notNull().integerGreaterThanZero();

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateCollateralReview(final String json) {

        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Set<String> disbursementParameters = new HashSet<>(Arrays.asList(LoanApiConstants.loanId,
                LoanApiConstants.collateralReviewOnDateParameterName, LoanApiConstants.noteParameterName,
                LoanApiConstants.localeParameterName, LoanApiConstants.dateFormatParameterName));

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, disbursementParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("loanDecisionEngine");

        final JsonElement element = this.fromApiJsonHelper.parse(json);

        final LocalDate collateralReviewOn = this.fromApiJsonHelper
                .extractLocalDateNamed(LoanApiConstants.collateralReviewOnDateParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.collateralReviewOnDateParameterName).value(collateralReviewOn).notNull();

        final String note = this.fromApiJsonHelper.extractStringNamed(LoanApiConstants.noteParameterName, element);
        baseDataValidator.reset().parameter(LoanApiConstants.noteParameterName).value(note).notExceedingLengthOf(1000).notNull();

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
}
