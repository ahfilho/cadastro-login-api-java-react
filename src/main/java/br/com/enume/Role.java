package br.com.enume;

public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USUARIO");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
