package com.axteroid.ose.server.repository.rules;

import java.util.List;

import com.axteroid.ose.server.jpa.model.Documento;

public interface TTOseServerQry {
	public List<Documento> findTTOseServer(Documento tbComprobante);
}
