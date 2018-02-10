package kolos.controllers;

import kolos.models.TaskModel;
import kolos.models.UserModel;
import kolos.services.TaskService;
import kolos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainControllerKolos {

    private final UserService userService;
    private final TaskService taskService;
    private final String tasksPath = "/home/rafalzel/projects/strony-www/server/files";
    private final String uploadPath = "/home/rafalzel/projects/strony-www/server/filesUploaded";

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
        TaskModel taskModel = taskService.findByUserId(userModel.getId());

        if (userModel != null) {
            model.addAttribute("logged", true);
            model.addAttribute("name", userModel.getUsername());
            model.addAttribute("userId", userModel.getId());
            if(taskModel != null){
                model.addAttribute("randomFile", taskModel.getFileName());
                model.addAttribute("isUploaded", taskModel.isUploaded());
            }
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

    @PostMapping("/losuj")
    public String randomFile(@RequestParam("userId") Long userId, Model model) throws IOException {
        List<String> filesInFolder = Files.walk(Paths.get(tasksPath))
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(s -> FilenameUtils.removeExtension(s.getFileName().toString()))
                .collect(Collectors.toList());
        Random randomizer = new Random();
        String random = filesInFolder.get(randomizer.nextInt(filesInFolder.size()));
        model.addAttribute("logged", true);
        model.addAttribute("randomFile", random);
        final TaskModel requestModel = new TaskModel();
        requestModel.setUserId(userId);
        requestModel.setStartTime(new Date());
        requestModel.setEndTime(new Date());
        requestModel.setUploaded(false);
        requestModel.setFileName(random);
        taskService.save(requestModel);
        return "draw-page";
    }

    @PostMapping("/fileDownload")
    public HttpEntity<byte[]> getFile(@RequestParam("fileName") String fileName, Model model) {
        try {
            final String pathname = tasksPath + "/" + fileName + ".pdf";
            System.out.println(pathname);
            InputStream is = FileUtils.openInputStream(new File(pathname));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/octet-stream");
            headers.set("Content-Disposition", "attachment");
            headers.setContentDispositionFormData("attachment", fileName + ".pdf");
            return new HttpEntity<>(IOUtils.toByteArray(is), headers);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PostMapping("/fileUpload")
    public String handleFileUpload(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if(file != null && file.getSize() > 0   ) {
            TaskModel taskModel = taskService.findByUserId(userId);
            taskModel.setUploaded(true);
            final String pathname = uploadPath + "/" + userId + "/" + file.getOriginalFilename();
            System.out.println("saved as: " + pathname);
            File targetFile = new File(pathname);
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        }
        model.addAttribute("logged", false);
        return "login";
    }
}
