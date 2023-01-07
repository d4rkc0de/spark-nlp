package com.d4rkc0de.stackoverflow_parser.services;

import com.d4rkc0de.stackoverflow_parser.entities.Question;
import com.d4rkc0de.stackoverflow_parser.entities.QuestionsResponse;
import com.d4rkc0de.stackoverflow_parser.entities.User;
import com.d4rkc0de.stackoverflow_parser.entities.UserResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.d4rkc0de.stackoverflow_parser.common.Constants.BASE_QUERY;
import static com.d4rkc0de.stackoverflow_parser.common.Constants.BASE_URL;

@Service
public class ApiParser {

    RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public List<Question> getQuestions(String tagName, String page, String pageSize) {
        return Objects.requireNonNull(restTemplate.getForEntity(BASE_URL + BASE_QUERY + "&tagged=" + tagName + "&page=" + page + "&pagesize=" + pageSize,
                QuestionsResponse.class).getBody()).getItems();
    }

    public User getUserInfo(String userId) {
        return Objects.requireNonNull(restTemplate.getForEntity(BASE_URL + "users/" + userId + "?site=stackoverflow",
                UserResponse.class).getBody()).getItems().get(0);
    }
}
