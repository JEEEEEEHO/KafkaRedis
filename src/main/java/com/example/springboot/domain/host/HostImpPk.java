package com.example.springboot.domain.host;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HostImpPk implements Serializable {
    private Long hostImg_turn;
    private String id;
}
