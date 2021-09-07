package no.unit.ill.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import no.unit.LibraryBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

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
    public void libraryLookupByBibnr() throws IOException, URISyntaxException, JAXBException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotek.xml");
        when(connection.connect(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("GOL", libraryBean.getBibkode());
        assertEquals("https://ncip.mikromarc.no/ncipservice/ncipresponder/parser?db=hallingdal-felles", libraryBean.getNncipp_server());
        assertEquals("NO-2061700", libraryBean.getBibnr());
        assertEquals("", libraryBean.getBibsysBibcode());
        assertEquals("", libraryBean.getStengt_fra());
    }

    @Test
    public void libraryLookupByBibnrThrowsException() throws IOException, URISyntaxException, JAXBException {
        when(connection.connect(anyString())).thenThrow(new IOException());
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutBibcode() throws IOException, URISyntaxException, JAXBException {
        InputStream inputstreamFromFile =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenBibkode.xml");
        when(connection.connect(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutLangCode() throws IOException, URISyntaxException, JAXBException {
        InputStream inputstreamFromFile =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenLandkode.xml");
        when(connection.connect(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsClosedLibrary() throws IOException, URISyntaxException, JAXBException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekStengt.xml");
        when(connection.connect(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("U", libraryBean.getStengt());
    }

    @Test
    public void libraryLookupWithLineEnding() throws IOException, URISyntaxException, JAXBException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/baseBibliotekWithLineEndingInInst.xml");
        when(connection.connect(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekService.libraryLookupByBibnr("xxxxx");
        assertEquals("Anno Kongsvinger museum\nBiblioteket", libraryBean.getInst());
    }

}

