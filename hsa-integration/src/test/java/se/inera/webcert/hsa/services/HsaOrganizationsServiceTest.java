package se.inera.webcert.hsa.services;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.webcert.hsa.model.Mottagning;
import se.inera.webcert.hsa.model.Vardenhet;
import se.inera.webcert.hsa.model.Vardgivare;
import se.inera.webcert.hsa.stub.HsaServiceStub;
import se.inera.webcert.hsa.stub.Medarbetaruppdrag;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author andreaskaltenbach
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:HsaOrganizationsServiceTest/test-context.xml")
public class HsaOrganizationsServiceTest {

    private static final String PERSON_HSA_ID = "Gunilla";

    private static final String CENTRUM_VAST = "centrum-vast";
    private static final String CENTRUM_OST = "centrum-ost";
    private static final String CENTRUM_NORR = "centrum-norr";

    @Autowired
    private HsaOrganizationsService service;

    @Autowired
    private HsaServiceStub serviceStub;

    @Test
    public void testEmptyResultSet() {

        Collection<Vardgivare> vardgivare = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertTrue(vardgivare.isEmpty());
    }

    @Test
    public void testSingleEnhetWithoutMottagningar() {
        addMedarbetaruppdrag(PERSON_HSA_ID, asList(CENTRUM_NORR));

        List<Vardgivare> vardgivare = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertEquals(1, vardgivare.size());

        Vardgivare vg = vardgivare.get(0);
        assertEquals("vastmanland", vg.getId());
        assertEquals("Landstinget Västmanland", vg.getNamn());
        assertEquals(1, vg.getVardenheter().size());

        Vardenhet enhet = vg.getVardenheter().get(0);
        assertEquals("centrum-norr", enhet.getId());
        assertEquals("Vårdcentrum i Norr", enhet.getNamn());
        assertTrue(enhet.getMottagningar().isEmpty());
    }

    @Test
    public void testMultipleEnheter() {
        
        // Load with 3 MIUs belonging to the same vårdgivare
        addMedarbetaruppdrag(PERSON_HSA_ID, asList(CENTRUM_VAST, CENTRUM_OST, CENTRUM_NORR));

        List<Vardgivare> vardgivare = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertEquals(1, vardgivare.size());

        Vardgivare vg = vardgivare.get(0);
        assertEquals(3, vg.getVardenheter().size());
        
        // Assert that mottagningar was loaded for 'centrum-ost'
        Vardenhet centrumOst = getVardenhetById(CENTRUM_OST, vg.getVardenheter());
        assertEquals(1, centrumOst.getMottagningar().size());
    }

    private Vardenhet getVardenhetById(final String vardenhetId, List<Vardenhet> vardenheter) {
        
        Predicate<Vardenhet> pred = new Predicate<Vardenhet>() {
            public boolean apply(Vardenhet v) {
                return vardenhetId.equalsIgnoreCase(v.getId());
            }
        };
        
        Optional<Vardenhet> results = Iterables.tryFind(vardenheter, pred);
        
        return (results.isPresent()) ? results.get() : null;
    }
    
    
    @Test
    public void testMultipleVardgivare() throws IOException {
        
        // Load with another vardgivare, which gives two vardgivare available
        addVardgivare("HsaOrganizationsServiceTest/landstinget-kings-landing.json");

        // Assign Gunilla one MIU from each vardgivare 
        addMedarbetaruppdrag("Gunilla", asList(CENTRUM_NORR, "red-keep"));

        List<Vardgivare> vardgivare = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        
        assertEquals(2, vardgivare.size());
    }

    private void addMedarbetaruppdrag(String hsaId, List<String> enhetIds) {
        List<Medarbetaruppdrag.Uppdrag> uppdrag = new ArrayList<>();
        for (String enhet : enhetIds) {
            uppdrag.add(new Medarbetaruppdrag.Uppdrag(enhet));
        }
        serviceStub.getMedarbetaruppdrag().add(new Medarbetaruppdrag(hsaId, uppdrag));
    }

    private void addVardgivare(String file) throws IOException {
        Vardgivare vardgivare = new ObjectMapper().readValue(new ClassPathResource(file).getFile(), Vardgivare.class);
        serviceStub.getVardgivare().add(vardgivare);
    }

    @Before
    public void setupVardgivare() throws IOException {
        addVardgivare("HsaOrganizationsServiceTest/landstinget-vastmanland.json");
    }

    @After
    public void cleanupServiceStub() {
        serviceStub.getVardgivare().clear();
        serviceStub.getMedarbetaruppdrag().clear();
    }

    @Test
    public void testInactiveEnhetFiltering() throws IOException {

        addVardgivare("HsaOrganizationsServiceTest/landstinget-upp-och-ner.json");

        // Assign Gunilla 5 MIUs where 2 is inactive (finito and futuro)
        addMedarbetaruppdrag(PERSON_HSA_ID, asList("finito", "here-and-now", "futuro", "still-open", "will-shutdown"));

        List<Vardgivare> vardgivareList = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertEquals(1, vardgivareList.size());
        assertEquals("upp-och-ner", vardgivareList.get(0).getId());
        assertEquals(3, vardgivareList.get(0).getVardenheter().size());
    }

    @Test
    public void testInactiveMottagningFiltering() throws IOException {
        addVardgivare("HsaOrganizationsServiceTest/landstinget-upp-och-ner.json");

        addMedarbetaruppdrag(PERSON_HSA_ID, asList("with-subs"));

        List<Vardgivare> vardgivareList = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertEquals(1, vardgivareList.size());

        Vardgivare vardgivare = vardgivareList.get(0);
        assertEquals(1, vardgivare.getVardenheter().size());

        List<Mottagning> mottagningar = vardgivare.getVardenheter().get(0).getMottagningar();
        assertEquals(3, mottagningar.size());

        assertEquals("mottagning-here-and-now", mottagningar.get(0).getId());
        assertEquals("mottagning-still-open", mottagningar.get(1).getId());
        assertEquals("mottagning-will-shutdown", mottagningar.get(2).getId());
    }

    @Test
    public void testUppdragFiltering() {

        // user has a different medarbetaruppdrag ändamål 'Animatör' in one enhet
        serviceStub.getMedarbetaruppdrag().add(new Medarbetaruppdrag(PERSON_HSA_ID, asList(new Medarbetaruppdrag.Uppdrag("centrum-ost", "Animatör"))));

        List<Vardgivare> vardgivareList = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);

        // no authorized vardgivere should be returned
        assertTrue(vardgivareList.isEmpty());
    }

    @Test
    public void mottagningListasMenFinnsEj() throws IOException {
        // WEBCERT-749
        addVardgivare("HsaOrganizationsServiceTest/landstinget-inkonsistent.json");
        addMedarbetaruppdrag(PERSON_HSA_ID, asList("enhet1"));
        List<Vardgivare> vardgivare = service.getAuthorizedEnheterForHosPerson(PERSON_HSA_ID);
        assertEquals(1, vardgivare.size());
        assertTrue(vardgivare.get(0).getVardenheter().get(0).getMottagningar().isEmpty());
    }
}
