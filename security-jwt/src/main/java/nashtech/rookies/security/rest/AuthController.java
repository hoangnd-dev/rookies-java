package nashtech.rookies.security.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nashtech.rookies.security.dto.JwtToken;
import nashtech.rookies.security.dto.SignInDto;
import nashtech.rookies.security.jwt.TokenProvider;

@RestController
public class AuthController extends V1Rest {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider         tokenProvider;

    public AuthController (AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping(
        path = "/auth/signin",
        consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<JwtToken> signIn (@RequestBody @Valid SignInDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authUser         = authenticationManager.authenticate(usernamePassword);
        var accessToken      = tokenProvider.generateAccessToken((UserDetails) authUser.getPrincipal());
        return ResponseEntity.ok(new JwtToken(accessToken));
    }


    @GetMapping("/me")
    ResponseEntity<String> me (Authentication authentication) {
        if ( authentication.getPrincipal() instanceof UserDetails user ) {
            return ResponseEntity.ok(user.getUsername());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/admin")
    ResponseEntity<String> admin () {
        return ResponseEntity.ok("You are admin");
    }

}
