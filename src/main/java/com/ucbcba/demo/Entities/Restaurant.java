package com.ucbcba.demo.Entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private Float score = Float.valueOf(0);

    @NotNull
    private String name;

    @NotNull
    private String direction;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "level_restaurant_id")
    private LevelRestaurant level;

    @NotNull
    private String phone;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<LikeRestaurant> likeRestaurants;

    @Lob
    @Column(columnDefinition="longblob")
    private byte[] foto;

    private String f;

    private Double latitude;
    private Double longitude;

    public String getF() { return f; }

    public void setF(String f) { this.f = f; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public byte[] getFoto() { return foto; }

    public void setFoto(byte[] foto) { this.foto = foto; }

    public List<LikeRestaurant> getLikeRestaurants() { return likeRestaurants; }

    public void setLikeRestaurants(List<LikeRestaurant> likeRestaurants) { this.likeRestaurants = likeRestaurants; }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LevelRestaurant getLevel() {
        return level;
    }

    public void setLevel(LevelRestaurant level) {
        this.level = level;
    }
}
