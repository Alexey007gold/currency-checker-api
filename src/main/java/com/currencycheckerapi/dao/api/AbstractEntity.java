package com.currencycheckerapi.dao.api;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base entity for database containing auto-generated id column
 *
 * Created by Alexey on 08.04.2017.
 */
@MappedSuperclass
@Data
public abstract class AbstractEntity {

    public static final String DB_SCHEMA = "strategy_game";

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

}
