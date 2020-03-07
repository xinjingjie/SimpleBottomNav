package com.example.simplebottomnav.bean;

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

    public User() {
    }

    public User(String username, String gender, String password, int age, String create_time, String telephone, String email, int pic_number) {
        this.username = username;
        this.gender = gender;
        this.password = password;
        this.age = age;
        this.create_time = create_time;
        this.telephone = telephone;
        this.email = email;
        this.pic_number = pic_number;
    }

    public User(String username, String gender, String password, int age, String create_time, String telephone, String email, int pic_number, int fans_number, int sub_number) {
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
}
