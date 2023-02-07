package com.kimsy.community_service.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AccountTypeTest {
    @DisplayName("지원되는 역할일 경우 예외가 발생하지 않는다.")
    @ParameterizedTest(name = "입력={0}")
    @ValueSource(strings = {"Realtor", "Lessor", "Lessee", "Guest"})
    void supportedRole(String role) {
        AccountType.validateSupportOrNot(role);
    }

    @DisplayName("지원되지 않는 역할일 경우 예외가 발생한다.")
    @ParameterizedTest(name = "입력={0}")
    @ValueSource(strings = {"REALTOR", "lessor", "LESSEE", "guest", "User"})
    @NullAndEmptySource
    void notSupportedRole(String role) {
        assertThatThrownBy(() -> AccountType.validateSupportOrNot(role))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원되지 않는 역할입니다.");
    }
}