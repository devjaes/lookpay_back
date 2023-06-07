package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(value = "select u.id as id, email, u.name as name, password, phoneNumber, address, rol from users u where email=:email", nativeQuery = true)
    public UserModel getByEmail(@Param("email") String email);
}
