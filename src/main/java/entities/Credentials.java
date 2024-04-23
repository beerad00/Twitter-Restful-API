package entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
}
