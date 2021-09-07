package no.unit.pnxservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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



    public void addLibraryNumber(String libraryNumber) {
        libraryNumbers.add(libraryNumber);
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public List<String> getLibraryNumbers() {
        return libraryNumbers;
    }

    public List<String> getMmsIds() {
        return mmsIds;
    }

    public void addMMId(String mmsId) {
        mmsIds.add(mmsId);
    }

    public void setLibraryNumbers(List<String> libraryNumbers) {
        this.libraryNumbers = libraryNumbers;
    }

    public void setMmsIds(List<String> mmsIds) {
        this.mmsIds = mmsIds;
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

    @Override
    public int hashCode() {
        return Objects.hash(institutionId);
    }
}
