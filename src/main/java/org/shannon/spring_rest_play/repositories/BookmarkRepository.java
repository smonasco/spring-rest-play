package org.shannon.spring_rest_play.repositories;

import org.shannon.spring_rest_play.Bookmark;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface BookmarkRepository extends PagingAndSortingRepository<Bookmark, Long> {
    Collection<Bookmark> findByAccountUsername(final String username);
}
