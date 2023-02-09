package com.kimsy.community_service.auth;

import com.kimsy.community_service.member.domain.AccountType;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthentication implements Authentication {
    private final Long accountId;
    private final AccountType accountType;

    public CustomAuthentication(final Long accountId, final AccountType accountType) {
        this.accountId = accountId;
        this.accountType = accountType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return accountId;
    }

    @Override
    public Object getPrincipal() {
        return accountId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return String.valueOf(accountId);
    }

    public Long getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
