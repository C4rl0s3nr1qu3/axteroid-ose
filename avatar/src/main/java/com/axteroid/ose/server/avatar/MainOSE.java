package com.axteroid.ose.server.avatar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.apirest.sunat.ConsultaIntegradaSunatRest;
import com.axteroid.ose.server.apirest.sunat.ConsultaSunat;
import com.axteroid.ose.server.avatar.dao.SunatParametroPGDao;
import com.axteroid.ose.server.avatar.dao.impl.InsertList2SunatPGDaoImpl;
import com.axteroid.ose.server.avatar.dao.impl.SunatParametroPGDaoImpl;
import com.axteroid.ose.server.avatar.jdbc.RsetPostGresJDBC;
import com.axteroid.ose.server.avatar.task.MainFactory;
import com.axteroid.ose.server.avatar.task.SunatListDownload;
import com.axteroid.ose.server.avatar.task.SunatListRead;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.jpa.model.SunatParametroPK;
import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.bean.SunatRequest;
import com.axteroid.ose.server.tools.bean.SunatBeanResponse;

public class MainOSE {
	private static Logger log = LoggerFactory.getLogger(MainOSE.class);
	SunatRequest sunatConsulta = new SunatRequest();  
	
	public static void main(String[] args) throws InterruptedException {
		MainOSE mainOSE = new MainOSE();
		//mainOSE.descargaSunatOSE();
		mainOSE.cargaListaSunatOSE();
		//mainOSE.getListaParametroOSE();
		//mainOSE.getListEncript();			
		//mainOSE.getListDesencript();
		//mainOSE.setInsertEncript2DB();
		//mainOSE.getListDesencript2DB();
		//mainOSE.updateCDR_UBL_CDRSUNAT();
		//mainOSE.updateARSUNAT();
		//mainOSE.getCPE();
		//mainOSE.getConsultaIntegradaSunat();
		System.exit(0);
	}
	
    private void descargaSunatOSE() {  
    	List <String> fechaList = new ArrayList<String>();
    	//fechaList.add("20201019");
    	//fechaList.add("20210302");
    	fechaList.add("20230317");
    	SunatListDownload sunatListDownload = new SunatListDownload();
    	for(String fecha : fechaList) { 
    		sunatListDownload.actualizarListasSunat(fecha);
    	}   	
    }	
    
    private void cargaListaSunatOSE() {  
    	SunatListDownload sunatListDownload = new SunatListDownload();
    	String fecha = "20230317";
    	sunatListDownload.listSunatAdd(fecha);	
    }	
		
    private void getListaParametroOSE() {  
    	SunatParametroPGDao sunatParametroPGDao = new SunatParametroPGDaoImpl();
    	String codParametro = "ERR";
    	String codArgumento = "";
    	sunatParametroPGDao.getSunatParametro(codParametro,codArgumento);
    }
    
	public void getListEncript() {				
		log.info("getListEncript ");
    	List<String> listCadena = new ArrayList<String>();
    	//listCadena.add("B3zl3nksOs2.");
    	listCadena.add("EMISOR_20195011169");
		for(String cadena : listCadena) {		
			String encriptado = NcCrypt.encriptarPassword(cadena);			
			log.info("Cadena : "+cadena+" - encriptado : "+encriptado);
		}
	}

	public void getListDesencript() {				
		log.info("getListDesencript ");
		//String cadena = "x'446F6356544D70354F6765536C392F6C7972597538413D3D2020202020202020'";
		String cadena = "x'4668566870454E4463446A38584C47314D6751386F497A66653052594B663554'";
		String encriptado = NcCrypt.desencriptarPassword(cadena);
		log.info(" Cadena: "+cadena+" - desencriptado: "+encriptado);
	}	
	
