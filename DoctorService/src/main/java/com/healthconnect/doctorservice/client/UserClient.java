package com.healthconnect.doctorservice.client;

import com.healthconnect.commonmodels.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign-client.services.urls.user}")
public interface UserClient {
    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}
