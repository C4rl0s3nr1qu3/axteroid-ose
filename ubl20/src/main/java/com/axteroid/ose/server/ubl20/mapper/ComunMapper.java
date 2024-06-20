package com.axteroid.ose.server.ubl20.mapper;

import java.util.List;

import com.axteroid.ose.server.ubl20.bean.LabelValue;

public interface ComunMapper {

    List<String> findUnidades();

    List<LabelValue> findUnidadesEquivalentes(String idEmisor);

    List<String> findTipoDocumento();

    List<String> findTipoDocumentoRelacionado();

    List<String> findDocumentoIdentidad();

    String findDocumentoIdentidadLabel(String tipoDocumento);

    String findTipoMonedaLabel(String tipoDocumento);

    List<String> findTipoMoneda();

    List<String> findTipoNC();

    List<String> findTipoND();

    Integer nextSequence();

    Integer nextSequencePostgresql();

    Integer nextSequenceOracle();
}
