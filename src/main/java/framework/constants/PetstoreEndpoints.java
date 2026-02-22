package framework.constants;

public final class PetstoreEndpoints {

    private PetstoreEndpoints() {}

    public static final String PET = "/pet";
    public static final String PET_BY_ID = "/pet/{petId}";
    public static final String FIND_BY_STATUS = "/pet/findByStatus";
    public static final String UPLOAD_IMAGE = "/pet/{petId}/uploadImage";
}