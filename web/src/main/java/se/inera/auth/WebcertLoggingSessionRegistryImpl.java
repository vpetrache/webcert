package se.inera.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

import se.inera.webcert.hsa.model.WebCertUser;
import se.inera.webcert.service.monitoring.MonitoringLogService;
import se.inera.webcert.service.monitoring.MonitoringLogServiceImpl.MonitoringEvent;

/**
 * Implementation of SessioRegistry that performs audit logging of login and logout.
 * 
 * @author npet
 *
 */
public class WebcertLoggingSessionRegistryImpl extends SessionRegistryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebcertLoggingSessionRegistryImpl.class);
    
    @Autowired
    private MonitoringLogService monitoringLog;

    @Override
    public void registerNewSession(String sessionId, Object principal) {

        LOGGER.debug("Attempting to register new session '{}'", sessionId);

        if (principal != null && principal instanceof WebCertUser) {
            WebCertUser user = (WebCertUser) principal;
            monitoringLog.logEvent(MonitoringEvent.USER_LOGIN, "Login user '{}' using scheme '{}'", user.getHsaId(), user.getAuthenticationScheme());
        }

        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {

        LOGGER.debug("Attempting to remove session '{}'", sessionId);

        SessionInformation sessionInformation = getSessionInformation(sessionId);

        if (sessionInformation == null) {
            super.removeSessionInformation(sessionId);
            return;
        }

        Object principal = sessionInformation.getPrincipal();

        if (principal instanceof WebCertUser) {
            WebCertUser user = (WebCertUser) principal;
            if (sessionInformation.isExpired()) {
                monitoringLog.logEvent(MonitoringEvent.USER_LOGOUT, "Session expired for user '{}' using scheme '{}'", user.getHsaId(), user.getAuthenticationScheme());
            } else {
                monitoringLog.logEvent(MonitoringEvent.USER_LOGOUT, "Logout user '{}' using scheme '{}'", user.getHsaId(), user.getAuthenticationScheme());
            }
        }

        super.removeSessionInformation(sessionId);
    }

}
