package plazadecomidas.users.adapters.driven.jpa.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.RoleEntity;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
}
