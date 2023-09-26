package se.inera.intyg.minaintyg.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @GetMapping
  public UserResponseDTO getUser() {
    return userService.getLoggedInUser().map(minaIntygUser ->
        UserResponseDTO.builder()
            .personId(minaIntygUser.getPersonId())
            .personName(minaIntygUser.getPersonName())
            .loginMethod(minaIntygUser.getLoginMethod())
            .build()
    ).orElse(null);
  }
}
