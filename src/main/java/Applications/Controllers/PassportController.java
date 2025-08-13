package Applications.Controllers;

import Applications.model.Passport;
import Applications.model.User;
import Applications.services.PassportService;
import Applications.services.UserService;
import Applications.util.PassportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/passports")
public class PassportController {
    private final PassportService passportService;
    private final UserService userService;
    private final PassportValidator passportValidator;

    @Autowired
    public PassportController(PassportService passportService, UserService userService, PassportValidator passportValidator) {
        this.passportService = passportService;
        this.userService = userService;
        this.passportValidator = passportValidator;
    }
@GetMapping()
public String index(Model model){
        model.addAttribute("passports", passportService.allPassports());
    return "passport/index";
}
@GetMapping("/{id}")
public String show(Model model, @PathVariable("id") int id){
        model.addAttribute("passport", passportService.showByIdOfPassport(id));
        return "passport/show";
}

        @GetMapping("/new")
        public String newPassport(@ModelAttribute("passport") Passport passport, Model model) {
            model.addAttribute("users", userService.findAllUsers());
            return "passport/new";
        }

    @PostMapping()
    public String save(@ModelAttribute("passport") @Valid Passport passport,
                       BindingResult bindingResult,
                       @RequestParam("ownerId") Integer ownerId,
                       Model model) {

        if (ownerId == null) {
            bindingResult.rejectValue("owner", "error.passport", "Owner must be selected");
            model.addAttribute("users", userService.findAllUsers());
            return "passport/new";
        }

        User user = userService.findUserById(ownerId);
        passport.setOwner(userService.findUserById(ownerId));

        if (user.getPassport() != null) {
            bindingResult.rejectValue("owner", "error.passport", "User already has a passport");
            model.addAttribute("users", userService.findAllUsers());
            return "passport/new";
        }

        passportService.save(passport);
        return "redirect:/passports";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Passport passport = passportService.showByIdOfPassport(id);
        model.addAttribute("passport", passport);
        model.addAttribute("users", userService.findAllUsers());
        return "passport/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("passport") @Valid Passport passport,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "passport/edit";
        }

        passport.setId(id);
        passportService.update(id, passport);
        return "redirect:/passports";
    }


    @GetMapping("/{id}/delete")
public String deletePassport(@PathVariable("id")int id, Model model){
        model.addAttribute("passport", passportService.showByIdOfPassport(id));
        return "passport/delete";
}
@PostMapping("/{id}/delete")
public String delete(@PathVariable("id") int id){
        passportService.delete(id);
        return "redirect:/passports";
}


}
