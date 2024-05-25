package plazadecomidas.users.adapters.driven.connection.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.users.adapters.driven.connection.exception.MicroserviceFailedInteractionException;
import plazadecomidas.users.adapters.driven.connection.feign.IEmployeeFeignClient;
import plazadecomidas.users.adapters.driven.connection.mapper.IEmployeeRequestMapper;
import plazadecomidas.users.adapters.driven.connection.request.AddEmployeeRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantConnectionAdapterTest {

    private RestaurantConnectionAdapter restaurantConnectionAdapter;

    private IEmployeeFeignClient employeeFeignClient;
    private IEmployeeRequestMapper employeeRequestMapper;

    @BeforeEach
    void setUp() {
        employeeFeignClient = mock(IEmployeeFeignClient.class);
        employeeRequestMapper = mock(IEmployeeRequestMapper.class);

        restaurantConnectionAdapter = new RestaurantConnectionAdapter(employeeFeignClient, employeeRequestMapper);
    }

    @Test
    void saveUserSuccessful() {
        // Given
        String ownerToken = "ownerToken";
        Long ownerId = 1L;
        Long employeeId = 2L;
        Long restaurantId = 3L;
        AddEmployeeRequest request = mock(AddEmployeeRequest.class);

        // When
        when(employeeRequestMapper.toEmployeeRequest(anyLong(), anyLong(), anyLong())).thenReturn(request);
        restaurantConnectionAdapter.saveUser(ownerToken, ownerId, employeeId, restaurantId);

        // Then
        verify(employeeFeignClient, times(1)).registerEmployee(ownerToken, request);
        verify(employeeRequestMapper, times(1)).toEmployeeRequest(anyLong(), anyLong(), anyLong());
    }

    @Test
    void saveUserFail() {
        // Given
        String ownerToken = "ownerToken";
        Long ownerId = 1L;
        Long employeeId = 2L;
        Long restaurantId = 3L;
        AddEmployeeRequest request = mock(AddEmployeeRequest.class);

        // When
        when(employeeRequestMapper.toEmployeeRequest(anyLong(), anyLong(), anyLong())).thenReturn(request);
        when(employeeFeignClient.registerEmployee(ownerToken, request)).thenThrow(new RuntimeException("Error"));


        assertThrows(MicroserviceFailedInteractionException.class, () -> restaurantConnectionAdapter.saveUser(ownerToken, ownerId, employeeId, restaurantId));

        // Then
        verify(employeeFeignClient, times(1)).registerEmployee(ownerToken, request);
        verify(employeeRequestMapper, times(1)).toEmployeeRequest(anyLong(), anyLong(), anyLong());
    }
}