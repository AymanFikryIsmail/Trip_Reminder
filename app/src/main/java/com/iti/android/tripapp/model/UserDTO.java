package com.iti.android.tripapp.model;

/**
 * Created by ayman on 2019-02-08.
 */

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String img;
    private String password;
    private String phone;

    public UserDTO(String id, String name, String email, String password, String phone , String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
        this.password = password;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImg() {
        return img;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
