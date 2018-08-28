package org.shannon.spring_rest_play.repositories.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.shannon.spring_rest_play.Account;
import org.shannon.spring_rest_play.Bookmark;
import org.shannon.spring_rest_play.repositories.AccountRepository;
import org.shannon.spring_rest_play.repositories.BookmarkRepository;
import org.shannon.spring_rest_play.repositories.InMemoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class InMemoryBookmarkRepository extends InMemoryRepository<Bookmark, Long> implements BookmarkRepository {
    private final NavigableMap<String, Bookmark> bookmarksByDescription = new TreeMap<>();
    private final AccountRepository accounts;
    private AtomicLong lastId = new AtomicLong(0);

    @Override
    protected Bookmark withGeneratedValues(Bookmark entity) {
        return entity.withId(lastId.incrementAndGet());
    }

    @Override
    protected void additionalLookupsAdd(Bookmark entity) {
        // TODO: this implementation assumes some uniqueness which isn't enforced. Probably also an issue for the InMemoryAccountRepo
        bookmarksByDescription.put(entity.getDescription(), entity);
    }

    @Override
    protected void additionalLookupsRemove(Bookmark entity) {
        bookmarksByDescription.remove(entity.getDescription());
    }

    @Override
    protected void additionalLookupsClear() {
        bookmarksByDescription.clear();
    }

    @Override
    protected Long getId(Bookmark entity) {
        return entity.getId();
    }

    @Override
    public Collection<Bookmark> findByAccountUsername(String username) {
        return accounts.findByUsername(username).map(Account::getBookmarks)
                .orElse(Collections.emptySet());
    }

    @Override
    public Iterable<Bookmark> findAll(Sort sort) {
        val optionalSort = Optional.ofNullable(sort);
        val length = optionalSort.map(s -> Arrays.asList(s).size()).orElse(0);
        val direction = Optional.ofNullable(sort)
                .map(s -> s.getOrderFor("Description"))
                .map(Sort.Order::getDirection);
        if (!direction.isPresent() && length != 0) {
            throw new IllegalArgumentException("Can only sort Bookmarks on `Description`");
        }
        return
                (
                        direction.orElse(Sort.Direction.ASC) == Sort.Direction.ASC
                        ? bookmarksByDescription
                        : bookmarksByDescription.descendingMap()
                ).values();
    }

    @Override
    public Page<Bookmark> findAll(Pageable pageable) {
        return new PageImpl<>(
                StreamSupport.stream(
                        findAll(pageable.getSort())
                                .spliterator()
                        , false
                )
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .collect(Collectors.toList())
                , pageable
                , count()
        );
    }
}
