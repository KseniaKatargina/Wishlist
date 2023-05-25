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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img", columnDefinition = "varchar")
    private String img;

    @Column(name = "text", unique = true)
    private String text;

    @ManyToMany(mappedBy = "products")
    private List<Wishlist> wishlists = new ArrayList<>();

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    public Product(long id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", text='" + text + '\'' +
                ", wishlists=" + wishlists +
                ", categories=" + categories.stream().map(category -> category.getName().toString()).toList() +
                '}';
    }
}
