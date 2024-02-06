package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.entities.User;
import com.devsuperior.DSCatalog.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT tb_user.email AS username, tb_user.password, authority, tb_role.id AS roleId " +
            "FROM tb_user " +
            "INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id " +
            "INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id " +
            "WHERE tb_user.email = :username")
    List<UserDetailsProjection> searchByUsername(String username);



}
