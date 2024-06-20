package com.axteroid.ose.server.rulesejb.rules.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.rulesejb.dao.SunatParametroDAOLocal;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroRulesLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EGuiaDocumento;
import com.axteroid.ose.server.tools.edocu.EPercepcionDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.ERetencionDocumento;
import com.axteroid.ose.server.tools.ubltype.TypeAddress;
import com.axteroid.ose.server.tools.ubltype.TypeCommodityClassification;
import com.axteroid.ose.server.tools.ubltype.TypeConsignment;
import com.axteroid.ose.server.tools.ubltype.TypeDelivery;
import com.axteroid.ose.server.tools.ubltype.TypeDespatch;
import com.axteroid.ose.server.tools.ubltype.TypeItem;
import com.axteroid.ose.server.tools.ubltype.TypeItemProperty;
import com.axteroid.ose.server.tools.ubltype.TypeLocation;
import com.axteroid.ose.server.tools.ubltype.TypePartyLegalEntity;
import com.axteroid.ose.server.tools.ubltype.TypePayment;
import com.axteroid.ose.server.tools.ubltype.TypePricingReference;
import com.axteroid.ose.server.tools.ubltype.TypeShipment;
import com.axteroid.ose.server.tools.ubltype.TypeTransportEvent;

@Stateless
@Local(UBLListParametroRulesLocal.class)
public class UBLListParametroRulesImpl implements UBLListParametroRulesLocal{
	
    private static final Logger log = LoggerFactory.getLogger(UBLListParametroRulesImpl.class);
    
    public UBLListParametroRulesImpl() {}
 
	public void reglasValidarParametrosFacturaBoleta(Documento tbComprobante, EDocumento eDocumento) {
		//log.info("reglasValidarParametrosFacturaBoleta ");			
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
			
			for(TypeDelivery typeDelivery : eDocumento.getDelivery()) {
				TypeLocation deliveryLocation = typeDelivery.getDeliveryLocation();
				if(deliveryLocation != null) {
					TypeAddress address = deliveryLocation.getAddress();
					if((address != null) && (address.getId()!=null)) {
						if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
								Constantes.SUNAT_PARAMETRO_016, address.getId())) {
							log.info("address.getId(): {}",address.getId());
							String obsv = "";						
				    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
				    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
				    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4231); 		
						}					
					}				
				}
//				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) {
//					TypeShipment shipment = typeDelivery.getShipment();
//					if(shipment != null) {
//						TypeDelivery typeDeliveryShip = shipment.getDelivery();
//						if(typeDeliveryShip != null) {
//							TypeAddress address = typeDeliveryShip.getDeliveryAddress();						
//							if((address != null) && (address.getId()!=null)) {
								//log.info("address.getId(): "+address.getId());
