package taack.universe.implementation.mistral;

public enum MistralRole {
    SYSTEM("system"),
    USER("role");
    private final String value;
    MistralRole(String roleValue) {
        this.value = roleValue;
    }
    public String getRoleValue() {
        return this.value;
    }


}
