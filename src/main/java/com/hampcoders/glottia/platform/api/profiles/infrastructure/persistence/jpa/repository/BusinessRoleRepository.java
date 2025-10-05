package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for BusinessRole entity
 * Repository for Business Role entity operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Repository
public interface BusinessRoleRepository extends JpaRepository<BusinessRole, Long> {
    
    /**
     * Find business role by role enum
     * @param role the business role enum to search for
     * @return Optional<BusinessRole> containing the business role if found
     */
    Optional<BusinessRole> findByRole(BusinessRoles role);
    
    /**
     * Check if business role exists by role enum
     * @param role the business role enum to check
     * @return true if business role exists, false otherwise
     */
    boolean existsByRole(BusinessRoles role);

}