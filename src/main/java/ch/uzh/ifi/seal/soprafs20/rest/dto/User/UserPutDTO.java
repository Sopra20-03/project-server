package ch.uzh.ifi.seal.soprafs20.rest.dto.User;

public class UserPutDTO {
    private String name;
    private String username;
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }
}
