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
@XmlType(name = "SunatPerceptionInformationType", propOrder = {
        "sunatPerceptionAmount",
        "sunatPerceptionDate",
        "sunatNetTotalCashed",
        "exchangeRate",
})

public class SunatPerceptionInformationType {
	
    @XmlElement(name = "SUNATPerceptionAmount",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatPerceptionAmountType sunatPerceptionAmount;
    @XmlElement(name = "SUNATPerceptionDate",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatPerceptionDateType sunatPerceptionDate;
    @XmlElement(name = "SUNATNetTotalCashed",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatNetTotalCashedType sunatNetTotalCashed;
    @XmlElement(name = "ExchangeRate", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    protected ExchangeRateType exchangeRate;
    
	public SunatPerceptionAmountType getSunatPerceptionAmount() {
		return sunatPerceptionAmount;
	}
	public void setSunatPerceptionAmount(
			SunatPerceptionAmountType sunatPerceptionAmount) {
		this.sunatPerceptionAmount = sunatPerceptionAmount;
	}
	public SunatPerceptionDateType getSunatPerceptionDate() {
		return sunatPerceptionDate;
	}
	public void setSunatPerceptionDate(SunatPerceptionDateType sunatPerceptionDate) {
		this.sunatPerceptionDate = sunatPerceptionDate;
	}
	public SunatNetTotalCashedType getSunatNetTotalCashed() {
		return sunatNetTotalCashed;
	}
	public void setSunatNetTotalCashed(SunatNetTotalCashedType sunatNetTotalCashed) {
		this.sunatNetTotalCashed = sunatNetTotalCashed;
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



