package no.unit.ill.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static no.unit.ill.services.BaseBibliotekService.WRONG_URL_FOR_GET_IN_BASEBIBLIOTEK_SERVICE_FOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseBibliotekServiceTest {

    BaseBibliotekService baseBibliotekService;
    BaseBibliotekServiceConnection connection;

    @BeforeEach
    public void init() throws JAXBException {
        this.connection = mock(BaseBibliotekServiceConnection.class);
        this.baseBibliotekService = new BaseBibliotekService(connection);
    }

    @Test
    public void testEmptyConstructor() throws JAXBException {
        baseBibliotekService = new BaseBibliotekService(); //a bit cheating for codeCoverage
    }

    @Test
    public void libraryLookupByBibnr() throws IOException, URISyntaxException, JAXBException {
        InputStream inputStream = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotek.xml");
        when(connection.connect(anyString())).thenReturn(inputStream);
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("GOL", basebibliotekBean.getBibKode());
        assertEquals("https://ncip.mikromarc.no/ncipservice/ncipresponder/parser?db=hallingdal-felles",
                basebibliotekBean.getNncippServer());
        assertEquals("NO-2061700", basebibliotekBean.getBibNr());
        assertEquals("", basebibliotekBean.getBibsysBibcode());
        assertEquals("", basebibliotekBean.getStengtFra());
    }

    @Test
    public void libraryLookupByBibnrThrowsIOException() throws IOException, URISyntaxException, JAXBException {
        when(connection.connect(anyString())).thenThrow(new IOException());
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(basebibliotekBean);
    }

    @Test
    public void libraryLookupByBibnrThrowsUriSyntaxException() throws IOException, URISyntaxException, JAXBException {
        when(connection.connect(anyString())).thenThrow(new URISyntaxException("bibnummer",
                WRONG_URL_FOR_GET_IN_BASEBIBLIOTEK_SERVICE_FOR));
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(basebibliotekBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutBibcode() throws IOException, URISyntaxException, JAXBException {
        InputStream inputStream =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenBibkode.xml");
        when(connection.connect(anyString())).thenReturn(inputStream);
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(basebibliotekBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutLangCode() throws IOException, URISyntaxException, JAXBException {
        InputStream inputStream =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenLandkode.xml");
        when(connection.connect(anyString())).thenReturn(inputStream);
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(basebibliotekBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsClosedLibrary() throws IOException, URISyntaxException, JAXBException {
        InputStream inputStream = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekStengt.xml");
        when(connection.connect(anyString())).thenReturn(inputStream);
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("U", basebibliotekBean.getStengt());
    }

    @Test
    public void libraryLookupWithLineEnding() throws IOException, URISyntaxException, JAXBException {
        InputStream inputStream = getClass().getResourceAsStream("/baseBibliotekWithLineEndingInInst.xml");
        when(connection.connect(anyString())).thenReturn(inputStream);
        BaseBibliotekBean basebibliotekBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("Anno Kongsvinger museum\nBiblioteket", basebibliotekBean.getInst());
    }

}