	public void setInsertEncript2DB() {				
		log.info("setInsertEncript2DB ");
		try {
			String cadena = "EthicalHackingC47314D6";
			//String cadena = "AXTEROIDOSEAVATAR";	
			//String cadena = "EMISOR_20520485750";
			//String cadena = "PSE_20298058490";
			String encriptado = NcCrypt.encriptarPassword(cadena);
			SunatParametroPK sunatParametroPK = new SunatParametroPK();		
			sunatParametroPK.setCodParametro("SEC");
			sunatParametroPK.setCodArgumento("AXTEROIDOSE_Ethical_Hacking");
			//sunatParametroPK.setCodArgumento("AXTEROIDOSE_AVATAR");
			//sunatParametroPK.setCodArgumento("AXTEROID_EMISOR_20520485750");
			//sunatParametroPK.setCodArgumento("AXTEROID_PSE_20298058490");
			SunatParametro sunatParametro = new SunatParametro();
			sunatParametro.setSunatParametroPK(sunatParametroPK);
			sunatParametro.setDesArgumento(encriptado);
			sunatParametro.setUserCrea("AXTEROIDOSE_AVATAR");
			sunatParametro.setFechaCrea(new Date());
			InsertList2SunatPGDaoImpl insertList2TSPGDaoImpl = new InsertList2SunatPGDaoImpl();
			insertList2TSPGDaoImpl.insertSunatParametro(sunatParametro);	
			log.info("Cadena : "+cadena+" - encriptado : "+encriptado);
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getListEncript2DB Exception \n"+errors);
		}
	}
	
	public void getListDesencript2DB() {	
		log.info("getListDesencript2DB ");
		RsetPostGresJDBC rsetPostGresJDBC = new RsetPostGresJDBC();
		String parametro = "SEC";
		String val = "AXTEROID_EMISOR_20376365680";
		String cadena = rsetPostGresJDBC.listComprobantes_Parametro(parametro,val);
		String encriptado = NcCrypt.desencriptarPassword(cadena);
		log.info("usuario: "+val+" - Cadena: "+cadena+" - desencriptado: "+encriptado);
	}	
	
	private void updateCDR_UBL_CDRSUNAT() {
		//MainFactory mainFactory = new MainFactory();	
		//oseFactory.actualizarListasSunat();		
		//String tipo = "UBL";
		//String tipo = "CDR";
		//String tipo = "CDR-SUNAT";
		//oseFactory.updateCDR_UBL_CDRSUNAT(136520l, tipo);
	}
	
	private void updateARSUNAT() {
		MainFactory mainFactory = new MainFactory();
//		String estado = "90";
//		String contar = "500";
//		String mensaje = " is null ";
//		for(int j = 1; j<50; j++)
//			mainFactory.updateMensajeSUNAT(estado, contar, mensaje);		
		mainFactory.updateMensajeSUNATID("56629191");		
	}

    public void getCPE(){
    	log.info("getCPE"); 
    	List<String[]> ltSt = new ArrayList<String[]>();
    	//String [] tb1 = "20478005017-01-F004-00025128-10/03/2020-47.20".split("-");
    	//Date d = new Date(1597035600000L);
    	//String fecha = DateUtil.dateFormat(d,"dd/MM/yyyy");
    	//System.out.print("fecha: "+fecha);2020-08-26
//    	String fecha = "26/08/2020";
//    	String monto = "10130.00";
//    	String ubl = "20256211310-03-B019-00000122-"+fecha+"-"+monto;
    	
    	//String fecha = "31/08/2020";//2020-09-04
    	//String monto = "1400.56";  	
    	//String ubl = "20256211310-03-B212-00003013-"+fecha+"-"+monto;

//    	String fecha = "09/09/2020";//2020-09-04
//    	String monto = "1400.56";  	
//    	String ubl = "20256211310-07-B212-1885-"+fecha+"-"+monto;
    	//20510103531-03-B103-0005885|06/02/2020|198.00
    	//20337657037 - 03 - B006 - 86198 | 03/09/2020 | 5.00
    	//20517710955 - 03 - B002 - 89208 | 22/10/2020 | 60.00
    	
    	String fecha = "22/10/2020";//2020-10-06
    	String monto = "60.00";  	
    	String ubl = "20603235780-01-F001-01277206-"+fecha+"-"+monto;
    	//String ubl = "20603235780-01-F001-1277206-"+fecha+"-"+monto;
    	
    	String [] tb1 = ubl.split("-");
    	ltSt.add(tb1);  
    	//String [] tb2 = "20100317967-01-0005-00000010".split("-");
    	//ltSt.add(tb2); 
    	for(String[] s : ltSt) {
        	sunatConsulta.setNumRuc(s[0]);
        	sunatConsulta.setCodComp(s[1]);
        	sunatConsulta.setNumeroSerie(s[2]);
        	sunatConsulta.setNumero(s[3]);
        	sunatConsulta.setFechaEmision(s[4]);
        	sunatConsulta.setMonto(s[5]);     		
    		this.getCPESunatList();
    		this.getOlitwsconscpegem();
    		//this.getOltiitconsvalicpe();        	     	      	
    		this.getConsultaIntegradaSunat();
    	}
    } 	
	
