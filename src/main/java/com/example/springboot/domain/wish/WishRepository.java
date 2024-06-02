package com.example.springboot.domain.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface WishRepository extends JpaRepository<WishList, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM WishList w WHERE w.userId =:userId AND w.hostNum =:hnum")
    void deleteWishItem(@Param("userId")String userId, @Param("hnum")Long hnum);

    @Query("SELECT w as hostNum FROM WishList w WHERE w.userId =:userId")
    List<WishList> viewWish(@Param("userId")String userId);
}
