package ch.uzh.ifi.seal.soprafs20.security;

import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            userService.logoutUser(authentication.getName());
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
