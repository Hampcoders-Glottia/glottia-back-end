package com.hampcoders.glottia.platform.api.profiles.infrastructure.authorization.sfs.pipeline;

//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import java.io.IOException;

//public class UnauthorizedProfileRequestHandlerEntryPoint implements AuthenticationEntryPoint {
//
//    private static final Logger LOGGER
//            = LoggerFactory.getLogger(UnauthorizedProfileRequestHandlerEntryPoint.class);
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authenticationException) throws IOException, ServletException {
//
//        LOGGER.error("Unauthorized profile request: {}", authenticationException.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request detected");
//    }
//}
