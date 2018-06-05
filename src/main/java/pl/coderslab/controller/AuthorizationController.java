package pl.coderslab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.model.User;
import pl.coderslab.repository.UserRepository;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String demo() {
        return "auth/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String demo(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null){
            HttpSession session = request.getSession();

            session.setAttribute("username", user.getUsername());
            session.setAttribute("authorized", true);
            session.setAttribute("role", "customer");

            session.setMaxInactiveInterval(30*60);

            return "user authorized";
        }
        else
        {
            return "user not authorized";
        }
    }

    @GetMapping("/register")
    public String registerForm(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request){

        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("customer");

        userRepository.save(user);

        return "auth/after-register";
    }
}