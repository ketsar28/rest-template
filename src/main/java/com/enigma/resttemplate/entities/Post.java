package com.enigma.resttemplate.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "m_posts")
public class Post {
    @Id
    private Integer id;
    private String title;
    private String body;
    private Integer userId;
}
