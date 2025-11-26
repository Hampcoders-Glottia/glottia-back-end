package com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.iam.interfaces.acl.IamContextFacade;

@Service
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;
    
    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Optional<Long> fetchUserIdByUsername(String username) {
        return iamContextFacade.fetchUserIdByUsername(username) != 0L ? Optional.of(iamContextFacade.fetchUserIdByUsername(username)) : Optional.empty();
    }

    public boolean existsByUsername(String username) {
        return iamContextFacade.existsByUsername(username);
    }

    
}
