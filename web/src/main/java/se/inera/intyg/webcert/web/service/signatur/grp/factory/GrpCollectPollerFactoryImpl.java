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

package se.inera.intyg.webcert.web.service.signatur.grp.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import se.inera.intyg.webcert.web.service.signatur.grp.GrpCollectPoller;

/**
 * Created by eriklupander on 2015-08-25.
 *
 * TODO. I'm not fond of this approach of getting hold of prototype-scoped
 * spring beans in a singleton context. Look into method injection as well.
 */
@Component
public class GrpCollectPollerFactoryImpl implements GrpCollectPollerFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public GrpCollectPoller getInstance() {
        return applicationContext.getBean("grpCollectPoller", GrpCollectPoller.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}