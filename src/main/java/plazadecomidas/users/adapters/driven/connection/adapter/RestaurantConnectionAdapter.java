package plazadecomidas.users.adapters.driven.connection.adapter;

import lombok.AllArgsConstructor;
import plazadecomidas.users.adapters.driven.connection.exception.MicroserviceFailedInteractionException;
import plazadecomidas.users.adapters.driven.connection.feign.IEmployeeFeignClient;
import plazadecomidas.users.adapters.driven.connection.mapper.IEmployeeRequestMapper;
import plazadecomidas.users.adapters.driven.connection.request.AddEmployeeRequest;
import plazadecomidas.users.adapters.driven.connection.util.ConnectionConstants;
import plazadecomidas.users.domain.secondaryport.IRestaurantConnectionPort;

@AllArgsConstructor
public class RestaurantConnectionAdapter implements IRestaurantConnectionPort {

    private final IEmployeeFeignClient employeeFeignClient;
    private final IEmployeeRequestMapper employeeRequestMapper;

    @Override
    public void saveUser(String ownerToken, Long ownerId, Long employeeId, Long restaurantId) {
        AddEmployeeRequest employee = employeeRequestMapper.toEmployeeRequest(ownerId, employeeId, restaurantId);

        try {
            employeeFeignClient.registerEmployee(ownerToken, employee);
        } catch (Exception e) {
            throw new MicroserviceFailedInteractionException(
                    ConnectionConstants.FAILED_EMPLOYEE_RESTAURANT_LINK.formatted(
                            e.getMessage()));
        }
    }
}
