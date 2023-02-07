package com.kimsy.community_service.auth;

import com.kimsy.community_service.member.domain.AccountType;

public class MockAuthentication extends CustomAuthentication {
    public MockAuthentication() {
        super(47L, AccountType.REALTOR);
    }
}
