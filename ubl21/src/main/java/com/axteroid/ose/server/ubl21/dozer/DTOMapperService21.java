package com.axteroid.ose.server.ubl21.dozer;

/**
 * User: RAC
 * Date: 06/03/12
 */
public interface DTOMapperService21 {

    <T> T map(java.lang.Object o, java.lang.Class<T> tClass);
    public void init();
}
