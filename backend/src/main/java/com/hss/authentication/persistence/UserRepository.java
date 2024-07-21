package com.hss.authentication.persistence;

import com.hss.authentication.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
            SELECT usr FROM User usr
            WHERE :searchText IS NULL
                OR CONCAT(usr.id, ' ', usr.username, ' ', usr.email) LIKE CONCAT('%', LOWER(CAST(:searchText AS text)), '%')""")
    Page<User> findBySearchText(@Param("searchText") String searchText, Pageable pageable);
}