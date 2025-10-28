// ez itt a minimalis biztonsagi beallitas + CORS engedely a Next.js-nek
package hu.szakdolgozat.azonositas.biztonsag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class BiztonsagBeallitas {

    @Bean
    public SecurityFilterChain biztonsagiLanc(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(eng -> eng
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    // ez itt engedi, hogy a http://localhost:3000-rol hivhasd a backendet fejlesztesben
    @Bean
    public CorsConfigurationSource corsForras() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://100.111.244.151:3000" // <- a “Network” URL
        ));
        cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cors.setAllowedHeaders(List.of("Content-Type","Authorization"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource forras = new UrlBasedCorsConfigurationSource();
        forras.registerCorsConfiguration("/**", cors);
        return forras;
    }
}
