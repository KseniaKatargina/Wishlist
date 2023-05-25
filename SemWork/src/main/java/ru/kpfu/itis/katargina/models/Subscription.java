package ru.kpfu.itis.katargina.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "subscriptions")
    private List<Category> categories = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    public Subscription(long id){
        this.id = id;
    }
}
