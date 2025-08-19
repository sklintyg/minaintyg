package se.inera.intyg.minaintyg.user;

import static se.inera.intyg.minaintyg.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.logging.PerformanceLogging;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @GetMapping
  @PerformanceLogging(eventAction = "retrieve-user", eventType = EVENT_TYPE_ACCESSED)
  public UserResponseDTO getUser() {
    return userService.getLoggedInUser().map(minaIntygUser ->
        UserResponseDTO.builder()
            .personId(minaIntygUser.getUserId())
            .personName(minaIntygUser.getUserName())
            .loginMethod(minaIntygUser.getLoginMethod())
            .build()
    ).orElse(null);
  }
}
