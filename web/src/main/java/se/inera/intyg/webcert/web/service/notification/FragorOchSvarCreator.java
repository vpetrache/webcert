package se.inera.intyg.webcert.web.service.notification;

import se.inera.intyg.common.support.modules.support.api.notification.FragorOchSvar;

public interface FragorOchSvarCreator {

    FragorOchSvar createFragorOchSvar(String intygsId);

}