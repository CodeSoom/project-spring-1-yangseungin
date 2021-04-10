package com.devactivity.repo;

import com.devactivity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface RepoRepository extends JpaRepository<Repo, Long> {
    Set<Repo> findAllByOwnerOrderByStarCountDesc(User user);
}
