package com.axteroid.ose.server.rulesejb.dao;

import java.util.Date;
import javax.ejb.Local;
import com.axteroid.ose.server.jpa.model.Documento;

@Local
public interface SunatCertificadoEmisorDAOLocal {
	public void getCertificadoEmisor_NumIdCd(Documento tbComprobante, 
			Date fechaEmisionComprobante, String numIdCd, Date dategetNotBefore, Date dateNotAfter);
}
