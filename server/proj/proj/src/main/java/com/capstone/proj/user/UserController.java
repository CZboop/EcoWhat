package com.capstone.proj.user;

import com.capstone.proj.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PutMapping("{id}")
    public void updateUser(@PathVariable("id") int id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }

    // || ===================  Login Authentication ===================== ||

    @PostMapping("login")
    public Token authenticateLogin(
            @RequestParam("email") final String email,
            @RequestParam("password") final String password) {
                return userService.authenticateLogin(email, password);
    }

    @PostMapping("{id}")
    public Optional<User> getLoggedInUserById(@PathVariable int id, @RequestBody Token token) {
        return userService.getLoggedInUserById(id, token);
    }

    @PostMapping("logout")
    public void logOut(@RequestBody Token token) {
        userService.logOut(token);
    }

//    methods related to when the user last contacted mp

    @PostMapping("/contacted/{id}")
    public void addContactTimeForUser(@PathVariable int id, @RequestBody String time){
        userService.addContactTimeForUser(id, time);
    }

    @GetMapping("/lastcontact/{id}")
    public String getLastContactForUser(@PathVariable int id){
        return userService.getLastContactForUser(id);
    }

    // old methods that don't coincide with token based authentication
//    @PostMapping("user")
//    public Optional<User> findByToken(@RequestBody String token) {
//        return userService.findByToken(token);
//    }
//
//    @PatchMapping("token")
//    public void removeTokenOnLogOut(@RequestBody String token) {
//        userService.removeTokenOnLogOut(token);
//    }
}
