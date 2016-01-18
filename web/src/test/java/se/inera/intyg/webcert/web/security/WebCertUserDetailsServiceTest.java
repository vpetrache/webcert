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

package se.inera.intyg.webcert.web.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.webcert.web.auth.common.AuthConstants.SPRING_SECURITY_SAVED_REQUEST_KEY;

import org.apache.cxf.staxutils.StaxUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.NameID;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import se.inera.intyg.webcert.integration.hsa.model.Vardenhet;
import se.inera.intyg.webcert.integration.hsa.model.Vardgivare;
import se.inera.intyg.webcert.integration.hsa.services.HsaOrganizationsService;
import se.inera.intyg.webcert.integration.hsa.services.HsaPersonService;
import se.inera.intyg.webcert.web.auth.authorities.AuthoritiesConstants;
import se.inera.intyg.webcert.web.auth.authorities.AuthoritiesResolver;
import se.inera.intyg.webcert.web.auth.authorities.Privilege;
import se.inera.intyg.webcert.web.auth.authorities.Role;
import se.inera.intyg.webcert.web.auth.bootstrap.AuthoritiesConfigurationTestSetup;
import se.inera.intyg.webcert.web.auth.exceptions.HsaServiceException;
import se.inera.intyg.webcert.web.auth.exceptions.MissingMedarbetaruppdragException;
import se.inera.intyg.webcert.web.service.feature.WebcertFeatureService;
import se.inera.intyg.webcert.web.service.monitoring.MonitoringLogService;
import se.inera.intyg.webcert.web.service.user.dto.WebCertUser;
import se.riv.infrastructure.directory.v1.PaTitleType;
import se.riv.infrastructure.directory.v1.PersonInformationType;

