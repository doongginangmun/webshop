package com.toy.webshop.controlloer;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.User;
import com.toy.webshop.form.UserForm;
import com.toy.webshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @GetMapping("/users/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }

    /**
     * 회원가입
     */
    @PostMapping("/users/new")
    public String create(@Valid UserForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "/users/createUserForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        User user = User.builder()
                    .email(form.getEmail())
                    .password(encoder.encode(form.getPassword()))
                    .name(form.getName())
                    .address(address)
                    .build();

        userService.save(user);
        return "redirect:/";
    }

    /**
     * 로그인
     */
    @GetMapping("/signin")
    public String signin() { return "users/signinForm"; }

    @GetMapping("/login-error")
    public String loginError(@RequestParam(value = "exception", required = false)String exception,
                             Model model) {
        model.addAttribute("exception", exception);
        model.addAttribute("loginError", true);
        return "users/signinForm";
    }

    /**
     * 유저 전체 조회
     */
    @GetMapping("/users")
    public String userList(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        return "users/userList";
    }

    /**
     * ajax - email 중복 체크
     * @param email 검증할 email
     * @return 유효한 이메일인지, 중복이 있는지 없는지 map에 담아서 return
     */
    @GetMapping("/duplicate")
    @ResponseBody
    public Map<String, Boolean> duplicateEmailCheck(String email) {
        boolean validEmail = isValidEmail(email);
        boolean duplicateEmail = userService.validateDuplicateEmail(email);

        Map<String, Boolean> map = new HashMap<>();
        map.put("validEmail", validEmail);
        map.put("duplicateEmail", duplicateEmail);

        return map;
    }

    /**
     * 유효한 이메일인지 검증
     */
    private boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches())
            err = true;
        return err;
    }
}
