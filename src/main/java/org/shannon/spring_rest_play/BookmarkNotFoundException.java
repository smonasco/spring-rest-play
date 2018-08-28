package org.shannon.spring_rest_play;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(Long bookmarkId) {
        super("Could not find bookmark '" + bookmarkId + "'.");
    }
}
