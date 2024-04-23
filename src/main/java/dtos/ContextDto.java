package dtos;

import com.cooksys.social_media_1.entities.Tweet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {
    private Tweet target;

    List<Tweet> before;
    List<Tweet> after;
}
