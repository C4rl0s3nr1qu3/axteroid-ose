package com.axteroid.ose.server.repository.dao;

import java.sql.Timestamp;
import java.util.Map;

import com.axteroid.ose.server.jpa.model.Documento;

public interface TtStressDAO {
	public void grabarTsStress(Documento tbComprobante, Map<String, Timestamp> mapTime);
}
