package plazadecomidas.users.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.RoleEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IRoleEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IRoleRepository;
import plazadecomidas.users.domain.model.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleAdapterTest {

    private RoleAdapter roleAdapter;

    private IRoleEntityMapper roleEntityMapper;
    private IRoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleEntityMapper = mock(IRoleEntityMapper.class);
        roleRepository = mock(IRoleRepository.class);
        roleAdapter = new RoleAdapter(roleEntityMapper, roleRepository);
    }

    @Test
    void findById() {
        RoleEntity roleEntity = mock(RoleEntity.class);
        Role role = mock(Role.class);

        when(roleEntityMapper.roleEntityToRole(any(RoleEntity.class))).thenReturn(role);
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(roleEntity));

        roleAdapter.findById(1L);

        verify(roleRepository, times(1)).findById(anyLong());
        verify(roleEntityMapper, times(1)).roleEntityToRole(any(RoleEntity.class));
    }

    @Test
    void findByIdNotFound() {
        RoleEntity roleEntity = mock(RoleEntity.class);

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegistryNotFoundException.class, () -> roleAdapter.findById(1L));

        verify(roleRepository, times(1)).findById(anyLong());
        verify(roleEntityMapper, times(0)).roleEntityToRole(any(RoleEntity.class));
    }
}