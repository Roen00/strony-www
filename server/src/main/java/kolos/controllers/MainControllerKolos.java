package kolos.controllers;

import kolos.models.UserModel;
import kolos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class MainControllerKolos {

    private final UserService userService;

    private final String filesPath = "/home/rafalzel/projects/strony-www/server/files";

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
    public String login(@RequestParam("password") String password, @RequestParam("nrIndeksu") String nrIndeksuInString, Model model) {

        Long nrIndeksuInLong = null;
        try {
            nrIndeksuInLong = Long.parseLong(nrIndeksuInString);
        } catch (NumberFormatException e) {
            model.addAttribute("NumberFormatException", true);
        }
        UserModel userModel = userService.findByAndPasswordAndNrIndeksu(password, nrIndeksuInLong);

            if (userModel != null) {
                model.addAttribute("logged", true);
                model.addAttribute("name", userModel.getUsername());
                return "draw-page";

        } else {
            model.addAttribute("userDoesntExists", true);
            model.addAttribute("logged", false);
            return "login";
        }
    }

    /* // logout
        return redirect:login*/
    @PostMapping("/register")
    public String registerPost(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("nrIndeksu") String nrIndeksuInString, Model model) {

        Long nrIndeksuInLong = null;
        try {
            nrIndeksuInLong = Long.parseLong(nrIndeksuInString);

            UserModel userModel = userService.findByUserNameAndPasswordAndNrIndeksu(username, password, nrIndeksuInLong);
            if (userModel != null) {
                model.addAttribute("error", "Już istnieje taki uzytkownik!");
                model.addAttribute("savedLogin", false);
            } else {
                userService.save(UserModel.builder().username(username).password(password).nrIndeksu(nrIndeksuInLong).build());
                model.addAttribute("savedLogin", true);
                model.addAttribute("logged", false);
                return "/login";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("NumberFormatException", true);
            model.addAttribute("logged", false);
        } catch (Exception e) {
            model.addAttribute("logged", false);
            model.addAttribute("ConstraintViolationException", true);
        }

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

    @PostMapping("/fileDownload")
    public HttpEntity<byte[]> getFile(@RequestParam("fileName") String fileName, HttpServletResponse response, Model model) {
        try {
            // get your file as InputStream
            final String pathname = filesPath + "/" + fileName + ".pdf";
            System.out.println(pathname);
            InputStream is = FileUtils.openInputStream(new File(pathname));
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName + ".pdf");
            return new HttpEntity<>(IOUtils.toByteArray(is), headers);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
