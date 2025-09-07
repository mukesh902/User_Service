package com.userApi.User_Registration_Module.repository;

import com.userApi.User_Registration_Module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByCityNameAndUserType(String cityName, String userType);
    List<User> findByCityName(String cityName);

    List<User> findByUserType(String userType);

    @Query(value = "SELECT * FROM my_db.user u WHERE " +
            "(:cityName IS NULL OR :cityName = '' OR LOWER(u.city_name) = LOWER(:cityName)) " +
            "AND (:userType IS NULL OR :userType = '' OR LOWER(u.user_type) = LOWER(:userType))",
            nativeQuery = true)
    List<User> searchUsersWithFilters(@Param("cityName") String cityName, @Param("userType") String userType);


}
