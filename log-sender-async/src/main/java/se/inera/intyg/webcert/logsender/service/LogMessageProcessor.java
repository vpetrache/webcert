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

package se.inera.intyg.webcert.logsender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.logmessages.AbstractLogMessage;
import se.inera.intyg.webcert.logsender.client.LogSenderClient;
import se.inera.intyg.webcert.logsender.exception.LoggtjanstExecutionException;
import se.inera.intyg.webcert.logsender.exception.PermanentException;
import se.inera.intyg.webcert.logsender.exception.TemporaryException;
import se.inera.intyg.webcert.logsender.model.LogTypeFactory;
import se.riv.ehr.log.store.storelogresponder.v1.StoreLogResponseType;
import se.riv.ehr.log.store.v1.ResultType;

import javax.xml.ws.WebServiceException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eriklupander on 2015-05-21.
 */
public class LogMessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(LogMessageProcessor.class);

    @Autowired
    private LogSenderClient logSenderClient;

    @Autowired
    private LogTypeFactory logTypeFactory;

    /**
     * Note use of Camel "boxing" of the body.
     *
     * @param groupedLogEntries
     * @throws Exception
     */
    public void process(List<AbstractLogMessage> groupedLogEntries) throws Exception {

        StoreLogResponseType response;
        try {
            response = logSenderClient.sendLogMessage(groupedLogEntries.stream()
                    .map(alm -> logTypeFactory.convert(alm))
                    .collect(Collectors.toList()));

            final ResultType result = response.getResultType();
            final String resultText = result.getResultText();

            switch(result.getResultCode()) {
                case OK:
                    break;
                case ERROR:
                case VALIDATION_ERROR:
                    LOG.warn("Error occured when trying to send log messages '{}'", resultText);
                    throw new PermanentException("Unhandled error: " + resultText);
                case INFO:
                    LOG.warn("Warning occured when trying to send log messages '{}'. Will not requeue.", resultText);
                    break;
                default:
                    throw new TemporaryException(resultText);
            }
        } catch (LoggtjanstExecutionException e) {
            LOG.warn("Call to send log message caused a LoggtjanstExecutionException: {}. Will retry", e.getMessage());
            throw new TemporaryException(e.getMessage());
        } catch (WebServiceException e) {
            LOG.warn("Call to send log message caused an error: {}. Will retry", e.getMessage());
            throw new TemporaryException(e.getMessage());
        }
    }
}
