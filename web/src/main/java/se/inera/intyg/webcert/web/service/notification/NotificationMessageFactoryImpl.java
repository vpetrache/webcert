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

package se.inera.intyg.webcert.web.service.notification;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.inera.intyg.common.support.modules.support.api.notification.FragorOchSvar;
import se.inera.intyg.common.support.modules.support.api.notification.HandelseType;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.inera.intyg.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.intyg.webcert.persistence.utkast.model.Utkast;
import se.inera.intyg.webcert.persistence.utkast.repository.UtkastRepository;

@Component
public class NotificationMessageFactoryImpl implements NotificationMessageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationMessageFactoryImpl.class);

    private static final List<HandelseType> USES_FRAGOR_OCH_SVAR = Arrays.asList(HandelseType.FRAGA_FRAN_FK,
            HandelseType.SVAR_FRAN_FK, HandelseType.FRAGA_TILL_FK, HandelseType.FRAGA_FRAN_FK_HANTERAD,
            HandelseType.SVAR_FRAN_FK_HANTERAD, HandelseType.INTYG_MAKULERAT);

    @Autowired
    private FragorOchSvarCreator fragorOchSvarCreator;

    @Autowired
    private UtkastRepository utkastRepository;

    @Override
    public NotificationMessage createNotificationMessage(String intygsId, HandelseType handelse) {

        Utkast utkast = utkastRepository.findOne(intygsId);

        if (utkast == null) {
            LOG.error("Could not retrieve utkast with id {}", intygsId);
            return null;
        }

        return createNotificationMessage(utkast, handelse);
    }
    /*
     * (non-Javadoc)
     *
     * @see se.inera.intyg.webcert.web.service.notification.NotificationMessageFactory#createNotificationMessage(se.inera.intyg.webcert.web.
     * persistence.utkast.model.Utkast, se.inera.intyg.common.support.modules.support.api.notification.HandelseType)
     */
    @Override
    public NotificationMessage createNotificationMessage(Utkast utkast, HandelseType handelse) {

        String intygsId = utkast.getIntygsId();
        String intygsTyp = utkast.getIntygsTyp();

        LocalDateTime handelseTid = LocalDateTime.now();
        String logiskAdress = utkast.getEnhetsId();

        FragorOchSvar fragaSvar = FragorOchSvar.getEmpty();

        // Add a count of questions to the message
        if (USES_FRAGOR_OCH_SVAR.contains(handelse)) {
            fragaSvar = fragorOchSvarCreator.createFragorOchSvar(intygsId);
        }

        String utkastJson = utkast.getModel();

        return new NotificationMessage(intygsId, intygsTyp, handelseTid, handelse, logiskAdress, utkastJson, fragaSvar);
    }

    /*
     * (non-Javadoc)
     *
     * @see se.inera.intyg.webcert.web.service.notification.NotificationMessageFactory#createNotificationMessage(se.inera.intyg.webcert.web.
     * persistence.fragasvar.model.FragaSvar, se.inera.intyg.common.support.modules.support.api.notification.HandelseType)
     */
    @Override
    public NotificationMessage createNotificationMessage(FragaSvar fragaSvar, HandelseType handelse) {
        String intygsId = fragaSvar.getIntygsReferens().getIntygsId();
        return createNotificationMessage(intygsId, handelse);
    }

}
