package oasis.names.specification.ubl.schema.xsd;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ItemPropertyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PricingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;

import java.util.List;

/**
 * Created by RAZANERO on 24/06/14.
 */
public interface IDocumentoItemSunat {

    public PricingReferenceType getPricingReference();

    public String getIdValue();

    public List<ItemPropertyType> getItemPropertyTypeList();

    public List<TaxTotalType> getTaxTotal();

    public List<AllowanceChargeType> getAllowanceCharge();
}
