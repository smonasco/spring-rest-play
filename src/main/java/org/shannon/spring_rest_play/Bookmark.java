package org.shannon.spring_rest_play;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Value
@Entity
public class Bookmark {
    @Id
    @GeneratedValue
    @Wither
    private final Long id;
    @JsonIgnore
    @ManyToOne
    @NonNull
    private final Account account;
    @NonNull
    private final String uri;
    @NonNull
    private final String description;
}
