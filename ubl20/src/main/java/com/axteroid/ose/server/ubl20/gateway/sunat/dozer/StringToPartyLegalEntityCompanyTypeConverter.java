package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompanyIDType;

public class StringToPartyLegalEntityCompanyTypeConverter implements CustomConverter {

	@Override
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class destinationClass,
			Class sourceClass) {

		String valor = (String) sourceFieldValue;
		if (valor == null) {
			return null;
		}
		
		String[] values = StringUtils.split(valor, "|");
				
		CompanyIDType companyIDType = new CompanyIDType();
		companyIDType.setValue(values[0]);
		companyIDType.setSchemeID(values[1]);
		companyIDType.setSchemeAgencyName(values[2]);
		companyIDType.setSchemeURI(values[3]);
		
		PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
		partyLegalEntityType.setCompanyID(companyIDType);
		return Arrays.asList(partyLegalEntityType);
	}

}
