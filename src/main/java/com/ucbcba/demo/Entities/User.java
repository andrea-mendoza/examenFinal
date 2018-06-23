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

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private String role = "CLIENTE";
    private String password;
    private String passwordConfirm;

    private String name;
    private Integer telephone;
    @Lob
    @Column(columnDefinition="longblob")
    private byte[] foto;

    private String intereses = "";
    private String descripcion = "";
    private String aficiones = "";

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

    public String getF() { return f; }

    public void setF(String f) { this.f = f; }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAficiones() {
        return aficiones;
    }

    public void setAficiones(String aficiones) {
        this.aficiones = aficiones;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
