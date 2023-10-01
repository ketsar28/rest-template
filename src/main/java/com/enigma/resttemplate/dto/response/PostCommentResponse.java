package com.enigma.resttemplate.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostCommentResponse {
    private Integer idPost;
    private Integer userIdPost;
    private String namePost;
    private String emailPost;
    private String bodyPost;
}
