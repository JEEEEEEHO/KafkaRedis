package com.example.book.springboot.web;

import com.example.book.springboot.service.hosts.HostsService;
import com.example.book.springboot.web.dto.hosts.HostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HostsApiController {
    private final HostsService hostsService;

    @PostMapping("/api/v1/hosts")
    public int save(@RequestBody HostsSaveRequestDto requestDto){
        return hostsService.save(requestDto);
    }
}
