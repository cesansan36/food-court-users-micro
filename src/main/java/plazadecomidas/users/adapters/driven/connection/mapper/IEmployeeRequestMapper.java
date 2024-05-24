package plazadecomidas.users.adapters.driven.connection.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driven.connection.request.AddEmployeeRequest;

@Mapper(componentModel = "spring")
public interface IEmployeeRequestMapper {

    @Mapping(target = "idOwner", source = "ownerId")
    @Mapping(target = "idEmployee", source = "employeeId")
    @Mapping(target = "idRestaurant", source = "restaurantId")
    AddEmployeeRequest toEmployeeRequest(Long ownerId, Long employeeId, Long restaurantId);
}
