package dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextDto {
    private Tweet target;

    List<Tweet> before;
    List<Tweet> after;
}
