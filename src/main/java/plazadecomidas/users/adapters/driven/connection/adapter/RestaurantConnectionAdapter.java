package plazadecomidas.users.adapters.driven.connection.adapter;

import lombok.AllArgsConstructor;
import plazadecomidas.users.adapters.driven.connection.feign.IEmployeeFeignClient;
import plazadecomidas.users.adapters.driven.connection.mapper.IEmployeeRequestMapper;
import plazadecomidas.users.adapters.driven.connection.request.AddEmployeeRequest;
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
            System.out.println("Si se pudo");
        } catch (Exception e) {
            System.out.println("No se pudo");
            e.printStackTrace();
        }
    }
}
