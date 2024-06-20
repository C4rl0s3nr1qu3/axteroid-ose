package com.axteroid.ose.server.rulesejb.dao;

import java.util.List;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.Emisor;
import com.axteroid.ose.server.rulesejb.emf.DAOCom;

@Local
public interface EmisorDAOLocal extends DAOCom<Emisor>{
	public Emisor buscarEmisor(Documento documento);
	public List<Emisor> buscarEmisorRuc(Documento documento) ;
}
