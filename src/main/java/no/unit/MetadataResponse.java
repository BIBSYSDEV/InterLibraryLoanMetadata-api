package no.unit;

import java.util.ArrayList;
import java.util.List;
import nva.commons.core.JsonSerializable;

public class MetadataResponse implements JsonSerializable {

    public String isbn;
    public String source;
    public String record_id;
    public String publicationPlace;
    public String b_title;
    public String volume;
    public String creation_year;
    public String creator;
    public String pages;
    public String publisher;
    public String display_title;
    public List<Library> libraries = new ArrayList<>();

    protected class Library {

        public String institution_code;
        public String display_name;
        public String mms_id;
        public String library_code;
        public String ncip_server_url;

    }
}
