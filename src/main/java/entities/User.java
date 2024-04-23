package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="user_name")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "username", column = @Column(name = "username", unique = true))
    })
    private Credentials username;
    @Embedded
    private Profile profile;

    private Long joined;
    private boolean deleted;
}
