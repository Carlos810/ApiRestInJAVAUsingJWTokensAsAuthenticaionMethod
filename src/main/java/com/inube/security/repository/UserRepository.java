package com.inube.security.repository;

import com.inube.security.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserRepository {
    //@Autowired permite que Spring inyecvte automaticamente una instancia de JdbcTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate ;

    @Override
    public UserModel findByName(String user) {

        //Se usa  '?' para evitar inyeccion a SQL
        String sql = "SELECT * FROM USERSSBS WHERE name = ?";

        //queryForObject:
        //Ejecuta la consulta
        //Esper un solo resultado
        //Mapea el resultado a un objeto UserModel
        return  jdbcTemplate.queryForObject(
                sql,
                new Object[]{user}, //? porque se usa este objeto 'user'
                new BeanPropertyRowMapper<>(UserModel.class)
        );

    }
}
