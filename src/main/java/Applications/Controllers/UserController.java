package Applications.Controllers;



import Applications.model.Passport;
import Applications.model.User;

import Applications.services.PassportService;
import Applications.services.UserService;
import Applications.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

private final UserService userService;
private final UserValidator userValidator;
private final PassportService passportService;
@Autowired
    public UserController(UserService userService, UserValidator userValidator, PassportService passportService) {
        this.userService = userService;
    this.userValidator = userValidator;
    this.passportService = passportService;
}

    @GetMapping()
    public String index(Model model){
model.addAttribute("users", userService.findAllUsers());
return "user/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id, @ModelAttribute("passport")Passport passport){
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("passport", passportService.findByOwner(userService.findUserById(id)));
    return "user/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user){
return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
    if(bindingResult.hasErrors())
        return "user/new";

    userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("user", userService.findUserById(id));
        return "user/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, @PathVariable("id") int id, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
    if(bindingResult.hasErrors())
        return "user/edit";

    user.setId(id);
userService.update(user.getId(), user);
        return "redirect:/users";

    }
    @GetMapping("/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id){
model.addAttribute("user", userService.findUserById(id));
        return "user/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id){
        userService.delete(id);
        return "redirect:/users";
    }
}
