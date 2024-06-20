package com.axteroid.ose.server.repository.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class OseCpeBean {
	
	@JsonProperty("NUM_RUC")
    private long numRuc;
	
	@JsonProperty("COD_CPE")
    private String codCpe;
	
    @JsonProperty("NUM_SERIE_CPE")
    private String numSerieCpe;
    
    @JsonProperty( "NUM_CPE")
    private String numCpe;
    
    @JsonProperty("IND_ESTADO_CPE")
    private Short indEstadoCpe;
    
    @JsonProperty("FEC_EMISION_CPE")
    private Date fecEmisionCpe;
    
    @JsonProperty("MTO_IMPORTE_CPE")
    private BigDecimal mtoImporteCpe;
    
    @JsonProperty("COD_MONEDA_CPE")
    private String codMonedaCpe;

	public long getNumRuc() {
		return numRuc;
	}

	public void setNumRuc(long numRuc) {
		this.numRuc = numRuc;
	}

	public String getCodCpe() {
		return codCpe;
	}

	public void setCodCpe(String codCpe) {
		this.codCpe = codCpe;
	}

	public String getNumSerieCpe() {
		return numSerieCpe;
	}

	public void setNumSerieCpe(String numSerieCpe) {
		this.numSerieCpe = numSerieCpe;
	}

	public String getNumCpe() {
		return numCpe;
	}

	public void setNumCpe(String numCpe) {
		this.numCpe = numCpe;
	}

	public Short getIndEstadoCpe() {
		return indEstadoCpe;
	}

	public void setIndEstadoCpe(Short indEstadoCpe) {
		this.indEstadoCpe = indEstadoCpe;
	}

	public Date getFecEmisionCpe() {
		return fecEmisionCpe;
	}

	public void setFecEmisionCpe(Date fecEmisionCpe) {
		this.fecEmisionCpe = fecEmisionCpe;
	}

	public BigDecimal getMtoImporteCpe() {
		return mtoImporteCpe;
	}

	public void setMtoImporteCpe(BigDecimal mtoImporteCpe) {
		this.mtoImporteCpe = mtoImporteCpe;
	}

	public String getCodMonedaCpe() {
		return codMonedaCpe;
	}

	public void setCodMonedaCpe(String codMonedaCpe) {
		this.codMonedaCpe = codMonedaCpe;
	}    
}
