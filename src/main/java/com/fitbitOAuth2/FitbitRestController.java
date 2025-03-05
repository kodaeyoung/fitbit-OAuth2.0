package com.fitbitOAuth2;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class FitbitRestController {
    private final WebClient webClient;

    public FitbitRestController(WebClient.Builder webClientBuilder,
                                @Value("${fitbit.client-id}") String clientId,
                                @Value("${fitbit.client-secret}") String clientSecret,
                                @Value("${fitbit.token-uri}") String tokenUri,
                                @Value("${fitbit.redirect-uri}") String redirectUri) {
        this.webClient = webClientBuilder.build();
    }

    // 사용자의 날짜별 활동 요약 가져오기
    @GetMapping("/api/activities")
    public Mono<String> getActivities(@RequestParam("date") String date, HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");
        if (accessToken == null) {
            return Mono.just("Access token is missing.");
        }
        System.out.println("엑세스토큰: " + accessToken);
        return webClient.get()
                .uri("https://api.fitbit.com/1/user/-/activities/date/" + date + ".json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(String.class);
    }


    @GetMapping("/api/profile")
    public Mono<String> getProfile(HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");
        if (accessToken == null) {
            return Mono.just("Access token is missing.");
        }
        return webClient.get()
                .uri("https://api.fitbit.com/1/user/-/profile.json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(String.class);
    }


}