import javax.xml.transform.stream.StreamSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class WebCertUserDetailsServiceTest extends AuthoritiesConfigurationTestSetup {

    private static final String PERSONAL_HSAID = "TST5565594230-106J";

    private static final String VARDGIVARE_HSAID = "IFV1239877878-0001";
    private static final String ENHET_HSAID_1 = "IFV1239877878-103H";
    private static final String ENHET_HSAID_2 = "IFV1239877878-103P";

    private static final String TITLE_HEAD_DOCTOR = "Överläkare";

    @InjectMocks
    private WebCertUserDetailsService userDetailsService = new WebCertUserDetailsService();

    @Mock
    private HsaOrganizationsService hsaOrganizationsService;

    @Mock
    private HsaPersonService hsaPersonService;

    @Mock
    private WebcertFeatureService webcertFeatureService;

    @Mock
    private MonitoringLogService monitoringLogService;

    @Mock
    private AuthoritiesResolver authoritiesResolver;


    private Vardgivare vardgivare;
    private WebCertUserOrigin webCertUserOrigin;


    @BeforeClass
    public static void bootstrapOpenSaml() throws Exception {
        DefaultBootstrap.bootstrap();
    }

    @Before
    public void setup() {
        // Setup a servlet request
        MockHttpServletRequest request = mockHttpServletRequest("/any/path");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(hsaPersonService.getHsaPersonInfo(anyString())).thenReturn(Collections.emptyList());
        AUTHORITIES_RESOLVER.setHsaPersonService(hsaPersonService);
        userDetailsService.setAuthoritiesResolver(AUTHORITIES_RESOLVER);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleLakare() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleTandLakare() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-tandlakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_TANDLAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_TANDLAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasMultipleTitles() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-multiple-titles.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserIsAtLakare() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-at-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserIsAtLakareButWithoutLicense() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-at-lakare-utan-legitimation.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilgesWhenUserIsDoctorButHasNotYetASwedishLicense() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-lakare-with-titleCode-and-groupPrescriptionCode.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }


    @Test
    public void assertRoleAndPrivilgesWhenTitleCodeAndGroupPrescriptionCodeDoesNotMatch() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-lakare-with-titleCode-and-bad-groupPrescriptionCode.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertTrue("0000000".equals(webCertUser.getForskrivarkod()));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleLakareWhenUserHasMultipleTitleCodes() throws Exception {
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-multiple-title-codes.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleVardadministratorWhenUserIsNotADoctor() throws Exception {
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-no-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_ADMIN));
        assertUserPrivileges(AuthoritiesConstants.ROLE_ADMIN, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleDoctorAndUsesDjupintegrationsLink() throws Exception {
        // given
        String requestURI = "/visa/intyg/789YAU453999KL2JK/alternatePatientSSn=191212121212&responsibleHospName=ÅsaAndersson";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertEquals(webCertUser.getOrigin(), WebCertUserOriginType.DJUPINTEGRATION.name());
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleDoctorAndUsesUthoppsLink() throws Exception {
        // given
        String requestURI = "/webcert/web/user/certificate/789YAU453999KL2JK/questions";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertEquals(webCertUser.getOrigin(), (WebCertUserOriginType.UTHOPP.name()));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleTandlakareAndUsesDjupintegrationsLink() throws Exception {
        // given
        String requestURI = "/visa/intyg/789YAU453999KL2JK/alternatePatientSSn=191212121212&responsibleHospName=ÅsaAndersson";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-tandlakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_TANDLAKARE));
        assertEquals(webCertUser.getOrigin(), WebCertUserOriginType.DJUPINTEGRATION.name());
        assertUserPrivileges(AuthoritiesConstants.ROLE_TANDLAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserHasTitleTandlakareAndUsesUthoppsLink() throws Exception {
        // given
        String requestURI = "/webcert/web/user/certificate/789YAU453999KL2JK/questions";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-tandlakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_TANDLAKARE));
        assertEquals(webCertUser.getOrigin(), WebCertUserOriginType.UTHOPP.name());
        assertUserPrivileges(AuthoritiesConstants.ROLE_TANDLAKARE, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserIsNotDoctorAndUsesDjupintegrationsLink() throws Exception {
        // given
        String requestURI = "/visa/intyg/789YAU453999KL2JK/alternatePatientSSn=191212121212&responsibleHospName=ÅsaAndersson";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-no-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_ADMIN));
        assertEquals(webCertUser.getOrigin(), WebCertUserOriginType.DJUPINTEGRATION.name());
        assertUserPrivileges(AuthoritiesConstants.ROLE_ADMIN, webCertUser);
    }

    @Test
    public void assertRoleAndPrivilegesWhenUserIsNotDoctorAndUsesUthoppsLink() throws Exception {
        // given
        String requestURI = "/webcert/web/user/certificate/789YAU453999KL2JK/questions";
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest(requestURI)));

        SAMLCredential samlCredential = createSamlCredential("saml-assertion-no-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_ADMIN));
        assertEquals(webCertUser.getOrigin(), WebCertUserOriginType.UTHOPP.name());
        assertUserPrivileges(AuthoritiesConstants.ROLE_ADMIN, webCertUser);
    }

    @Test(expected = MissingMedarbetaruppdragException.class)
    public void testMissingMedarbetaruppdrag() throws Exception {
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-no-lakare.xml");
        userDetailsService.loadUserBySAML(samlCredential);
    }

    @Test(expected = MissingMedarbetaruppdragException.class)
    public void testMissingSelectedUnit() throws Exception {
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-without-enhet.xml");
        userDetailsService.loadUserBySAML(samlCredential);
    }

    @Test
    public void testNoGivenName() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-no-givenname.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertEquals("Gran", webCertUser.getNamn());
    }

    @Test
    public void testPopulatingWebCertUser() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();
        setupCallToGetHsaPersonInfo();
        setupCallToWebcertFeatureService();

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertEquals(PERSONAL_HSAID, webCertUser.getHsaId());
        assertEquals("Markus Gran", webCertUser.getNamn());
        assertEquals(1, webCertUser.getVardgivare().size());
        assertEquals(VARDGIVARE_HSAID, webCertUser.getVardgivare().get(0).getId());
        assertEquals(vardgivare, webCertUser.getVardgivare().get(0));
        assertEquals(vardgivare, webCertUser.getValdVardgivare());
        assertNotNull(webCertUser.getValdVardenhet());
        assertEquals(ENHET_HSAID_1, webCertUser.getValdVardenhet().getId());
        assertEquals(3, webCertUser.getSpecialiseringar().size());
        assertEquals(2, webCertUser.getLegitimeradeYrkesgrupper().size());
        assertEquals(TITLE_HEAD_DOCTOR, webCertUser.getTitel());
        assertFalse(webCertUser.getFeatures().isEmpty());

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);

        verify(hsaOrganizationsService).getAuthorizedEnheterForHosPerson(PERSONAL_HSAID);
        verify(hsaPersonService, atLeastOnce()).getHsaPersonInfo(PERSONAL_HSAID);
        verify(webcertFeatureService).getActiveFeatures();
    }

    @Test
    public void testPopulatingWebCertUserWithTwoUserTypes() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");
        setupCallToAuthorizedEnheterForHosPerson();

        PersonInformationType userType1 = buildPersonInformationType(PERSONAL_HSAID, "Titel1",
                Arrays.asList("Kirurgi", "Öron-, näs- och halssjukdomar"), Collections.singletonList("Läkare"));
        PersonInformationType userType2 = buildPersonInformationType(PERSONAL_HSAID, "Titel2", Arrays.asList("Kirurgi", "Reumatologi"),
                Collections.singletonList("Psykoterapeut"));
        List<PersonInformationType> userTypes = Arrays.asList(userType1, userType2);

        Role expected = AUTHORITIES_RESOLVER.getRole("LAKARE");

        // when
        when(hsaPersonService.getHsaPersonInfo(anyString())).thenReturn(userTypes);

        // then
        WebCertUser webCertUser = (WebCertUser) userDetailsService.loadUserBySAML(samlCredential);

        assertEquals(PERSONAL_HSAID, webCertUser.getHsaId());
        assertEquals("Markus Gran", webCertUser.getNamn());

        assertEquals(3, webCertUser.getSpecialiseringar().size());
        assertEquals(2, webCertUser.getLegitimeradeYrkesgrupper().size());

        assertEquals("Titel1, Titel2", webCertUser.getTitel());

        assertTrue(webCertUser.getRoles().containsKey(AuthoritiesConstants.ROLE_LAKARE));
        assertUserPrivileges(AuthoritiesConstants.ROLE_LAKARE, webCertUser);

        verify(hsaOrganizationsService).getAuthorizedEnheterForHosPerson(PERSONAL_HSAID);
        verify(hsaPersonService, atLeastOnce()).getHsaPersonInfo(PERSONAL_HSAID);
    }

    @Test(expected = HsaServiceException.class)
    public void unexpectedExceptionWhenProcessingData() throws Exception {
        // given
        SAMLCredential samlCredential = createSamlCredential("saml-assertion-with-title-lakare.xml");

        // when
        when(hsaOrganizationsService.getAuthorizedEnheterForHosPerson(anyString())).thenThrow(new NullPointerException());

        // then
        userDetailsService.loadUserBySAML(samlCredential);

        // fail the test if we come to this point
        fail("Expected exception");
    }


    // ~ Private assertion methods
    // =====================================================================================

    private void assertUserPrivileges(String roleName, WebCertUser user) {
        Role role = AUTHORITIES_RESOLVER.getRole(roleName);
        List<Privilege> expected = role.getPrivileges()
                .stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .collect(Collectors.toList());

        Map<String, Privilege> map = user.getAuthorities();
        List<Privilege> actual = map.entrySet()
                .stream()
                .sorted((p1, p2) -> p1.getValue().getName().compareTo(p2.getValue().getName()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());

        String e = expected.toString().replaceAll("\\s","");
        String a = actual.toString().replaceAll("\\s","");
        assertEquals(e, a);
    }


    // ~ Private setup methods
    // =====================================================================================

    private PersonInformationType buildPersonInformationType(String hsaId, String title, List<String> specialities, List<String> titles) {

        PersonInformationType type = new PersonInformationType();
        type.setPersonHsaId(hsaId);

        if (title != null) {
            type.setTitle(title);
        }

        if ((titles != null) && (titles.size() > 0)) {
            for (String t : titles) {
                PaTitleType paTitle = new PaTitleType();
                paTitle.setPaTitleName(t);
                type.getPaTitle().add(paTitle);
            }
        }

        if ((specialities != null) && (specialities.size() > 0)) {
            type.getSpecialityName().addAll(specialities);
        }

        return type;
    }

    private SAMLCredential createSamlCredential(String filename) throws Exception {
        Document doc = StaxUtils.read(new StreamSource(new ClassPathResource(
                "WebCertUserDetailsServiceTest/" + filename).getInputStream()));
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(Assertion.DEFAULT_ELEMENT_NAME);

        Assertion assertion = (Assertion) unmarshaller.unmarshall(doc.getDocumentElement());
        NameID nameId = assertion.getSubject().getNameID();
        return new SAMLCredential(nameId, assertion, "remoteId", "localId");
    }

    private MockHttpServletRequest mockHttpServletRequest(String requestURI) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        if ((requestURI != null) && (requestURI.length() > 0)) {
            request.setRequestURI(requestURI);
        }

        SavedRequest savedRequest = new DefaultSavedRequest(request, new PortResolverImpl());
        request.getSession().setAttribute(SPRING_SECURITY_SAVED_REQUEST_KEY, savedRequest);

        return request;
    }

    private void setupCallToAuthorizedEnheterForHosPerson() {
        vardgivare = new Vardgivare(VARDGIVARE_HSAID, "IFV Testlandsting");
        vardgivare.getVardenheter().add(new Vardenhet(ENHET_HSAID_1, "VårdEnhet2A"));
        vardgivare.getVardenheter().add(new Vardenhet(ENHET_HSAID_2, "Vårdcentralen"));

        List<Vardgivare> vardgivareList = Collections.singletonList(vardgivare);
        when(hsaOrganizationsService.getAuthorizedEnheterForHosPerson(PERSONAL_HSAID)).thenReturn(vardgivareList);
    }

    private void setupCallToGetHsaPersonInfo() {
        List<String> specs = Arrays.asList("Kirurgi", "Öron-, näs- och halssjukdomar", "Reumatologi");
        List<String> titles = Arrays.asList("Läkare", "Psykoterapeut");

        List<PersonInformationType> userTypes = Collections.singletonList(buildPersonInformationType(PERSONAL_HSAID, TITLE_HEAD_DOCTOR, specs, titles));

        when(hsaPersonService.getHsaPersonInfo(PERSONAL_HSAID)).thenReturn(userTypes);
    }

    private void setupCallToWebcertFeatureService() {
        Set<String> availableFeatures = new TreeSet<>();
        availableFeatures.add("feature1");
        availableFeatures.add("feature2");
        when(webcertFeatureService.getActiveFeatures()).thenReturn(availableFeatures);
    }

}
