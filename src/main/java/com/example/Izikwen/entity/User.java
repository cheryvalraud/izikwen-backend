package com.example.Izikwen.entity;

import com.example.Izikwen.repository.UserDetailsWithVersion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails, UserDetailsWithVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String country;

    @JsonIgnore
    private String password;

    private String authProvider;   // "google" | "password"
    private String auth0Id;

    private Instant createdAt = Instant.now();

    // ===== 2FA =====

    private boolean twoFactorEnabled = false;

    @Column(length = 512)
    private String twoFactorSecret;

    // ===== TOKEN VERSION =====

    private long tokenVersion = 0;

    // ---------- UserDetails ----------

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    // ---------- Token Version ----------

    @Override
    public long getTokenVersion() {
        return tokenVersion;
    }

    public void incrementTokenVersion() {
        this.tokenVersion++;
    }

    // ---------- GETTERS ----------

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCountry() { return country; }
    public String getAuthProvider() { return authProvider; }
    public String getAuth0Id() { return auth0Id; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public String getTwoFactorSecret() { return twoFactorSecret; }

    // ---------- SETTERS ----------

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setCountry(String country) { this.country = country; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
    public void setAuth0Id(String auth0Id) { this.auth0Id = auth0Id; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setPassword(String password) { this.password = password; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
    public void setTwoFactorSecret(String twoFactorSecret) { this.twoFactorSecret = twoFactorSecret; }
}
