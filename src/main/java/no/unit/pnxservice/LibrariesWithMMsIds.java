package no.unit.pnxservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibrariesWithMMsIds {
    private final String institutionId;
    private List<String> libraryNumbers;
    private List<String> mmsIds;

    /**
     * Constructor.
     * @param institutionId received from the lad11 record
     */
    public LibrariesWithMMsIds(String institutionId) {
        this.institutionId = institutionId;
        libraryNumbers = new ArrayList<>();
        mmsIds = new ArrayList<>();
    }

    public void setLibrary_number(List<String> libraryNumbers) {
        this.libraryNumbers = libraryNumbers;
    }

    public void addLibraryNumber(String libraryNumber) {
        libraryNumbers.add(libraryNumber);

    }

    public void setMms_ids(List<String> mmsIds) {
        this.mmsIds = mmsIds;
    }

    public void addMMId(String mmsId) {
        mmsIds.add(mmsId);
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public List<String> getLibraryNumbers() {
        return libraryNumbers;
    }

    public List<String> getMms_ids() {
        return mmsIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LibrariesWithMMsIds)) {
            return false;
        } else {
            return this.institutionId.equals(((LibrariesWithMMsIds) obj).institutionId);
        }
    }

    /**
     * converts LibrariesWithMMSids to a json object.
     * @return json representation of the object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("institusion_id", institutionId);
        jsonObject.put("library_numbers", new JSONArray(libraryNumbers));
        jsonObject.put("mms_ids", new JSONArray(mmsIds));
        return jsonObject;
    }
}
