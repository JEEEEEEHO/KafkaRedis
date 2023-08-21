package com.example.springboot.service.host;

import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private  final HostRepository hostRepository;
    private final UserRepository userRepository;



}
