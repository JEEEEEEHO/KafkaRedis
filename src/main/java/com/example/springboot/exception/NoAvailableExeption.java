package com.example.springboot.exception;

public class NoAvailableExeption extends RuntimeException {
    public NoAvailableExeption(String hnum) {
        super("호스트번호 : "+hnum+"의 예약 가능 인원 부족");
    }
}
