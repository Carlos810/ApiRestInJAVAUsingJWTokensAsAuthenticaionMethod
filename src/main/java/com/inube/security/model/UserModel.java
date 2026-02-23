package com.inube.security.model;

import lombok.Data;

@Data
public class UserModel {
    //noombre de la llave primaria en basede datos
    Integer user_id;

    String name;

    String password;
    String phone;
    //rolde usuario para control de acceso: juan, gabriela, etc.
    String role;
}
