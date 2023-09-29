package com.enigma.resttemplate.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostResponse {
    private Integer idPost;
    private Integer userIdPost;
    private String titlePost;
    private String bodyPost;
}