//								if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
//										Constantes.SUNAT_PARAMETRO_016, address.getId())) {
//									log.info("address.getId(): {}",address.getId());
//									String obsv = "";
//						    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
//						    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
//						    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4176); 		
//								}					
//							}
//							
//						}
//						TypeAddress originAddress = shipment.getOriginAddress();
//						if((originAddress != null) && (originAddress.getId()!=null)) {
//							if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
//									Constantes.SUNAT_PARAMETRO_016, originAddress.getId())) {
//								log.info("originAddress.getId(): {}",originAddress.getId());
//								String obsv = "";
//					    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
//					    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
//					    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4181); 		
//							}					
//						}						
//					}
//				}			
			}
			
			reglasValidarParametrosItemFBDC(tbComprobante, eDocumento);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) 
				return;								
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}		
	}
	
	public void reglasValidarParametrosReciboServiciosPublicos(Documento tbComprobante, EDocumento eDocumento) {
		//log.info("reglasValidarParametrosReciboServiciosPublicos ");
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
		
			if(eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getDistrict()!=null){
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, Constantes.SUNAT_PARAMETRO_016, 
						eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getDistrict())) {
					log.info("eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getDistrict(): {}",
							eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getDistrict());
					String obsv = "";
		    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
		    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
		    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_ERROR_4231); 		
				}		
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	public void reglasValidarParametrosNotaCredito(Documento tbComprobante, EDocumento eDocumento) {	
		//log.info("reglasValidarParametrosNotaCredito");
		try {
			reglasValidarParametrosNotaDC(tbComprobante, eDocumento);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) 
				return;
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	public void reglasValidarParametrosNotaDebito(Documento tbComprobante, EDocumento eDocumento) {		
		//log.info("reglasValidarParametrosNotaDebito");
		try {
			reglasValidarParametrosNotaDC(tbComprobante, eDocumento);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) 
				return;
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	private void reglasValidarParametrosNotaDC(Documento tbComprobante, EDocumento eDocumento) {
		//log.info("reglasValidarParametrosNotaDC");
		try {
			reglasValidarParametrosItemFBDC(tbComprobante, eDocumento);
			if(tbComprobante.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0))) 
				return;
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	private void reglasValidarParametrosItemFBDC(Documento tbComprobante, EDocumento eDocumento) {		
		//log.info("reglasValidarParametrosItemFBDC");		
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
					(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
			
			
			if(eDocumento.getAccountingSupplierParty()!= null && 
					eDocumento.getAccountingSupplierParty().getParty() != null &&
					eDocumento.getAccountingSupplierParty().getParty().getPartyLegalEntity() != null) {			
				for (TypePartyLegalEntity typePartyLegalEntity : eDocumento.getAccountingSupplierParty().getParty().getPartyLegalEntity()) {
					if(typePartyLegalEntity.getRegistrationAddress()!=null) { 
						TypeAddress registrationAddress = typePartyLegalEntity.getRegistrationAddress();
						//log.info("registrationAddress.getId(): "+registrationAddress.getId());
						if(registrationAddress.getId()!=null) {
							if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
									Constantes.SUNAT_PARAMETRO_016, registrationAddress.getId())) {
								log.info("registrationAddress.getId(): {}",registrationAddress.getId());
								String obsv = "";
					    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
					    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
					    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4093); 		
							}	
						}
					}
				}
			}			
						
			for(EDocumentoItem edi : eDocumento.getItems()){			
				// Código de precio - PriceTypeCode		
				//log.info("edi.getUnidadMedidaComercial(): |{}|",edi.getUnidadMedidaComercial());
				if(edi.getUnidadMedidaComercial()!=null) {
					if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
							Constantes.SUNAT_PARAMETRO_C03,edi.getUnidadMedidaComercial())) {
						log.info("edi.getUnidadMedidaComercial(): |{}|",edi.getUnidadMedidaComercial());
						tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
						tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2936);
						return;
					}			
				}
				for(TypePricingReference edp : edi.getListTypePricingReference()){
					if(edp.getPriceTypeCode()!=null) {
						if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
								Constantes.SUNAT_PARAMETRO_010, edp.getPriceTypeCode())) {
							log.info("edp.getPriceTypeCode(): {}",edp.getPriceTypeCode());
							tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
							tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2410);
							return;
						}	
					}
				}
				if(edi.getItem()!=null){
					TypeItem item = edi.getItem();						
					if(item.getCommodityClassification() != null) {
						//log.info(" typeItem.getCommodityClassification().size() -  "+typeItem.getCommodityClassification().size());								
						for(TypeCommodityClassification tcc : item.getCommodityClassification()){
							//log.info(" tcc.getItemClassificationCode() -  "+tcc.getItemClassificationCode());
							if(tcc.getItemClassificationCode()!=null){
								if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
										Constantes.SUNAT_PARAMETRO_025, tcc.getItemClassificationCode() )) {
									log.info("tcc.getItemClassificationCode(): {}",tcc.getItemClassificationCode());
									String obsv = "";
						    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
						    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
						    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4332); 
								}
							}
						}
					}
					
					if(item.getAdditionalItemProperty() != null) {
						//log.info(" item.getAdditionalItemProperty().size() -  "+item.getAdditionalItemProperty().size());								
						for(TypeItemProperty tcc : item.getAdditionalItemProperty()){
							//log.info("tbComprobante.getTipoComprobante(): "+tbComprobante.getTipoComprobante()+" - tcc.getNameCode(): "+tcc.getNameCode()+" - tcc.getValue(): "+tcc.getValue());
							if((tcc.getValue()!=null) && (tcc.getNameCode()!=null)){					
								if(((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA)) ||
										(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA))) &&
										((tcc.getNameCode().equals("3055")) || (tcc.getNameCode().equals("3057")) ||
												(tcc.getNameCode().equals("4030")) || (tcc.getNameCode().equals("4032")) ||
												(tcc.getNameCode().equals("4042")) || (tcc.getNameCode().equals("4044")) ||
												(tcc.getNameCode().equals("7006")))) {
									if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
											Constantes.SUNAT_PARAMETRO_016, tcc.getValue() )) {
										log.info("tcc.getValue(): {}",tcc.getValue());
										String obsv = "";
							    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
							    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
							    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4280); 
									}
								}
								
								if(((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_CREDITO)) ||
										(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_NOTA_DEBITO))) &&
										(tcc.getNameCode().equals("7006"))){
									if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
											Constantes.SUNAT_PARAMETRO_016, tcc.getValue() )) {
										log.info("tcc.getValue(): {}",tcc.getValue());
										String obsv = "";
							    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
							    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
							    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4280); 
									}
								}
							}
						}
					}
				}	
				if(((tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_FACTURA))) ||
						(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_BOLETA))) {
					if(edi.getDelivery()!=null){
						for(TypeDelivery typeDelivery : edi.getDelivery()){
							if(typeDelivery.getDespatch()!=null) {
								TypeDespatch despatch = typeDelivery.getDespatch();
								if(despatch.getDespatchAddress()!=null) {
									TypeAddress despatchAddress = despatch.getDespatchAddress();
									if(despatchAddress.getId()!=null) {
										//log.info(" despatchAddress.getId(): "+despatchAddress.getId());
										if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
												Constantes.SUNAT_PARAMETRO_016, despatchAddress.getId() )) {
											log.info("despatchAddress.getId(): {}",despatchAddress.getId());
											String obsv = "";
								    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
								    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
								    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 
										}
									}								
								}
							}
							if(typeDelivery.getDeliveryLocation()!=null) {
								TypeLocation deliveryLocation = typeDelivery.getDeliveryLocation();
								if(deliveryLocation.getAddress()!= null) {
									TypeAddress address = deliveryLocation.getAddress();
									if(address.getId()!=null) {
										//log.info(" address.getId(): "+address.getId());
										if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
												Constantes.SUNAT_PARAMETRO_016, address.getId() )) {
											log.info("address.getId(): {}",address.getId());
											String obsv = "";
								    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
								    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
								    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 
										}
									}	
								}
							}
							if(typeDelivery.getShipment()!=null) {
								TypeShipment shipment = typeDelivery.getShipment();
								if(shipment.getConsignment()!=null) {
									for(TypeConsignment typeConsignment : shipment.getConsignment()) {
										if(typeConsignment.getPlannedPickupTransportEvent()!=null) {
											TypeTransportEvent plannedPickupTransportEvent = typeConsignment.getPlannedPickupTransportEvent();
											if(plannedPickupTransportEvent.getLocation()!= null) {
												TypeLocation location = plannedPickupTransportEvent.getLocation();
												if(location.getId()!=null) {
													if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
															Constantes.SUNAT_PARAMETRO_016, location.getId() )) {
														log.info("location.getId(): {}",location.getId());
														String obsv = "";
											    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
											    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
											    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 
													}
												}	
											}
										}
										if(typeConsignment.getPlannedDeliveryTransportEvent()!=null) {
											TypeTransportEvent plannedDeliveryTransportEvent = typeConsignment.getPlannedDeliveryTransportEvent();
											if(plannedDeliveryTransportEvent.getLocation()!= null) {
												TypeLocation location = plannedDeliveryTransportEvent.getLocation();
												if(location.getId()!=null) {
													if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
															Constantes.SUNAT_PARAMETRO_016, location.getId() )) {
														log.info("location.getId(): {}",location.getId());
														String obsv = "";
											    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
											    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
											    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 
													}
												}	
											}
										}
									}
								}								
							}							
						}
					}
				}
			}		
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosItemFBDC Exception \n"+errors);
		} 
	}
	
	public void reglasValidarParametrosDAEAdquiriente(Documento tbComprobante, EDocumento eDocumento) {
		//log.info("reglasValidarParametrosDAEAdquiriente");
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						

			if(eDocumento.getAccountingCustomerParty()!= null && 
					eDocumento.getAccountingCustomerParty().getParty() != null &&
					eDocumento.getAccountingCustomerParty().getParty().getPostalAddress() != null &&
					eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getPostalZone()!=null){
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante,
						Constantes.SUNAT_PARAMETRO_016, 
						eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getPostalZone())) {
					log.info("eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getPostalZone(): {}",
							eDocumento.getAccountingCustomerParty().getParty().getPostalAddress().getPostalZone());
					String obsv = "";
		    		if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
		    			 obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
		    		tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4231); 		
				}		
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}	
	
	public void reglasValidarParametrosResumenDiario(Documento tbComprobante, EResumenDocumento eDocumento) {
		//log.info("reglasValidarParametrosResumenDiario");
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						

			for(EResumenDocumentoItem erdi : eDocumento.getItems()){
				for(TypePayment typePayment : erdi.getListTypePayment()){
					if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
							Constantes.SUNAT_PARAMETRO_017, typePayment.getInstructionID())) {
						log.info("typePayment.getInstructionID(): {}",typePayment.getInstructionID());
						tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
						tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2256);
						return;
					}
				}
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	public void reglasValidarParametrosRetencion(Documento tbComprobante, ERetencionDocumento eDocumento) {
		// Fecha de emisión		
		//log.info(" eDocumento.getAgentParty().getPostalAddress().getId() -->"+ eDocumento.getAgentParty().getPostalAddress().getId());
		//log.info(" eDocumento.getReceiverParty().getPostalAddress().getId() -->"+eDocumento.getReceiverParty().getPostalAddress().getId());		
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						

			if(eDocumento.getAgentParty().getPostalAddress().getId()!=null) {
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
						Constantes.SUNAT_PARAMETRO_016, eDocumento.getAgentParty().getPostalAddress().getId())) {
					log.info("eDocumento.getAgentParty().getPostalAddress().getId(): {}",
							eDocumento.getAgentParty().getPostalAddress().getId());
					String obsv = "";
					if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
						obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
					tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2917); 
					return;      			
				}
			}
			
			if(eDocumento.getReceiverParty().getPostalAddress().getId()!=null) {
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
						Constantes.SUNAT_PARAMETRO_016, eDocumento.getReceiverParty().getPostalAddress().getId())) {
					log.info("eDocumento.getReceiverParty().getPostalAddress().getId(): {}",
							eDocumento.getReceiverParty().getPostalAddress().getId());
					String obsv = "";
					if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
						obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
					tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2917); 
					return;      			
				}
			 }
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	public void reglasValidarParametrosPercepcion(Documento tbComprobante, EPercepcionDocumento eDocumento) {		
		//log.info(" eDocumento.getAgentParty().getPostalAddress().getId() -->"+ eDocumento.getAgentParty().getPostalAddress().getId());
		//log.info(" eDocumento.getReceiverParty().getPostalAddress().getId() -->"+eDocumento.getReceiverParty().getPostalAddress().getId());			
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						

			if(eDocumento.getAgentParty().getPostalAddress().getId()!=null) {
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
						Constantes.SUNAT_PARAMETRO_016, eDocumento.getAgentParty().getPostalAddress().getId())) {
					log.info("eDocumento.getAgentParty().getPostalAddress().getId(): {}",
							eDocumento.getAgentParty().getPostalAddress().getId());			
					String obsv = "";
					if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
						obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
					tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2917); 
					return;      			
				}
			}
			
			if(eDocumento.getReceiverParty().getPostalAddress().getId()!=null) {
				if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
						Constantes.SUNAT_PARAMETRO_016,  eDocumento.getReceiverParty().getPostalAddress().getId())) {
					log.info("eDocumento.getReceiverParty().getPostalAddress().getId(): {}",
							eDocumento.getReceiverParty().getPostalAddress().getId());				
					String obsv = "";
					if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
						obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
					tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_2917); 
					return;      			
				}
			}
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}
	
	public void reglasValidarParametrosGuia(Documento tbComprobante, EGuiaDocumento eDocumento){
		//log.info(" eDocumento.getNumeroDocumentoDestinatario() -->"+ eDocumento.getNumeroDocumentoDestinatario());
		//log.info(" eDocumento.getTipoDocumentoDestinatario() -->"+ eDocumento.getTipoDocumentoDestinatario());
		try {
			SunatParametroDAOLocal tsParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						

			if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
					Constantes.SUNAT_PARAMETRO_006, eDocumento.getTipoDocumentoDestinatario())) {
				log.info("eDocumento.getTipoDocumentoDestinatario(): {}", eDocumento.getTipoDocumentoDestinatario());
				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2760);
				return;      			
			}
			
			//log.info(" eDocumento.getMotivoTraslado() -->"+ eDocumento.getMotivoTraslado());
			if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
					Constantes.SUNAT_PARAMETRO_021, eDocumento.getMotivoTraslado() )) {
				log.info("eDocumento.getMotivoTraslado(): {}", eDocumento.getMotivoTraslado());
				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_1063);
				return;      			
			}
			
			//log.info(" eDocumento.getModalidadTraslado() -->"+ eDocumento.getModalidadTraslado());
			if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
					Constantes.SUNAT_PARAMETRO_022, eDocumento.getModalidadTraslado())) {
				log.info("eDocumento.getModalidadTraslado(): {}"+ eDocumento.getModalidadTraslado());
				tbComprobante.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_2773);
				return;      			
			}
			
			//log.info(" eDocumento.getUbigeoPtoLLegada() -->"+ eDocumento.getUbigeoPtoLLegada());
			if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
					Constantes.SUNAT_PARAMETRO_016, eDocumento.getUbigeoPtoLLegada())) {
				log.info("eDocumento.getUbigeoPtoLLegada(): {}", eDocumento.getUbigeoPtoLLegada());
				String obsv = "";
				if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
					obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   		 	tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 			
			}	
			
			//log.info(" eDocumento.getUbigeoPtoPartida() -->"+ eDocumento.getUbigeoPtoPartida());
			if(tsParametroDAOLocal.getParametroNoExiste(tbComprobante, 
					Constantes.SUNAT_PARAMETRO_016, eDocumento.getUbigeoPtoPartida())) {
				log.info("eDocumento.getUbigeoPtoPartida(): {}", eDocumento.getUbigeoPtoPartida());
				String obsv = "";
	   		 	if(tbComprobante.getObservaNumero()!=null && !(tbComprobante.getObservaNumero().isEmpty()))
	   		 		obsv = tbComprobante.getObservaNumero()+Constantes.OSE_SPLIT_3;
	   		 	tbComprobante.setObservaNumero(obsv+Constantes.SUNAT_OBSERV_4200); 			
			}	
		} catch (Exception e) {
			tbComprobante.setErrorComprobante(Constantes.CONTENT_ERROR_DB.charAt(0));
			tbComprobante.setErrorNumero(Constantes.SUNAT_ERROR_0138);
			tbComprobante.setErrorLog(e.toString());
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("reglasValidarParametrosFacturaBoleta Exception \n"+errors);
		}			
	}    
    
	public String bucarParametro(String codParametro, String codArgumento) {
		log.info("bucarParametro: {} - {} ", codParametro,codArgumento);
		String desArgumento = "";
		try {					
			SunatParametroDAOLocal sunatParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
			desArgumento = sunatParametroDAOLocal.bucarParametro(codParametro,codArgumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarParametro Exception \n"+errors);
		}
		return desArgumento;
	}	
	
	public String bucarLoginParametro(String codArgumento) {
		log.info("bucarLoginParametro: {}",codArgumento);
		String desArgumento = "";
		try {					
			SunatParametroDAOLocal sunatParametroDAOLocal = 
				(SunatParametroDAOLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"SunatParametroDAOImpl"+Constantes.OSE_CDEJB_DAO+"SunatParametroDAOLocal");						
			desArgumento = sunatParametroDAOLocal.bucarLoginParametro(codArgumento);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("bucarLoginParametro Exception \n"+errors);
		}
		return desArgumento;
	}		
	
}
