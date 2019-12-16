package com.currencycheckerapi.dao.api;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Base repository for database entities
 *
 * @author Alexey Koveckiy on 08.04.2017.
 */
@NoRepositoryBean
public interface BaseRepository<E extends AbstractEntity> extends PagingAndSortingRepository<E, String> {
}
