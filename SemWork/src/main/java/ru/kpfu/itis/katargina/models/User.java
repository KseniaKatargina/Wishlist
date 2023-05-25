package ru.kpfu.itis.katargina.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import ru.kpfu.itis.katargina.utils.Role;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


@DynamicUpdate
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    public User(long id){
        this.id = id;
    }

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String birthday;

    @Transient
    private int years;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private Subscription subscription;
}