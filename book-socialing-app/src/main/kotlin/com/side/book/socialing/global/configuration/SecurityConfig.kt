package com.side.book.socialing.global.configuration

import com.side.book.socialing.domain.auth.service.CustomOAuth2UserService
import com.side.book.socialing.global.security.filter.JwtAuthenticationFilter
import com.side.book.socialing.global.security.handler.OAuth2SuccessHandler
import com.side.book.socialing.global.security.service.JwtAuthService
import com.side.book.socialing.global.utils.log
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@ConditionalOnProperty(
    name = ["spring.auth.active"],
    havingValue = "true",
    matchIfMissing = true
)
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val jwtAuthService: JwtAuthService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/", "/oauth2/**", "/login/oauth2/code/**").permitAll()
                    .requestMatchers("/api/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(oAuth2SuccessHandler)
                    .userInfoEndpoint { it.userService(customOAuth2UserService) }
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { request, response, authException ->
                    log.error("인증 실패 - URI: ${request.requestURI}, 예외: ${authException.message}", authException)

                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json"
                    response.characterEncoding = "UTF-8"
                    response.writer.write("""{"error": "Unauthorized", "message": "인증이 필요합니다"}""")
                }
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtAuthService),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}
