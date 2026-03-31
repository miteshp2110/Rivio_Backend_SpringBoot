package com.cts.rivio.modules.auth.repository;
import com.cts.rivio.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
