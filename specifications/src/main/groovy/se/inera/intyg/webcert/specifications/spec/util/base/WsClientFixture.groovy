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

package se.inera.intyg.webcert.specifications.spec.util.base

import java.security.cert.X509Certificate

import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

import org.apache.cxf.configuration.jsse.TLSClientParameters
import org.apache.cxf.configuration.security.FiltersType
import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.frontend.ClientProxyFactoryBean
import org.apache.cxf.jaxws.JaxWsClientFactoryBean
import org.apache.cxf.message.Message
import org.apache.cxf.transport.http.HTTPConduit
import org.w3.wsaddressing10.AttributedURIType

import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum

class WsClientFixture {

	private final static String LOGICAL_ADDRESS = "FKORG"

    String baseUrl = System.getProperty("certificate.baseUrl")

    boolean nyaKontraktet = false

	private CustomObjectMapper jsonMapper = new CustomObjectMapper();
	protected AttributedURIType logicalAddress = new AttributedURIType()

	public WsClientFixture() {
		this(LOGICAL_ADDRESS)
	}

    public WsClientFixture(String address) {
        logicalAddress.setValue(address)
        init()
    }

    public WsClientFixture(String address, String baseUrl) {
        logicalAddress.setValue(address)
        this.baseUrl = baseUrl
        init()
    }

    public void init() {}

	def asJson(def object) {
		StringWriter sw = new StringWriter()
		jsonMapper.writeValue(sw, object)
		return sw.toString()
	}

	def asErrorMessage(String s) {
		throw new Exception("message:<<${s.replace(System.getProperty('line.separator'), ' ')}>>")
	}

	def setEndpoint(def responder, String serviceName, String url = baseUrl + serviceName) {
		if (!url) url = baseUrl + serviceName
		Client client = ClientProxy.getClient(responder)
		client.getRequestContext().put(Message.ENDPOINT_ADDRESS, url)
	}

	def createClient(def responderInterface, String url) {
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean(new JaxWsClientFactoryBean());
		factory.setServiceClass( responderInterface );
		factory.setAddress(url);
		def responder = factory.create();
		if (url.startsWith("https:")) {
			setupSSLCertificates(responder)
		}
		return responder
	}

	def resultAsString(response) {
        String result = null
		if (response) {
            if (nyaKontraktet) {
                switch (response.result.resultCode) {
                    case ResultCodeType.OK:
                        result = response.result.resultCode.toString()
                        break
                    case ResultCodeType.INFO:
                        result = "[${response.result.resultCode.toString()}] - ${response.result.resultText}"
                        break
                    case ResultCodeType.ERROR:
                        result = "[${response.result.errorId.toString()}] - ${response.result.resultText}"
                        break
                }
            } else {
    	        switch (response.result.resultCode) {
    	            case ResultCodeEnum.OK:
    	                result = response.result.resultCode.toString()
                        break
    	            case ResultCodeEnum.INFO:
    	                result = "[${response.result.resultCode.toString()}] - ${response.result.infoText}"
                        break
                    case ResultCodeEnum.ERROR:
    					result = "[${response.result.errorId.toString()}] - ${response.result.errorText}"
                        break
    	        }
            }
		}
		return result
	}

	def setupSSLCertificates(def responder) {
		Client client = ClientProxy.getClient(responder)
		HTTPConduit httpConduit = (HTTPConduit)client.getConduit();
		TLSClientParameters tlsParams = new TLSClientParameters();
		tlsParams.setDisableCNCheck(true);

        TrustManager[] tm = [new TrustAllX509TrustManager()]
        tlsParams.setTrustManagers(tm);

		FiltersType filter = new FiltersType();
		filter.getInclude().add(".*_EXPORT_.*");
		filter.getInclude().add(".*_EXPORT1024_.*");
		filter.getInclude().add(".*_WITH_DES_.*");
        filter.getInclude().add(".*_WITH_AES_.*");
		filter.getInclude().add(".*_WITH_NULL_.*");
		filter.getExclude().add(".*_DH_anon_.*");
		tlsParams.setCipherSuitesFilter(filter);//set all the needed include filters.

		httpConduit.setTlsClientParameters(tlsParams);
	}

    /**
     * This class allow any X509 certificates to be used to authenticate the remote side of a secure socket, including
     * self-signed certificates.
     */
    public class TrustAllX509TrustManager implements X509TrustManager {

        /** Empty array of certificate authority certificates. */
        private static final X509Certificate[] acceptedIssuers = [];

        /**
         * Always trust for client SSL chain peer certificate chain with any authType authentication types.
         *
         * @param chain the peer certificate chain.
         * @param authType the authentication type based on the client certificate.
         */
        public void checkClientTrusted( X509Certificate[] chain, String authType ) {
        }

        /**
         * Always trust for server SSL chain peer certificate chain with any authType exchange algorithm types.
         *
         * @param chain the peer certificate chain.
         * @param authType the key exchange algorithm used.
         */
        public void checkServerTrusted( X509Certificate[] chain, String authType ) {
        }

        /**
         * Return an empty array of certificate authority certificates which are trusted for authenticating peers.
         *
         * @return a empty array of issuer certificates.
         */
        public X509Certificate[] getAcceptedIssuers() {
            return ( acceptedIssuers );
        }
    }

}
