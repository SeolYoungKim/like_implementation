package com.kimsy.community_service.auth;

import com.kimsy.community_service.member.domain.AccountType;
import lombok.Getter;

@Getter
public class AuthenticationToken {
    private final String accountType;
    private final Long accountId;

    public AuthenticationToken(final String token) {
        final String[] parsedToken = parseToken(token);
        AccountType.validateSupportOrNot(parsedToken[0]);

        this.accountType = parsedToken[0];
        this.accountId = toLong(parsedToken[1]);
    }

    private String[] parseToken(final String token) {
        final String[] parsedToken = token.split(" ");
        validateTokenInfo(parsedToken);

        return parsedToken;
    }

    private void validateTokenInfo(final String[] tokenInfo) {
        if (tokenInfo.length != 2) {
            throw new IllegalArgumentException("지원하는 토큰 형식이 아닙니다. ex:Realtor 412");
        }
    }

    private Long toLong(final String accountId) {
        try {
            return Long.valueOf(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("accountId는 숫자 형식이어야 합니다.");
        }
    }
}

