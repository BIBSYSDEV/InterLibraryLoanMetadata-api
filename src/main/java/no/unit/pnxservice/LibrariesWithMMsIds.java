package no.unit.pnxservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibrariesWithMMsIds {
    private final String institutionId;
    private List<String> libraryNumbers;
    private List<String> mms_ids;

    public LibrariesWithMMsIds(String institutionId) {
        this.institutionId = institutionId;
        libraryNumbers = new ArrayList<>();
        mms_ids = new ArrayList<>();
    }

    public void setLibrary_number(List<String> libraryNumbers) {
        this.libraryNumbers = libraryNumbers;
    }

    public void addLibraryNumber(String libraryNumber){
        libraryNumbers.add(libraryNumber);

    }

    public void setMms_ids(List<String> mms_ids) {
        this.mms_ids = mms_ids;
    }

    public void addMMId(String mmsId){
        mms_ids.add(mmsId);
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public List<String> getLibraryNumbers() {
        return libraryNumbers;
    }

    public List<String> getMms_ids() {
        return mms_ids;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LibrariesWithMMsIds)) {
            return false;
        } else {
           return this.institutionId.equals(((LibrariesWithMMsIds) obj).institutionId);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("institusion_id", institutionId);
        jsonObject.put("library_numbers", new JSONArray(libraryNumbers));
        jsonObject.put("mms_ids", new JSONArray(mms_ids));
        return jsonObject;
    }
}
