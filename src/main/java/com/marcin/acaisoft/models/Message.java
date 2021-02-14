package com.marcin.acaisoft.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.marcin.acaisoft.jsonviews.Views;
import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.SASI;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("messages")
public class Message{
    @Column
    @JsonView(Views.Internal.class)
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID id;
    @Column
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Email
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @JsonView(Views.Public.class)
    private String email;
    @Column
    @NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 2, max = 32, message = "Title length should be between 2 and 32 character")
    @JsonView(Views.Public.class)
    private String title;
    @Column
    @NotNull(message = "Content cannot be null")
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 2, max = 256, message = "Message length should be between 2 and 256 character")
    @JsonView(Views.Public.class)
    private String content;
    @Column
    @NotNull(message = "Magic number cannot be null")
    @SASI
    @JsonView(Views.Public.class)
    @JsonProperty("magic_number")
    private Integer magicNumber;
}
