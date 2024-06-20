package com.axteroid.ose.server.ubl20.dozer;

/**
 * User: RAC
 * Date: 06/03/12
 */
public interface DTOMapperService {

    <T> T map(java.lang.Object o, java.lang.Class<T> tClass);
    public void init();
}
