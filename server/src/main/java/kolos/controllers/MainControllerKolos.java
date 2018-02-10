package kolos.controllers;

import kolos.models.UserModel;
import kolos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainControllerKolos {

    private final UserService userService;

    @GetMapping("/draw-page")
    public String drawPage(Model model) {
        model.addAttribute("logged", false);
        return "draw-page";
    }


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("logged", false);
        return "/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("nrIndeksu") Long nrIndeksu, Model model) {
        UserModel userModel = userService.findByUserNameAndPasswordAndNrIndeksu(username, password, nrIndeksu);
        if (userModel != null) {
            model.addAttribute("logged", true);
            return "redirect:/draw-page";
        } else {
            model.addAttribute("userDoesntExists", true);
            model.addAttribute("logged", false);
            return "login";
        }
    }

    /* // logout
        return redirect:login*/
    @PostMapping("/register")
    public String registerPost(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("nrIndeksu") Long nrIndeksu, Model model) {
        UserModel userModel = userService.findByUserNameAndPasswordAndNrIndeksu(username, password, nrIndeksu);
        if (userModel != null) {
            model.addAttribute("error", "Już istnieje taki uzytkownik!");
            model.addAttribute("savedLogin", false);
        } else {
            userService.save(UserModel.builder().username(username).password(password).nrIndeksu(nrIndeksu).build());
            model.addAttribute("savedLogin", true);
        }
        model.addAttribute("logged", false);
        return "/register";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("logged", false);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("logged", false);
        model.addAttribute("savedLogin", false);
        return "register";
    }
}