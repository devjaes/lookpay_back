package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(value = "select u.id as id, email, u.name as name, password, phone_number, address, rol, c.name as city, p.name as province, c.id as city_id from users u inner join cities c on c.id = u.city_id inner join provinces p on p.id = c.province_id where email=:email", nativeQuery = true)
    public UserModel getByEmail(@Param("email") String email);

    @Query(value = "select u.id as id, email, u.name as name, password, phone_number, address, rol, c.name as city, p.name as province, c.id as city_id from users u inner join cities c on c.id = u.city_id inner join provinces p on p.id = c.province_id where u.id=:userId", nativeQuery = true)
    public UserModel getByUserId(@Param("userId") Long id);
}
