package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;


    @Column
    private String tagLine;

    @Column
    private String refreshToken;

    @Email(message = "유효한 이메일을 입력해주세요.")
    @NotBlank(message = "Email을 입력해주세요.")
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    public User(String userId, String password, String userName, String email, UserStatus userStatus) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.userStatus = userStatus;
    }

    public void update(ProfileRequestDto requestDto) {
        this.userName = requestDto.getUserName();
        this.tagLine = requestDto.getTagLine();
    }
}
