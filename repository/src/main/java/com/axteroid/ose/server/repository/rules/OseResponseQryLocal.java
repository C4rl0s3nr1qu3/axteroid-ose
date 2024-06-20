package com.axteroid.ose.server.repository.rules;

import java.util.List;

import com.axteroid.ose.server.repository.model.OseQueryBean;
import com.axteroid.ose.server.repository.model.OseResponseBean;

public interface OseResponseQryLocal {
	public List<OseResponseBean> getListOseResponseBean(OseQueryBean oseQueryBean);
	public boolean findOseResponseBean(String id);
}
