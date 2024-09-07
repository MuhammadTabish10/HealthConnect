package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceConstants {

    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";
    public static final String RESOURCE_ACCESS = "resource_access";
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String TOKEN_EXP_KEY = "exp";
    public static final String TOKEN_USER_ID_KEY = "userId";
    public static final String TOKEN_EMAIL_KEY = "email";
    public static final String TOKEN_ROLES_KEY = "roles";
    public static final String TOKEN_ISSUER_KEY = "iss";
    public static final String TOKEN_AZP_KEY = "azp";

}
