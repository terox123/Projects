package com.UserPassportBoot.Controllers;


import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.services.PassportService;
import com.UserPassportBoot.services.UserService;
import com.UserPassportBoot.util.PassportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/passports")
public class PassportController {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_FIELD = "id";

    private final PassportService passportService;
    private final UserService userService;
    private final PassportValidator passportValidator;

    @Autowired
    public PassportController(PassportService passportService,
                              UserService userService,
                              PassportValidator passportValidator) {
        this.passportService = passportService;
        this.userService = userService;
        this.passportValidator = passportValidator;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(defaultValue = DEFAULT_SORT_FIELD) String sortBy,
                        @RequestParam(defaultValue = "asc") String direction,
                        @RequestParam(required = false) String sort) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Passport> passportPage = getPassportPageBasedOnSort(sort, pageable);

        addPaginationAttributes(model, passportPage, page, size, sortBy, direction, sort);
        return "passport/index";
    }

    private Page<Passport> getPassportPageBasedOnSort(String sort, Pageable pageable) {
        return switch (Optional.ofNullable(sort).orElse("")) {
            case "young" -> passportService.findAllPassportByOwnerBirthDateDesc(pageable);
            case "old" -> passportService.findAllPassportByOwnerBirthDateAsc(pageable);
            default -> passportService.allPassports(pageable);
        };
    }

    private void addPaginationAttributes(Model model, Page<Passport> page,
                                         int currentPage, int size,
                                         String sortBy, String direction, String sort) {
        model.addAttribute("passports", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        model.addAttribute("sort", sort);
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("passport", passportService.showByIdOfPassport(id));
        return "passport/show";
    }

    @GetMapping("/new")
    public String newPassport(@ModelAttribute("passport")  Passport passport,
                              Model model,
                              @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        model.addAttribute("users", userService.findAllUsers(pageable));
        return "passport/new";
    }

    @PostMapping
    public String save(@ModelAttribute("passport") @Valid Passport passport,
                       BindingResult bindingResult,
                       @RequestParam("ownerId") Integer ownerId,
                       Model model,
                       @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        passportValidator.validate(passport, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers(pageable));
            return "passport/new";
        }

        User owner = userService.findUserById(ownerId);
        passport.setOwner(owner);

        passportService.save(passport);
        return "redirect:/passports";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model,
                       @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Passport passport = passportService.showByIdOfPassport(id);
        model.addAttribute("passport", passport);
        model.addAttribute("users", userService.findAllUsers(pageable));
        return "passport/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("passport") @Valid Passport passport,
                         BindingResult bindingResult,
                         Model model,
                         @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        passportValidator.validate(passport, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers(pageable));
            return "passport/edit";
        }

        passport.setId(id);
        passportService.update(id, passport);
        return "redirect:/passports";
    }

    @GetMapping("/{id}/delete")
    public String deletePassport(@PathVariable("id") int id, Model model) {
        Passport passport = passportService.showByIdOfPassport(id);
        model.addAttribute("passport", passport);
        model.addAttribute("owner", passport.getOwner());
        return "passport/delete";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        passportService.delete(id);
        return "redirect:/passports";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "passport/search";
    }

    @GetMapping("/search/results")
    public String searchResult(Model model,
                               @RequestParam("numberPart") String partOfNumber,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Passport> passportPage = passportService.searchPassportByStartingWith(partOfNumber, pageable);

        model.addAttribute("passportsByPart", passportPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", passportPage.getTotalPages());
        model.addAttribute("totalElements", passportPage.getTotalElements());
        model.addAttribute("numberPart", partOfNumber);

        return "passport/searchResult";
    }
}