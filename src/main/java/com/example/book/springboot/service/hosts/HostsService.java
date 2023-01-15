package com.example.book.springboot.service.hosts;

import com.example.book.springboot.domain.hosts.HostsRepository;
import com.example.book.springboot.domain.user.User;
import com.example.book.springboot.domain.user.UserRepository;
import com.example.book.springboot.web.dto.hosts.HostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class HostsService {
    private  final HostsRepository hostsRepository;
    private final UserRepository userRepository;

    @Transactional
    public int save(HostsSaveRequestDto requestDto) {
       User user = userRepository.findById(requestDto.getId())
               .orElseThrow(()->new IllegalArgumentException("회원없음 id->"+requestDto.getId()));
       user.stsUpdate(1);
        return hostsRepository.save(requestDto.toEntity(user)).getHnum();
    }
}
