package plazadecomidas.users.adapters.driven.connection.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import plazadecomidas.users.adapters.driven.connection.request.AddEmployeeRequest;

@FeignClient(name = "employee", url = "http://localhost:8080/employees")
public interface IEmployeeFeignClient {

    @PostMapping("register")
    public ResponseEntity<Void> registerEmployee(@RequestHeader(value = "Authorization") String token, @RequestBody AddEmployeeRequest addEmployeeRequest);
}
