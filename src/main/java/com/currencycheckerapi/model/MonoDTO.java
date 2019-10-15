package com.currencycheckerapi.model;

import lombok.Data;

@Data
public class MonoDTO {

    private Integer currencyCodeA;
    private Integer currencyCodeB;
    private Long date;
    private Float rateBuy;
    private Float rateSell;
    private Float rateCross;

}
