package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.SunatAcuseReciboSunat;
import com.axteroid.ose.server.rulesejb.emf.DAOCom;

@Local
public interface SunatAcuseReciboSunatDAOLocal extends DAOCom<SunatAcuseReciboSunat> {
	public void grabaTsAcuseReciboSunat(SunatAcuseReciboSunat tsAcuseReciboSunat);
}
