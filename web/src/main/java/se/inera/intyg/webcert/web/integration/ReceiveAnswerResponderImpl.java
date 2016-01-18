/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.webcert.web.integration;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.InnehallType;
import se.inera.ifv.insuranceprocess.healthreporting.receivemedicalcertificateanswer.rivtabp20.v1.ReceiveMedicalCertificateAnswerResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.receivemedicalcertificateanswerresponder.v1.AnswerFromFkType;
import se.inera.ifv.insuranceprocess.healthreporting.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.webcert.web.integration.registry.IntegreradeEnheterRegistry;
import se.inera.intyg.webcert.web.integration.validator.QuestionAnswerValidator;
import se.inera.intyg.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.intyg.webcert.persistence.fragasvar.model.Status;
import se.inera.intyg.webcert.web.service.fragasvar.FragaSvarService;
import se.inera.intyg.webcert.web.service.mail.MailNotificationService;
import se.inera.intyg.webcert.web.service.notification.NotificationService;

import java.util.List;


/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class ReceiveAnswerResponderImpl implements ReceiveMedicalCertificateAnswerResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveAnswerResponderImpl.class);

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private FragaSvarService fragaSvarService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IntegreradeEnheterRegistry integreradeEnheterRegistry;

    @Override
    public ReceiveMedicalCertificateAnswerResponseType receiveMedicalCertificateAnswer(
            AttributedURIType logicalAddress, ReceiveMedicalCertificateAnswerType request) {

        ReceiveMedicalCertificateAnswerResponseType response = new ReceiveMedicalCertificateAnswerResponseType();

        // Validate incoming request
        List<String> validationMessages = QuestionAnswerValidator.validate(request);
        if (!validationMessages.isEmpty()) {
            response.setResult(ResultOfCallUtil.failResult(StringUtils.join(validationMessages, ",")));
            return response;
        }

        // Fetch the answer
        AnswerFromFkType answerType = request.getAnswer();

        // Verify there is a valid reference ID
        Long referensId;
        try {
            referensId = Long.parseLong(answerType.getVardReferensId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("No question found with internal ID " + request.getAnswer().getVardReferensId(), e);
        }

        // Set result and send response back to caller
        response.setResult(ResultOfCallUtil.okResult());

        // Notify stakeholders and return the response
        sendNotification(processAnswer(referensId, answerType.getSvar()));
        return response;
    }

    private FragaSvar processAnswer(Long referensId, InnehallType answerContents) {
        long refId = referensId;
        String text = answerContents.getMeddelandeText();
        LocalDateTime ldt = answerContents.getSigneringsTidpunkt();

        FragaSvar fragaSvar = fragaSvarService.processIncomingAnswer(refId, text, ldt);
        return fragaSvar;
    }

    private void sendNotification(FragaSvar fragaSvar) {

        String careUnitId = fragaSvar.getVardperson().getEnhetsId();

        if (integreradeEnheterRegistry.isEnhetIntegrerad(careUnitId)) {
            sendNotificationToQueue(fragaSvar);
        } else {
            sendNotificationByMail(fragaSvar);
        }
    }

    private void sendNotificationToQueue(FragaSvar fragaSvar) {
        if (fragaSvar.getStatus() == Status.CLOSED) {
            notificationService.sendNotificationForAnswerHandled(fragaSvar);
            LOGGER.debug("Notification sent: a closed answer with id '{}' (related to certificate with id '{}') was received from FK.", fragaSvar.getInternReferens(), fragaSvar.getIntygsReferens().getIntygsId());
        } else {
            notificationService.sendNotificationForAnswerRecieved(fragaSvar);
            LOGGER.debug("Notification sent: an answer with id '{}' (related to certificate with id '{}') was received from FK.", fragaSvar.getInternReferens(), fragaSvar.getIntygsReferens().getIntygsId());
        }
    }

    private void sendNotificationByMail(FragaSvar fragaSvar) {
        // Send mail to enhet to inform about new answer
        try {
            mailNotificationService.sendMailForIncomingAnswer(fragaSvar);
        } catch (MailSendException e) {
            Long svarsId = fragaSvar.getInternReferens();
            String intygsId = fragaSvar.getIntygsReferens().getIntygsId();
            String enhetsId = fragaSvar.getVardperson().getEnhetsId();
            String enhetsNamn = fragaSvar.getVardperson().getEnhetsnamn();
            LOGGER.error("Notification mail for answer '" + svarsId
                    + "' concerning certificate '" + intygsId
                    + "' couldn't be sent to " + enhetsId
                    + " (" + enhetsNamn + "): " + e.getMessage());
        }
    }
}
