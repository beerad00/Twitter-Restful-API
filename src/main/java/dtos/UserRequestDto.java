package dtos;

import entities.Credentials;
import entities.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {
    private Credentials username;

    private Profile profile;

    private Long joined;
    private boolean deleted;
}

