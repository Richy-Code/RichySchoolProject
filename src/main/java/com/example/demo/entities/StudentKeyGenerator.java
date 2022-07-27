package com.example.demo.entities;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.exception.spi.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class StudentKeyGenerator implements IdentifierGenerator, Configurable {
    private String prefix;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        IdentifierGenerator.super.configure(type, params, serviceRegistry);
        prefix = params.getProperty("prefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj)
            throws HibernateException {
        String suffix;
        String query = String.format("select count(%s) from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());
        Connection connection = session.connection();
        int max = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                max = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if ((max + 1) <10)
            suffix = String.format("%03d",(max+1));
        else if ((max + 1) < 100) {
            suffix = String.format("%02d",(max + 1));
        }else
            suffix = String.valueOf((max + 1));

        return prefix + "-" + suffix;
    }

    @Override
    public void configure(Properties properties) throws HibernateException {

    }
}
