package sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.axteroid.ose.server.ubl20.gateway.request.MyNamespacePrefixMapper;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExchangeRateType;

/**
 * User: HNA
 * Date: 01/12/15
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SunatRetentionInformationType", propOrder = {
        "sunatRetentionAmount",
        "sunatRetentionDate",
        "sunatNetTotalPaid",
        "exchangeRate",
})

public class SunatRetentionInformationType {
	
    @XmlElement(name = "SUNATRetentionAmount",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatRetentionAmountType sunatRetentionAmount;
    @XmlElement(name = "SUNATRetentionDate",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatRetentionDateType sunatRetentionDate;
    @XmlElement(name = "SUNATNetTotalPaid",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatNetTotalPaidType sunatNetTotalPaid;
    @XmlElement(name = "ExchangeRate", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    protected ExchangeRateType exchangeRate;
    
	public SunatRetentionAmountType getSunatRetentionAmount() {
		return sunatRetentionAmount;
	}
	public void setSunatRetentionAmount(
			SunatRetentionAmountType sunatRetentionAmount) {
		this.sunatRetentionAmount = sunatRetentionAmount;
	}
	public SunatRetentionDateType getSunatRetentionDate() {
		return sunatRetentionDate;
	}
	public void setSunatRetentionDate(SunatRetentionDateType sunatRetentionDate) {
		this.sunatRetentionDate = sunatRetentionDate;
	}
	public SunatNetTotalPaidType getSunatNetTotalPaid() {
		return sunatNetTotalPaid;
	}
	public void setSunatNetTotalPaid(SunatNetTotalPaidType sunatNetTotalPaid) {
		this.sunatNetTotalPaid = sunatNetTotalPaid;
	}
	public ExchangeRateType getExchangeRate() {
		if(exchangeRate == null) {
			exchangeRate = new ExchangeRateType();
		}
		return exchangeRate;
	}
	public void setExchangeRate(ExchangeRateType exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}



