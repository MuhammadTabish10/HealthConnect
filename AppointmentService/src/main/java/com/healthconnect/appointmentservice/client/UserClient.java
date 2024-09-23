package com.healthconnect.appointmentservice.client;

import com.healthconnect.commonmodels.dto.DoctorDto;
import com.healthconnect.commonmodels.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", url = "${feign-client.services.urls.user}")
public interface UserClient {
    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/ids/{user-ids}")
    Map<Long, UserDto> getAllUsersByIds(@PathVariable("user-ids") List<Long> ids);
}
