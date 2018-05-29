package com.ucbcba.demo.Controllers;

import com.ucbcba.demo.Entities.*;

import com.ucbcba.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
        model.addAttribute( "categories", categoryService.listAllCategories());
        model.addAttribute("cities", cityService.listAllCities());
        model.addAttribute("username", this.username);
        return "home";
    }

    @RequestMapping("/search/{id}")
    String searchCategory(@PathVariable Integer id,Model model)throws UnsupportedEncodingException {
        auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = (auth.getName() == "anonymousUser")?"not logged in":auth.getName();
        if(username == "not logged in"){
            model.addAttribute("actualRole", "CLIENTE");
        }else{
            model.addAttribute("actualRole", userService.findByUsername(username).getRole());
        }
        byte[] bytes;
        String fot;
        List<Restaurant> restaurantIterable = (List<Restaurant>)restaurantService.listAllRestaurants();
        for(int i=0; i<restaurantIterable.size(); i++){
            bytes = Base64.encode(restaurantIterable.get(i).getFoto());
            fot = new String(bytes,"UTF-8");
            restaurantIterable.get(i).setF(fot);
        }

        model.addAttribute( "categories", categoryService.listAllCategories());
        Category categories = categoryService.getRestaurant(id);
        List<Restaurant> restaurants = categories.getRestaurants();
        model.addAttribute("restaurants",restaurants);
        return "search";
    }

    @RequestMapping("/search")
    String searchByName(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "id")Integer id, Model model)throws UnsupportedEncodingException {
        model.addAttribute("cities", cityService.listAllCities());
        auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = (auth.getName() == "anonymousUser")?"not logged in":auth.getName();
        if(username == "not logged in"){
            model.addAttribute("actualRole", "CLIENTE");
        }else{
            model.addAttribute("actualRole", userService.findByUsername(username).getRole());
        }


        byte[] bytes;
        String fot;
        if(name.equals("")){
            City city = cityService.getCity(id);
            List<Restaurant> restaurants = city.getRestaurants();
            for(int i=0; i<restaurants.size(); i++){
                bytes = Base64.encode(restaurants.get(i).getFoto());
                fot = new String(bytes,"UTF-8");
                restaurants.get(i).setF(fot);
            }
            model.addAttribute("restaurants",restaurants);
            return "searchCity";
        }
        else{

            List<Restaurant> restaurants1 = (List<Restaurant>)restaurantService.getRestaurantLikeName(name,id);
            for(int i=0; i<restaurants1.size(); i++){
                bytes = Base64.encode(restaurants1.get(i).getFoto());
                fot = new String(bytes,"UTF-8");
                restaurants1.get(i).setF(fot);
            }
            model.addAttribute("restaurants",restaurants1);
            return "search";
        }

    }

    @RequestMapping("/ADMIN/newRestaurant")
    String newRestaurant(Model model) {
        model.addAttribute("restaurant",new Restaurant());
        model.addAttribute( "categories", categoryService.listAllCategories());
        model.addAttribute("cities", cityService.listAllCities());
        return "newRestaurant";
    }
    @RequestMapping("/restaurants")
    String list(Model model) throws UnsupportedEncodingException {
        auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = (auth.getName() == "anonymousUser")?"not logged in":auth.getName();
        if(username == "not logged in"){
            model.addAttribute("actualRole", "CLIENTE");
        }else{
            model.addAttribute("actualRole", userService.findByUsername(username).getRole());
        }
        byte[] bytes;
        String fot;
        List<Restaurant> restaurantIterable = (List<Restaurant>)restaurantService.listAllRestaurants();
        for(int i=0; i<restaurantIterable.size(); i++){
            bytes = Base64.encode(restaurantIterable.get(i).getFoto());
            fot = new String(bytes,"UTF-8");
            restaurantIterable.get(i).setF(fot);

        }
        model.addAttribute("restaurants", restaurantService.listAllRestaurants());
        return "restaurants";
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
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("fot",fot);
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
}
