package com.devactivity.feed;

import com.devactivity.user.User;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public Set<Feed> createFeed(User user, String rssUrl) throws IOException, FeedException {
        SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(rssUrl)));
        List<SyndEntry> entries = syndFeed.getEntries();
        Set<Feed> feeds = user.getFeeds();
        entries.stream().forEach(syndEntry -> {
            Feed feed = Feed.builder()
                    .author(user)
                    .title(syndEntry.getTitle())
                    .createdDate(syndEntry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .link(syndEntry.getLink())
                    .build();
            feeds.add(feed);
        });
        feedRepository.saveAll(feeds);

        return feedRepository.findAllByAuthorOrderByCreatedDateDesc(user);
    }

    public void deleteFeed(User user) {
        Set<Feed> allByUserId = getFeeds(user);
        feedRepository.deleteAll(allByUserId);
    }

    public Set<Feed> getFeeds(User user) {
        return feedRepository.findAllByAuthorOrderByCreatedDateDesc(user);
    }

    public List<Feed> getLastFiveFeed() {
        return feedRepository.findTop5AllByOrderByCreatedDateDesc();
    }
}
