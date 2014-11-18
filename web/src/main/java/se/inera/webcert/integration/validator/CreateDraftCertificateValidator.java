package se.inera.webcert.integration.validator;

import se.inera.certificate.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.UtlatandeType;

public interface CreateDraftCertificateValidator {

    public abstract ValidationResult validate(UtlatandeType utlatande);

}
