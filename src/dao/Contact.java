package dao;

public class Contact {

    private String userID;
    private String firstName;
    private String lastName;
    private String email;

    public Contact() {
        this.userID = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
    }

    public Contact(String userID, String firstName, String lastName, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

}
