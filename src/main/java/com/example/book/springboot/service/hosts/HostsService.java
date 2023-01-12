package com.example.book.springboot.service.hosts;

import com.example.book.springboot.domain.hosts.HostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HostsService {
    private  final HostsRepository hostsRepository;
}
