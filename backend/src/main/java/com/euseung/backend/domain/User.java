package com.euseung.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2,max = 50)
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;

    @Enumerated(value = EnumType.STRING)
    private ERole eRole;
    private String ipAddress;
    private String lastAccess;
    @CreationTimestamp
    private LocalDateTime createDate;

}
