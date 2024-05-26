package plazadecomidas.users.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    @Query("SELECT u.cellPhoneNumber FROM UserEntity u WHERE u.id = :userId")
    Optional<String> findCellPhoneNumberById(@Param("userId") Long userId);
}
