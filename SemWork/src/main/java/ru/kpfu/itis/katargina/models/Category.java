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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @ManyToMany
    private List<Subscription> subscriptions = new ArrayList<>();

    public Category(long id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subscriptions=" + subscriptions.stream().map(subscription -> subscription.getId().toString()).toList() +
                ", products=" + products.stream().map(product -> product.getId().toString()).toList() +
                '}';
    }

}
