package com.inube.security.repository;

import com.inube.security.model.UserModel;

public interface IUserRepository {
    //metodo que buscara un suario por su nombre, recibe parametro username
    //devuelve un objeto usermodel si lo encuentra
    public UserModel findByName(String user);

}
