package taack.universe;

public enum Role {
    SYSTEM("system"),
    USER("role");
    private final String value;
    Role(String roleValue) {
        this.value = roleValue;
    }
    public String getRoleValue() {
        return this.value;
    }


}