    private void getCPESunatList(){
    	SunatListRead sunatListRead = new SunatListRead();
    	Long num = Long.valueOf(sunatConsulta.getNumero());
    	sunatListRead.buscarListGetSunatList(sunatConsulta.getNumRuc(), sunatConsulta.getCodComp(), 
    			sunatConsulta.getNumeroSerie(), sunatConsulta.getNumero());
    	sunatListRead.buscarListGetSunatListRest(sunatConsulta.getNumRuc(), sunatConsulta.getCodComp(), 
    			sunatConsulta.getNumeroSerie(), num);
    }
    
    private void getOlitwsconscpegem(){    	
		Long longRuc = Long.valueOf(sunatConsulta.getNumRuc());
		String nroDocRefPri = sunatConsulta.getNumeroSerie()+"-"+sunatConsulta.getNumero();		
    	Boolean cdr = ConsultaSunat.getOlitwsconscpegem(longRuc, sunatConsulta.getCodComp(), nroDocRefPri);
    	log.info("SUNAT getOlitwsconscpegem-1: "+cdr);
//    	cdr = ConsultaSunat.getOlitwsconscpegem_2(longRuc, sunatConsulta.getCodComp(), nroDocRefPri);
//    	log.info("SUNAT getOlitwsconscpegem-2: "+cdr);
    }     
    
    private void getConsultaIntegradaSunat(){    
    	log.info("getConsultaIntegradaSunat: "+sunatConsulta.getNumRuc());		   	
    	Optional<SunatBeanResponse> opt = ConsultaIntegradaSunatRest.validarComprobante(sunatConsulta);
    	if(opt.isPresent()){ 
    		SunatBeanResponse sunatValidar = opt.get();
    		log.info("sunatValidar.getSuccess(): "+sunatValidar.getSuccess());	
    		log.info("sunatValidar.getMessage(): "+sunatValidar.getMessage());
    		log.info("sunatValidar.getErrorCode(): "+sunatValidar.getErrorCode());
    		log.info("sunatValidar.getData().getEstadoCp(): "+sunatValidar.getData().getEstadoCp());
    		log.info("sunatValidar.getData().getEstadoRuc(): "+sunatValidar.getData().getEstadoRuc());
    		log.info("sunatValidar.getData().getCondDomiRuc(): "+sunatValidar.getData().getCondDomiRuc());
    		log.info("sunatValidar.getData().getObservaciones(): "+sunatValidar.getData().getObservaciones());
    	}
    	Optional<SunatBeanResponse> optB = ConsultaIntegradaSunatRest.buscaCpeConsultaIntegrada(sunatConsulta);
    	if(optB.isPresent()){ 
    		SunatBeanResponse sunatValidar = optB.get();
    		log.info("ConsultaIntegrada sunatValidar.getSuccess(): "+sunatValidar.getSuccess());	
    		log.info("ConsultaIntegrada sunatValidar.getMessage(): "+sunatValidar.getMessage());
    		log.info("ConsultaIntegrada sunatValidar.getErrorCode(): "+sunatValidar.getErrorCode());
    		log.info("ConsultaIntegrada sunatValidar.getData().getEstadoCp(): "+sunatValidar.getData().getEstadoCp());
    		log.info("ConsultaIntegrada sunatValidar.getData().getEstadoRuc(): "+sunatValidar.getData().getEstadoRuc());
    		log.info("ConsultaIntegrada sunatValidar.getData().getCondDomiRuc(): "+sunatValidar.getData().getCondDomiRuc());
    		log.info("ConsultaIntegrada sunatValidar.getData().getObservaciones(): "+sunatValidar.getData().getObservaciones());
    	}
    	
    }  
}
