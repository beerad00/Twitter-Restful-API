package dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    //User id
    private int author;

    private Long posted;

    private boolean deleted;
    private String content;
    //Tweet id
    private int inReplyTo;
    //Tweet id
    private int repostOf;
}
