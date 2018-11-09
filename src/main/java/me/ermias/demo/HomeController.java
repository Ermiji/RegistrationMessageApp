package me.ermias.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("messages",messageRepository.findAll());

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

        return "message";

    }

    @PostMapping("/addMessage")
    public String processForm(@Valid @ModelAttribute("message") Message message, BindingResult result ){

        if(result.hasErrors()){
            return "message";
        }
        messageRepository.save(message);

        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messageRepository.findById(id).get());
        return "showform";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messageRepository.findById(id));
        return  "message";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }





}
