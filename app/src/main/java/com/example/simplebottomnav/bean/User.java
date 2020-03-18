package com.example.simplebottomnav.bean;

import java.util.Objects;

public class User {
    private int uid;
    private String username;
    private String gender;
    private String password;
    private int age;
    private String create_time;
    private String telephone;
    private String email;
    private int pic_number;
    private int fans_number;
    private int sub_number;
    private String profile_picture;
    private String background_image;
    public User() {
    }

    public User(String username, String gender, String password, int age, String create_time, String telephone, String email, int pic_number, String profile_picture, String background_image) {
        this.username = username;
        this.gender = gender;
        this.password = password;
        this.age = age;
        this.create_time = create_time;
        this.telephone = telephone;
        this.email = email;
        this.pic_number = pic_number;
        this.profile_picture = profile_picture;
        this.background_image = background_image;
    }

    public User(String username, String gender, String password, int age, String create_time, String telephone, String email, int pic_number, int fans_number, int sub_number, String profile_picture, String background_image) {
        this.username = username;
        this.gender = gender;
        this.password = password;
        this.age = age;
        this.create_time = create_time;
        this.telephone = telephone;
        this.email = email;
        this.pic_number = pic_number;
        this.fans_number = fans_number;
        this.sub_number = sub_number;
        this.profile_picture = profile_picture;
        this.background_image = background_image;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPic_number() {
        return pic_number;
    }

    public void setPic_number(int pic_number) {
        this.pic_number = pic_number;
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }

    public int getSub_number() {
        return sub_number;
    }

    public void setSub_number(int sub_number) {
        this.sub_number = sub_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", create_time=" + create_time +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", pic_number=" + pic_number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid &&
                age == user.age &&
                pic_number == user.pic_number &&
                fans_number == user.fans_number &&
                sub_number == user.sub_number &&
                Objects.equals(username, user.username) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(password, user.password) &&
                Objects.equals(create_time, user.create_time) &&
                Objects.equals(telephone, user.telephone) &&
                Objects.equals(email, user.email) &&
                Objects.equals(profile_picture, user.profile_picture) &&
                Objects.equals(background_image, user.background_image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username, gender, password, age, create_time, telephone, email, pic_number, fans_number, sub_number, profile_picture, background_image);
    }
}
