package com.ucbcba.demo.Controllers;

import com.ucbcba.demo.Entities.*;

import com.ucbcba.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RestaurantController {
    private RestaurantService restaurantService;
    private CityService cityService;
    private CategoryService categoryService;
    private CommentService commentService;
    private UserService userService;
    private LikeRestaurantService likeRestaurantService;
    private Authentication auth;
    private String username;

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService){ this.restaurantService = restaurantService; }
    @Autowired
    public void setCityService(CityService cityService){
        this.cityService = cityService;
    }
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    @Autowired
    public void setCategoryService(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @Autowired
    public void setLikeRestaurantService(LikeRestaurantService likeRestaurantService) { this.likeRestaurantService = likeRestaurantService; }
    @Autowired
    public void setLikeRestaurantService(CommentService commentService) { this.commentService = commentService; }


    @RequestMapping("/")
    String home(Model model) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = (auth.getName() == "anonymousUser")?"not logged in":auth.getName();
        com.ucbcba.demo.Entities.User user = new com.ucbcba.demo.Entities.User(0, "CLIENTE");
        model.addAttribute( "categories", categoryService.listAllCategories());
        model.addAttribute("cities", cityService.listAllCities());
        model.addAttribute("username", this.username);
        if(this.username != "not logged in"){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User)auth.getPrincipal()).getUsername();
            user = userService.findByUsername(username);
        }
        model.addAttribute("user", user);

        return "home";
    }

    @RequestMapping("/search")
    String searchRestaurant(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                            @RequestParam(value = "categoria", required = false, defaultValue = "0") Integer categoria,
                            @RequestParam(value = "ciudad", required = false, defaultValue = "0") Integer ciudad,
                            Model model) throws UnsupportedEncodingException {

        auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = (auth.getName() == "anonymousUser") ? "not logged in" : auth.getName();
        if (username == "not logged in") {
            model.addAttribute("actualRole", "CLIENTE");
        } else {
            model.addAttribute("actualRole", userService.findByUsername(username).getRole());
        }
        List<Restaurant> restaurants = new ArrayList<>();
        if (!name.isEmpty()) {
            if (categoria > 0) {
                if (ciudad > 0) {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantLikeNameCityCategory(name, ciudad, categoria);
                } else {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantLikeNameCategory(name, categoria);
                }
            } else {
                if (ciudad > 0) {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantLikeNameCity(name, ciudad);
                } else {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantByName(name);
                }
            }
        } else {
            if (categoria > 0) {
                if (ciudad > 0) {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantLikeCityCategory(ciudad, categoria);
                } else {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantByCategory(categoria);
                }
            } else {
                if (ciudad > 0) {
                    restaurants = (List<Restaurant>) restaurantService.getRestaurantByCity(ciudad);
                } else {
                    restaurants = (List<Restaurant>) restaurantService.listAllRestaurants();
                }
            }
        }

        // ordena los restaurantes por score

        for(int j=0;j<restaurants.size();j++){
            restaurants.get(j).setScore(0);
            for(int i=0;i<restaurants.get(j).getComments().size();i++){
                (restaurants.get(j)).setScore((restaurants.get(j)).getScore() + ((restaurants.get(j)).getComments().get(i).getScore()));
            }
        }
        restaurants.sort((s1, s2) -> s1.getScore().compareTo(s2.getScore()));
        Collections.reverse(restaurants);


        byte[] bytes;
        String fot;
        List<Double> latitudes = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            bytes = Base64.encode(restaurants.get(i).getFoto());
            fot = new String(bytes, "UTF-8");
            restaurants.get(i).setF(fot);
            latitudes.add(restaurants.get(i).getLatitude());
            longitudes.add(restaurants.get(i).getLongitude());
        }
        model.addAttribute("searchText", name);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("latitudes", latitudes);
        model.addAttribute("longitudes", longitudes);
        model.addAttribute("size", restaurants.size());
        model.addAttribute("ciudades", cityService.listAllCities());
        model.addAttribute("categorias", categoryService.listAllCategories());
        return "restaurants";
    }

    @RequestMapping("/ADMIN/newRestaurant")
    String newRestaurant(Model model) {
        model.addAttribute("restaurant",new Restaurant());
        model.addAttribute( "categories", categoryService.listAllCategories());
        model.addAttribute("cities", cityService.listAllCities());
        return "newRestaurant";
    }

    @RequestMapping("/ADMIN/deleteRestaurant/{id}")
    String delete(@PathVariable Integer id) {
        restaurantService.deleteRestaurant(id);
        return "redirect:/ADMIN";

    }
    @RequestMapping(value = "/restaurant", method = RequestMethod.POST)
    String save(Restaurant restaurant, @RequestParam("file") MultipartFile files) throws IOException {
        byte[] f;
        if(!files.isEmpty()){
            f = files.getBytes();
            restaurant.setFoto(f);
        }else{
            Restaurant r = restaurantService.getRestaurant(restaurant.getId());
            restaurant.setFoto(r.getFoto());
        }
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/ADMIN";
    }
    @RequestMapping("/ADMIN/editRestaurant/{id}")
    String editRestaurant(@PathVariable Integer id, Model model) {
        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("cities", cityService.listAllCities());
        return "editRestaurant";
    }
    @RequestMapping("/showRestaurant/{id}")
    String showRestaurant(@PathVariable Integer id, Model model) throws UnsupportedEncodingException {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName() == "anonymousUser"){
            this.username = "not logged in";
            model.addAttribute("comment", false);
        }else{
            this.username = auth.getName();
            if(commentService.existsComment( userService.findByUsername(this.username), restaurantService.getRestaurant(id))){
                model.addAttribute("commentFunction", false);
            }else{
                model.addAttribute("commentFunction", true);
            }
        }
        Restaurant restaurant = restaurantService.getRestaurant(id);
        byte[] bytes;
        String fot;
        bytes = Base64.encode(restaurant.getFoto());
        fot = new String(bytes,"UTF-8");

        restaurant.setScore(0);
        for(int i=0;i<restaurant.getComments().size();i++){
            restaurant.setScore(restaurant.getScore() + (restaurant.getComments().get(i).getScore()));
        }
        if(restaurant.getComments().size() > 0){
            restaurant.setScore(restaurant.getScore()/restaurant.getComments().size());
        }
        model.addAttribute("fot", fot);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("user", userService.findByUsername(this.username));
        return "showRestaurant";
    }

    @RequestMapping(value = "/restaurant/like", method = RequestMethod.POST)
    String likeRestaurant(LikeRestaurant likeRestaurant) {
        List<LikeRestaurant> likeRestaurantList = (List<LikeRestaurant>)likeRestaurantService.listAllLikeRestaurant();
        for(int i=0; i<likeRestaurantList.size(); i++){
            if(likeRestaurantList.get(i).getRestaurant().getId() == likeRestaurant.getRestaurant().getId() &&
                    likeRestaurantList.get(i).getUser().getId() == likeRestaurant.getUser().getId()){
                return "redirect:/showRestaurant/" + likeRestaurant.getRestaurant().getId();
            }
        }
        likeRestaurantService.saveLikeRestaurant(likeRestaurant);
        return "redirect:/showRestaurant/" + likeRestaurant.getRestaurant().getId();
    }

    @RequestMapping("/ADMIN")
    String listADMIN(Model model) throws UnsupportedEncodingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User)auth.getPrincipal()).getUsername();
        com.ucbcba.demo.Entities.User user = userService.findByUsername(username);
        byte[] bytes;
        String fot;
        List<Restaurant> restaurantIterable = (List<Restaurant>)restaurantService.listAllRestaurants();
        for(int i=0; i<restaurantIterable.size(); i++){
            bytes = Base64.encode(restaurantIterable.get(i).getFoto());
            fot = new String(bytes,"UTF-8");
            restaurantIterable.get(i).setF(fot);
        }
        model.addAttribute("restaurants", restaurantService.listAllRestaurants());
        return "admin";
    }

    @RequestMapping("/profile/{id}")
    public String perfil(@PathVariable Integer id, Model model) throws UnsupportedEncodingException {
        com.ucbcba.demo.Entities.User user = userService.getUser(id);
        if(user != null){
            byte[] bytes;
            String fot;
            bytes = Base64.encode(user.getFoto());
            fot = new String(bytes,"UTF-8");
            model.addAttribute("fot",fot);

            model.addAttribute("user", user);
            return "showProfile";
        }
        return "redirect:/";
    }
    @RequestMapping("/ranking")
    public String ranking(Model model) throws UnsupportedEncodingException {
        List<Restaurant> restaurants = (List<Restaurant>)restaurantService.listGeneralRanking();
        byte[] bytes;
        String fot;
        List<Double> latitudes = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            bytes = Base64.encode(restaurants.get(i).getFoto());
            fot = new String(bytes, "UTF-8");
            restaurants.get(i).setF(fot);
            latitudes.add(restaurants.get(i).getLatitude());
            longitudes.add(restaurants.get(i).getLongitude());
        }
        model.addAttribute("restaurants", restaurants);
        return "ranking";
    }
}
