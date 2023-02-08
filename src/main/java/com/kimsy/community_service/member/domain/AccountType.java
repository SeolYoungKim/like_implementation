package com.kimsy.community_service.member.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public enum AccountType {
    REALTOR("Realtor", "공인중개사"),
    LESSOR("Lessor", "임대인"),
    LESSEE("Lessee", "임차인"),
    ;

    private static final Map<String, AccountType> TOKEN_VALUE_ACCOUNT_TYPE = Arrays.stream(values())
            .collect(Collectors.toMap(
                    accountType -> accountType.tokenValue,
                    accountType -> accountType));

    public static AccountType from(final String tokenValue) {
        if (!StringUtils.hasText(tokenValue) || !TOKEN_VALUE_ACCOUNT_TYPE.containsKey(tokenValue)) {
            throw new IllegalArgumentException("지원되지 않는 역할입니다.");
        }

        return TOKEN_VALUE_ACCOUNT_TYPE.get(tokenValue);
    }

    private final String tokenValue;
    private final String korAccountType;

    AccountType(final String tokenValue, final String korAccountType) {
        this.tokenValue = tokenValue;
        this.korAccountType = korAccountType;
    }
}
