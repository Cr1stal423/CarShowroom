package com.dealership.car.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} that retrieves the current auditor's username
 * from the Spring Security context.
 *
 * This class is annotated with @Component so that it is detected and registered as a bean
 * by Spring's component-scanning mechanism.
 */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * Retrieves the current auditor's username from the Spring Security context.
     *
     * @return an {@code Optional} containing the username of the current auditor if available, or an empty {@code Optional} if not.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
