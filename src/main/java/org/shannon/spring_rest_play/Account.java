package org.shannon.spring_rest_play;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Value
public class Account {
    @Id
    @GeneratedValue
    @Wither
    private final Long id;
    @NonNull
    private final String username;
    @NonNull
    @JsonIgnore
    private final String password;
    @OneToMany(mappedBy = "account")
    @NonNull
    @ToString.Exclude
    private final Set<Bookmark> bookmarks = new HashSet<>();

}
