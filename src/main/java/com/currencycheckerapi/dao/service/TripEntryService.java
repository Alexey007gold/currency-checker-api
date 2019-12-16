package com.currencycheckerapi.dao.service;

import com.currencycheckerapi.dao.api.BaseService;
import com.currencycheckerapi.dao.entity.TripEntryEntity;
import com.currencycheckerapi.service.repository.TripEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class TripEntryService extends BaseService<TripEntryEntity, TripEntryRepository> {
}
