package rayservice;

public enum RayStatus {
    OK("ok"),
    FIND("found"),
    UNFIND("unfound"),
    INACT("inactvated"),
    UNMATCHED("unmatched"),
    DUPLICATED("duplicated"),
    SQL_FAILURE("sql error: "),
    ERROR("unknown error: ");
    
    private String value;
    
    private RayStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
