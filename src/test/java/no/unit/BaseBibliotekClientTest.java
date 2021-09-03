package no.unit;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseBibliotekClientTest {

    private static final String SAMPLE_BASEBIBLIOTEK_URL = "https://unit.no/test";

    HttpConnectionWrapper httpConnectionWrapper;
    BaseBibliotekClient baseBibliotekClient;

    @BeforeEach
    public void init() throws JAXBException {
        this.httpConnectionWrapper = mock(HttpConnectionWrapper.class);
        this.baseBibliotekClient = new BaseBibliotekClient(SAMPLE_BASEBIBLIOTEK_URL, httpConnectionWrapper);
    }

    @Test
    public void libraryLookupByBibnr() throws IOException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotek.xml");
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertEquals("GOL", libraryBean.getBibkode());
        assertEquals("https://ncip.mikromarc.no/ncipservice/ncipresponder/parser?db=hallingdal-felles", libraryBean.getNncipp_server());
        assertEquals("NO-2061700", libraryBean.getBibnr());
        assertEquals("", libraryBean.getBibsysBibcode());
        assertEquals("", libraryBean.getStengt_fra());
    }

    @Test
    public void libraryLookupByBibnrThrowsException() throws IOException {
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenThrow(new IOException());
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutBibcode() throws IOException {
        InputStream inputstreamFromFile =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenBibkode.xml");
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsWithoutLangCode() throws IOException {
        InputStream inputstreamFromFile =
                getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekUtenLandkode.xml");
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertNull(libraryBean);
    }

    @Test
    public void libraryLookupByBibnrThatReturnsClosedLibrary() throws IOException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotekStengt.xml");
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertEquals("U", libraryBean.getStengt());
    }

    @Test
    public void libraryLookupWithLineEnding() throws IOException {
        InputStream inputstreamFromFile = getClass().getResourceAsStream("/baseBibliotekWithLineEndingInInst.xml");
        when(httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(anyString())).thenReturn(inputstreamFromFile);
        LibraryBean libraryBean = baseBibliotekClient.libraryLookupByBibnr("xxxxx");
        assertEquals("Anno Kongsvinger museum\nBiblioteket", libraryBean.getInst());
    }

}

