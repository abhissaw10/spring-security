package com.example.securitydemo;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationRole {

    STUDENT(Sets.newHashSet(ApplicationPermission.STUDENT_READ)),
    ADMIN(Sets.newHashSet(ApplicationPermission.COURSE_WRITE,ApplicationPermission.COURSE_READ)),
    NEW_ADMIN(Sets.newHashSet());


    private Set<ApplicationPermission> permissions;

    ApplicationRole(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationPermission> getPermissions(){
        return this.getPermissions();
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = permissions.stream().map(
                p-> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
