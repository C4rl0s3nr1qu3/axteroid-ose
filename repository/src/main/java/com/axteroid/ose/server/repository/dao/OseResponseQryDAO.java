package com.axteroid.ose.server.repository.dao;

import java.util.List;

import com.axteroid.ose.server.repository.model.OseQueryBean;
import com.axteroid.ose.server.repository.model.OseResponseBean;

public interface OseResponseQryDAO {
	public List<OseResponseBean> getListOseResponseBean(OseQueryBean oseQueryBean);
}
