package org.shannon.spring_rest_play;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Value
public class Greeting {
    @Id
    @GeneratedValue
    private final long id;
    private final @NonNull String content;
}
