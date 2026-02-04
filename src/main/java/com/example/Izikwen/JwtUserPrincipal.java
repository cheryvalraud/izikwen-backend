package com.example.Izikwen;

public class JwtUserPrincipal {

    private final Long id;


    public JwtUserPrincipal(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
