package com.example.basespringboot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import static jakarta.persistence.GenerationType.AUTO;


@Data
@Entity
@Table(name = "USER")
public class User extends BaseEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = AUTO, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USR_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "STATUS")
    private Integer status;

}
