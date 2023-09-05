package se.inera.intyg.minaintyg.testability;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/testability")
public class TestabilityController {
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        invalidateSessionAndClearContext(request);
    }

    private void invalidateSessionAndClearContext(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
