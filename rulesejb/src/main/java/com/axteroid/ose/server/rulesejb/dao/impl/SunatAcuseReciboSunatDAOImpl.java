package com.axteroid.ose.server.rulesejb.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.SunatAcuseReciboSunat;
import com.axteroid.ose.server.rulesejb.dao.SunatAcuseReciboSunatDAOLocal;
import com.axteroid.ose.server.rulesejb.emf.impl.DAOComImpl;

@Stateless
@Local(SunatAcuseReciboSunatDAOLocal.class)
public class SunatAcuseReciboSunatDAOImpl extends DAOComImpl<SunatAcuseReciboSunat> implements SunatAcuseReciboSunatDAOLocal {
	private static final Logger log = LoggerFactory.getLogger(SunatAcuseReciboSunatDAOImpl.class);
    public SunatAcuseReciboSunatDAOImpl() {}
    
	public void grabaTsAcuseReciboSunat(SunatAcuseReciboSunat tsAcuseReciboSunat){	
		log.info("grabaTsAcuseReciboSunat: "+tsAcuseReciboSunat.toString());
		try {    
			this.add(tsAcuseReciboSunat);	
			//log.info("Grabar Fin: "+tbComprobante.getNombre());
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("grabaTsAcuseReciboSunat Exception \n"+errors);
		}
	}
}
