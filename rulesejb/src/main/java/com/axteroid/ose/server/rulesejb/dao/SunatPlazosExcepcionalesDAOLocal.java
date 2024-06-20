package com.axteroid.ose.server.rulesejb.dao;

import java.util.Date;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;

@Local
public interface SunatPlazosExcepcionalesDAOLocal {
	 public void getPlazoExcepcional(Documento tbComprobante, Date fechaEmisionComprobante);
}
