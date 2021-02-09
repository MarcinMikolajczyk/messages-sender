package com.marcin.acaisoft.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.marcin.acaisoft.jsonviews.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Getter @Setter
@ToString
@NoArgsConstructor
@Table("messages")
public class Message{
    @Column
    @PrimaryKey
    @JsonView(Views.Internal.class)
    private UUID id;
    @Column
    @NotNull(message = "Email cannot be null")
    @Email
    @JsonView(Views.Public.class)
    private String email;
    @Column
    @NotNull(message = "Title cannot be null")
    @Size(min = 2, max = 32, message = "Title length should be between 2 and 32 character")
    @JsonView(Views.Public.class)
    private String title;
    @Column
    @NotNull(message = "Content cannot be null")
    @Size(min = 2, max = 256, message = "Message length should be between 2 and 256 character")
    @JsonView(Views.Public.class)
    private String content;
    @Column
    @NotNull(message = "Magic number cannot be null")
    @JsonView(Views.Public.class)
    @JsonProperty("magic_number")
    private Integer magicNumber;
}
