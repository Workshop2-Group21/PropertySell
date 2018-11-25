package com.uyr.yusara.dreamhome.Modal;

public class User {
    public String name;
    public String email;
    public String phone;
    public String role;
    public String profileimage2;

    public User()
    {

    }

    public User(String name, String email, String phone, String role,String profileimage2) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.profileimage2 = profileimage2;
    }

    public String getProfileimage() {
        return profileimage2;
    }

    public void setProfileimage(String profileimage2) {
        this.profileimage2 = profileimage2;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
