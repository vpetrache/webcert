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

package se.inera.intyg.webcert.web.service.intyg.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.intyg.common.integration.hsa.model.Vardenhet;
import se.inera.intyg.common.integration.hsa.model.Vardgivare;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.webcert.common.service.exception.WebCertServiceException;
import se.inera.intyg.webcert.persistence.utkast.model.Utkast;
import se.inera.intyg.webcert.web.service.intyg.converter.IntygServiceConverterImpl.Operation;
import se.inera.intyg.webcert.web.service.user.dto.WebCertUser;

@RunWith(MockitoJUnitRunner.class)
public class IntygServiceConverterTest {

    private IntygServiceConverterImpl converter = new IntygServiceConverterImpl();

    @Mock
    IntygModuleRegistry moduleRegistry;

    @Mock
    ModuleApi moduleApi;

    @Before
    public void setup() throws Exception {
        when(moduleRegistry.getModuleApi(any(String.class))).thenReturn(moduleApi);
        when(moduleApi.getUtlatandeFromJson(anyString())).thenReturn(new Utlatande());
        converter.setModuleRegistry(moduleRegistry);
    }


    @Test
    public void testBuildSendTypeFromUtlatande() throws Exception {

        Utlatande utlatande = createUtlatandeFromJson();

        SendType res = converter.buildSendTypeFromUtlatande(utlatande);

        assertNotNull(res);

        assertNotNull(res.getAvsantTidpunkt());

        assertThat(res.getVardReferensId(), containsString("SEND-123-"));

        assertEquals("123", res.getLakarutlatande().getLakarutlatandeId());
        assertEquals("Test Testorsson", res.getLakarutlatande().getPatient().getFullstandigtNamn());
        assertEquals("19121212-1212", res.getLakarutlatande().getPatient().getPersonId().getExtension());
        assertNotNull(res.getLakarutlatande().getSigneringsTidpunkt());
        //assertEquals("VardgivarId", res.getAdressVard().getHosPersonal().getForskrivarkod());
        assertEquals("En Läkare", res.getAdressVard().getHosPersonal().getFullstandigtNamn());
        assertEquals("Personal HSA-ID", res.getAdressVard().getHosPersonal().getPersonalId().getExtension());
        assertEquals("Kir mott", res.getAdressVard().getHosPersonal().getEnhet().getEnhetsnamn());
        assertEquals("VardenhetY", res.getAdressVard().getHosPersonal().getEnhet().getEnhetsId().getExtension());
        assertEquals("123456789011", res.getAdressVard().getHosPersonal().getEnhet().getArbetsplatskod().getExtension());
        assertEquals("Landstinget Norrland", res.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals("VardgivarId", res.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension());

    }

    @Test
    public void testConcatPatientName() {

        List<String> fNames = Arrays.asList("Adam", "Bertil", "Cesar");
        List<String> mNames = Collections.singletonList("Davidsson");
        String lName = "Eriksson";

        String name = converter.concatPatientName(fNames, mNames, lName);

        assertEquals("Adam Bertil Cesar Davidsson Eriksson", name);
    }

    @Test
    public void testConcatPatientNameWithSomeNamesBlank() {

        List<String> fNames = Arrays.asList("Adam", "", "Bertil");
        List<String> mNames = Collections.singletonList(" ");
        String lName = "Eriksson";

        String name = converter.concatPatientName(fNames, mNames, lName);

        assertEquals("Adam Bertil Eriksson", name);
    }

    @Test
    public void testBuildVardRefId() {

        LocalDateTime ts = LocalDateTime.parse("2014-01-01T12:34:56.123");

        String res = converter.buildVardReferensId(Operation.REVOKE, "ABC123", ts);

        assertNotNull(res);
        assertEquals(res, "REVOKE-ABC123-20140101T123456.123");
    }

    /**
     * Feed the buildUtlatandeFromUtkastModel with invalid JSON, expect WebCertServiceException.
     */
    @Test(expected = WebCertServiceException.class)
    public void testUtlatandBuiltFromInvalidJson() throws IOException {
        Utkast utkast = new Utkast();
        StringBuilder buf = new StringBuilder();
        buf.append("X").append(createUtlatandeJson());
        utkast.setModel(buf.toString());
        when(moduleApi.getUtlatandeFromJson(anyString())).thenThrow(new IOException());
        converter.buildUtlatandeFromUtkastModel(utkast);
    }

    @Test
    public void testBuildHosPersonalFromWebCertUser() {
        final String forskrivarkod = "forskrivarkod";
        final String hsaId = "hsaid";
        final String namn = "namn";
        final String arbetsplatskod = "arbetsplatskod";
        final String epost = "epost";
        final String enhetsId = "enhetsId";
        final String enhetsnamn = "enhetsnamn";
        final String postadress = "postadress";
        final String postnummer = "postnummer";
        final String postort = "postort";
        final String telefonnummer = "telefonnummer";
        final String vardgivarId = "vardgivarId";
        final String vardgivarnamn = "vardgivarnamn";
        Vardenhet valdVardenhet = new Vardenhet();
        valdVardenhet.setArbetsplatskod(arbetsplatskod);
        valdVardenhet.setEpost(epost);
        valdVardenhet.setId(enhetsId);
        valdVardenhet.setNamn(enhetsnamn);
        valdVardenhet.setPostadress(postadress);
        valdVardenhet.setPostnummer(postnummer);
        valdVardenhet.setPostort(postort);
        valdVardenhet.setTelefonnummer(telefonnummer);
        Vardgivare valdVardgivare = new Vardgivare();
        valdVardgivare.setId(vardgivarId);
        valdVardgivare.setNamn(vardgivarnamn);
        WebCertUser user = new WebCertUser();
        user.setForskrivarkod(forskrivarkod);
        user.setHsaId(hsaId);
        user.setNamn(namn);
        user.setValdVardenhet(valdVardenhet);
        user.setValdVardgivare(valdVardgivare);
        HoSPersonal result = converter.buildHosPersonalFromWebCertUser(user);

        assertEquals(forskrivarkod, result.getForskrivarKod());
        assertEquals(hsaId, result.getPersonId());
        assertEquals(namn, result.getFullstandigtNamn());
        assertEquals(arbetsplatskod, result.getVardenhet().getArbetsplatsKod());
        assertEquals(epost, result.getVardenhet().getEpost());
        assertEquals(enhetsId, result.getVardenhet().getEnhetsid());
        assertEquals(enhetsnamn, result.getVardenhet().getEnhetsnamn());
        assertEquals(postadress, result.getVardenhet().getPostadress());
        assertEquals(postnummer, result.getVardenhet().getPostnummer());
        assertEquals(postort, result.getVardenhet().getPostort());
        assertEquals(telefonnummer, result.getVardenhet().getTelefonnummer());
        assertEquals(vardgivarId, result.getVardenhet().getVardgivare().getVardgivarid());
        assertEquals(vardgivarnamn, result.getVardenhet().getVardgivare().getVardgivarnamn());
    }

    private Utlatande createUtlatandeFromJson() throws Exception {
        return new CustomObjectMapper().readValue(
                readClasspathResource("IntygServiceTest/utlatande.json").getFile(), Utlatande.class);
    }

    private String createUtlatandeJson() throws IOException {
        return IOUtils.toString(readClasspathResource("IntygServiceTest/utlatande.json").getInputStream(), "UTF-8");
    }

    private ClassPathResource readClasspathResource(String file) throws IOException {
        return new ClassPathResource(file);
    }

}
