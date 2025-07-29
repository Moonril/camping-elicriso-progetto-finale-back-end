package it.epicode.camping_elicriso_progetto_finale_back_end.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//@Configuration
//@EnableWebSecurity // abilita la clase a essere responsabile della sicurezza dei servizi
//@EnableMethodSecurity // abilita l'utilizzo della preautorizzazione direttamente sui metodi dei controller
//public class SecurityConfig {
//
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        // primo metodo serve per creare in automatico una pagina di login, disabled
//        httpSecurity.formLogin(http->http.disable());
//        //csrf serve per evitare la possibilità di utilizzi di sessioni lasciate aperte
//        httpSecurity.csrf(http->http.disable());
//        // non ci interessa perchè i servizi rest non hanno sessione
//        httpSecurity.sessionManagement(http->http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        // non ho capito. serve per bloccare richiesta da indirizzi ip e porte diversi da dove si trova il servizio
//        httpSecurity.cors(Customizer.withDefaults());
//
//
//
//        // prevede la approvazione o negazione di un servizio endpoint
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/auth/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/users/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/me").authenticated());
//
//
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/customers/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/accommodations/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/camping/bookings/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/restaurant/reservations/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/accommodations/**").permitAll());
//
//
//
//
//        //httpSecurity.authorizeHttpRequests(http->http.anyRequest().denyAll());
//
//        return httpSecurity.build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(10); // verrà applicato 10 volte
//    }
//
//    // cors
//    @Bean//permette di abilitare l'accesso al servizio anche da parte di server diversi da quello su cui risiede
//    //il servizio. In questo caso ho abilitato tutti i server ad accedere a tutti i servizi
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        return source;
//    }
//
//}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(http -> http.disable())
                .csrf(http -> http.disable())
                .sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/api/me").authenticated()
                        .requestMatchers("/customers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/accommodations/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/accommodations/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/accommodations/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/camping/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/camping/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/camping/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/camping/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/restaurant/reservations/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/restaurant/reservations/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/restaurant/reservations/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/restaurant/reservations/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
