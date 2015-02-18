package se.inera.webcert.notificationstub;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.CertificateStatusUpdateForCareType;

public class StubRestApi {

    @Autowired
    private NotificationStore notificationStore;

    @GET
    @Path("/notifieringar")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CertificateStatusUpdateForCareType> notifieringar() {
        return notificationStore.getNotifications();
    }
}
