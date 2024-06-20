package com.axteroid.ose.server.repository.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.conn.DaoMongoDB;
import com.axteroid.ose.server.repository.dao.OseCpeQryDAO;

public class OseCpeQryDAOImpl extends DaoMongoDB implements OseCpeQryDAO {

	private static final Logger log = LoggerFactory.getLogger(OseCpeQryDAOImpl.class);
	public OseCpeQryDAOImpl(){
		super();		
		conexionMongoClientOSE();
		conexionMongoDatabaseOSE();
	}
}
