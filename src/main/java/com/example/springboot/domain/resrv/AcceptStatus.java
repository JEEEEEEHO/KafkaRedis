package com.example.springboot.domain.resrv;

public enum AcceptStatus {
    /**
     * 예약 확정 여부
     * */
    ACCEPT("Y"), DENY("N"), DEFAULT("");

    final private String status;
    private AcceptStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
