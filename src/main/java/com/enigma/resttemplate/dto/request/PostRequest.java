package com.enigma.resttemplate.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostRequest {
    private Integer idPost;
    private Integer userIdPost;
    private String titlePost;
    private String bodyPost;
}
