package com.devsuperior.DSCatalog.projections;

public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    String getAuthority();
    Long getRoleId();

}
