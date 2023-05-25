package ru.kpfu.itis.katargina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.katargina.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query(value = "select s from Subscription s where s.user.email = :email")
    Subscription findByUserEmail(String email);
}
