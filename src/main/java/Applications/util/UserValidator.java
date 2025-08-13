package Applications.util;

import Applications.model.User;
import Applications.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
User user = (User) o;

if(userService.findByEmail(user.getEmail()) != null){
errors.rejectValue("email", "", "Email is already exists");
}

if(user.getName() == null || !user.getName().matches("^[a-zA-Z]{2,20}$")){
    errors.rejectValue("name", "", "Name should be from 2 to 20 characters, without numbers");
}

    }
}
