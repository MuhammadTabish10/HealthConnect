package com.healthconnect.apigateway.util;

import com.healthconnect.apigateway.constant.MessageConstants;
import com.healthconnect.apigateway.constant.ResourceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenUtils {

    public List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap(ResourceConstants.REALM_ACCESS);
        log.debug(MessageConstants.REALM_ACCESS_LOG, realmAccess);

        List<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.get(ResourceConstants.ROLES) instanceof List<?>) {
            roles = ((List<?>) realmAccess.get(ResourceConstants.ROLES)).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .map(role -> ResourceConstants.ROLE_PREFIX + role.toUpperCase())
                    .collect(Collectors.toList());
        }

        if (roles.isEmpty()) {
            log.warn(MessageConstants.NO_ROLES_REALM_ACCESS_LOG);
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(ResourceConstants.RESOURCE_ACCESS);

            if (resourceAccess != null) {
                for (Object resource : resourceAccess.values()) {
                    if (resource instanceof Map<?, ?> resourceMap) {
                        Object rolesObj = resourceMap.get(ResourceConstants.ROLES);
                        if (rolesObj instanceof List<?>) {
                            List<String> resourceRoles = ((List<?>) rolesObj).stream()
                                    .filter(role -> role instanceof String)
                                    .map(role -> (String) role)
                                    .map(role -> ResourceConstants.ROLE_PREFIX + role.toUpperCase())
                                    .toList();
                            roles.addAll(resourceRoles);
                        }
                    }
                }
            }
        }

        log.debug(MessageConstants.ROLES_EXTRACTED_LOG, roles);
        return roles.isEmpty() ? Collections.emptyList() : roles;
    }
}
