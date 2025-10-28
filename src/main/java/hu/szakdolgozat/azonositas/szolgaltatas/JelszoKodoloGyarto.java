// ez itt a jelszo kodolo bean (BCrypt)
package hu.szakdolgozat.azonositas.szolgaltatas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JelszoKodoloGyarto {

    @Bean
    public PasswordEncoder jelszoKodolo() {
        return new BCryptPasswordEncoder();
    }
}
