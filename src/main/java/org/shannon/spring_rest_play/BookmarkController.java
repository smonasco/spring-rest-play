package org.shannon.spring_rest_play;

import lombok.val;
import org.shannon.spring_rest_play.repositories.AccountRepository;
import org.shannon.spring_rest_play.repositories.BookmarkRepository;
import org.shannon.spring_rest_play.repositories.impl.InMemoryAccountRepository;
import org.shannon.spring_rest_play.repositories.impl.InMemoryBookmarkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/{userId}/bookmarks")
public final class BookmarkController {
    private final AccountRepository accountRepository = new InMemoryAccountRepository();
    private final BookmarkRepository bookmarkRepository = new InMemoryBookmarkRepository(accountRepository);

    @GetMapping
    Collection<Bookmark> readBookmarks(@PathVariable String userId) {
        this.validateUser(userId);

        return this.bookmarkRepository.findByAccountUsername(userId);
    }

    @PostMapping
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark bookmark) {
        this.validateUser(userId);

        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    val result = bookmarkRepository.save(new Bookmark(
                            null,
                            account,
                            bookmark.getUri()
                            , bookmark.getDescription()));

                    val location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.noContent().build());

    }

    @GetMapping("/{bookmarkId}")
    Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
        this.validateUser(userId);

        return Optional.ofNullable(this.bookmarkRepository.findOne(bookmarkId))
                .orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }

}
