package se.inera.intyg.minaintyg.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final MinaIntygUserService minaIntygUserService;

  @GetMapping
  public MinaIntygUser getUser() {
    return minaIntygUserService.getUser().orElse(null);
  }
}
