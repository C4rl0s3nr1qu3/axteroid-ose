package com.axteroid.ose.server.repository.dao;

import org.bson.Document;

public interface TtOseResponseDAO {
	public void grabarOseResponse(Document document);
	public boolean findOseResponse(Document document);
}
