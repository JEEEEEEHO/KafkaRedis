package com.example.book.springboot.web;

import com.example.book.springboot.service.hosts.HostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HostsApiController {
    private final HostsService hostsService;



}
