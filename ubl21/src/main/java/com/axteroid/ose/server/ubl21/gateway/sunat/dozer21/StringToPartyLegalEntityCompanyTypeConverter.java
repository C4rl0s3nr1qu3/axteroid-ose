package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2_1.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.CompanyIDType;

public class StringToPartyLegalEntityCompanyTypeConverter implements CustomConverter {
	//private static final Logger log = Logger.getLogger(StringToPartyLegalEntityCompanyTypeConverter.class);
	private static final Logger log = LoggerFactory.getLogger(StringToPartyLegalEntityCompanyTypeConverter.class);
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class destinationClass,
			Class sourceClass) {

		String valor = (String) sourceFieldValue;
		if (valor == null) {
			return null;
		}
		//log.info("21 StringToPartyLegalEntityCompanyTypeConverter --> "+valor);
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
