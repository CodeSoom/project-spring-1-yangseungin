package com.devactivity.feed;

import com.devactivity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Set<Feed> findAllByAuthorOrderByCreatedDateDesc(User userId);
    List<Feed> findTop5AllByOrderByCreatedDateDesc();
}
