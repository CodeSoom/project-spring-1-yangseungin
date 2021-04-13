package com.devactivity.repo;

import com.devactivity.user.User;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * github repository 관련 로직을 처리합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RepoService {
    private final RepoRepository repoRepository;
    private static final String PER_PAGE_100 = "per_page=100";

    /**
     * 유저의 github repository를 조회합니다.
     *
     * @param user 조회할 유저
     * @return 유저의 github repository Set
     */
    public Set<Repo> getReposOrderByStar(User user) {
        return repoRepository.findAllByOwnerOrderByStarCountDesc(user);
    }

    /**
     * 유저의 github repository를 저장합니다.
     *
     * @param user
     */
    public void createRepository(User user) {
        String reposUrl = user.getReposUrl() + "?" + PER_PAGE_100;
        Set<Repo> repos = user.getRepos();

        JSONArray body = callRepositoryApi(reposUrl);

        for (int i = 0; i < body.size(); i++) {
            LinkedHashMap repoInfo = (LinkedHashMap) body.get(i);
            Repo repo = Repo.builder()
                    .owner(user)
                    .name(repoInfo.get("name").toString())
                    .fullName(repoInfo.get("full_name").toString())
                    .about((String) repoInfo.get("description"))
                    .language((String) repoInfo.get("language"))
                    .starCount((Integer) repoInfo.get("stargazers_count"))
                    .forkCount((Integer) repoInfo.get("forks_count"))
                    .url((String) repoInfo.get("html_url"))
                    .build();
            repos.add(repo);
        }
        repoRepository.saveAll(repos);
    }

    /**
     * api를 호출하여 github repository 정보를 가져옵니다.
     *
     * @param reposUrl api를 호출할 url
     * @return github repository 정보
     */
    private JSONArray callRepositoryApi(String reposUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(reposUrl)
                .build(false);

        JSONArray body = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(httpHeaders), JSONArray.class)
                .getBody();
        return body;
    }
}
