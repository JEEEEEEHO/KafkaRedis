package com.example.springboot.domain.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishRepository extends JpaRepository<WishList, Long> {
    @Query("DELETE FROM WishList w WHERE w.userId =: userId AND w.hostNum =: hnum")
    void deleteWishItem(String userId, Long hnum);
}
