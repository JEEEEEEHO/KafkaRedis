package com.example.springboot.service.host;

import com.example.springboot.controller.dto.host.HostListResponseDto;
import com.example.springboot.domain.host.HostRepository;
import com.example.springboot.domain.user.User;
import com.example.springboot.domain.user.UserRepository;
import com.example.springboot.controller.dto.host.HostSaveRequestDto;
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

    // 호스트 리스트
    @Transactional
    public List<HostListResponseDto> findAllDesc() {
        return hostRepository.findAllDesc().stream().map(HostListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public int save(HostSaveRequestDto requestDto) {
       User user = userRepository.findById(requestDto.getId())
               .orElseThrow(()->new IllegalArgumentException("회원없음 id->"+requestDto.getId()));
       user.stsUpdate(1);
        return hostRepository.save(requestDto.toEntity(user)).getHnum();
    }


}
