package com.heliumv.factory;

import com.lp.server.projekt.service.BereichDto;

public interface IProjektServiceCall {
	BereichDto bereichFindByPrimaryKey(Integer iId);
}
