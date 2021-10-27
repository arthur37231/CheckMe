package comp5216.sydney.edu.au.checkme.entity;

import java.util.Objects;

public class User {
    private String fullName;
    private String phoneNumber;
    private String password;
    private String email;
    private String address;

    public User() {
    }

    public User(String fullName, String phoneNumber, String password, String email, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getPhoneNumber(), user.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhoneNumber());
    }
}
