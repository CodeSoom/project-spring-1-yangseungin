package com.devactivity.repo;

import com.devactivity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface RepoRepository extends JpaRepository<Repo, Long> {
    Set<Repo> findAllByOwnerOrderByStarCountDesc(User user);

    List<Repo> findTop10AllByOrderByStarCountDesc();

    @Query("SELECT SUM(r.starCount) FROM Repo r WHERE r.owner.id = ?1")
    Integer getTotalStarCount(Long id);
}
