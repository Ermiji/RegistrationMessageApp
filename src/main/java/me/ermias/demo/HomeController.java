package me.ermias.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController{

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("messages",messageRepository.findAll());

        if(userService.getUser()!=null){

            model.addAttribute("user_id",userService.getUser().getId());
        }


        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "loginform";
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registerform";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registerform";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/";
    }



    @RequestMapping("/addMessage")
    public String addMessage(Model model){

        model.addAttribute("message", new Message());
        model.addAttribute("messages", messageRepository.findAll());

        return "message";

    }

    @PostMapping("/addMessage")
    public String processForm(@Valid @ModelAttribute("message") Message message,
                              @RequestParam("file") MultipartFile file, BindingResult result ){


        if(file.isEmpty()){
            return "redirect:/";
        }

        try{
            Map uploadresult = cloudinaryConfig.upload(file.getBytes(), ObjectUtils.asMap("resourcetype","auto"));
            message.setImg(uploadresult.get("url").toString());
            message.setUser(userService.getUser());
            messageRepository.save(message);

        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/addMessage";
        }
        if(result.hasErrors()){
            return "message";
        }

        message.setUser(userService.getUser());
        messageRepository.save(message);

        return "redirect:/";
    }

    @RequestMapping("/myprofile/{id}")
    public String findProfile(@PathVariable("id") long id, Model model){

        model.addAttribute("user", userRepository.findById(id).get());
        model.addAttribute("user",userRepository.findById(id).get());

        return "profile";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messageRepository.findById(id));
        model.addAttribute("user",userRepository.findById(id).get());

        if(userService.getUser()!=null){
            model.addAttribute("user_id",userService.getUser().getId());
        }
        return "showform";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messageRepository.findById(id));
        model.addAttribute("user",userRepository.findById(id));
        return  "message";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }


}
