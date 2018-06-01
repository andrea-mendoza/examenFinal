package com.ucbcba.demo.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String username = "";

    private String role = "CLIENTE";
    private String password;
    private String passwordConfirm;

    private String name;
    private Integer telephone;
    @Lob
    @Column(columnDefinition="longblob")
    private byte[] foto = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };

    private String f;

    public User() { }

    public User(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getTelephone() { return telephone; }

    public void setTelephone(Integer telephone) { this.telephone = telephone; }

    public byte[] getFoto() { return foto; }

    public void setFoto(byte[] foto) { this.foto = foto; }

    public String getF() { return f; }

    public void setF(String f) { this.f = f; }
}
