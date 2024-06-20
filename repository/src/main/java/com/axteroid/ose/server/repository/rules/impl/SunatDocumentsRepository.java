package com.axteroid.ose.server.repository.rules.impl;

import java.util.ArrayList;
import java.util.List;

import javax.management.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.repository.builder.S3CrudBuild;
import com.axteroid.ose.server.tools.bean.DataQuery;
import com.axteroid.ose.server.tools.bean.FileSynchro;
import com.axteroid.ose.server.tools.bean.OseResponse;
import com.axteroid.ose.server.tools.bean.PopulationData;
import com.axteroid.ose.server.tools.bean.S3File;
import com.axteroid.ose.server.tools.bean.SunatBeanUpdateResponse;
import com.axteroid.ose.server.tools.bean.ZipFile;
import com.axteroid.ose.server.tools.exception.InvalidDocumentException;


public class SunatDocumentsRepository {

	private int limit;	
	private S3CrudBuild s3Repository = new S3CrudBuild();
	private String bucketName;	
	
	private static final Logger log = LoggerFactory.getLogger(SunatDocumentsRepository.class);	
		
	public static enum FileType{		
		UBL("XML_UBL"),
		CDR("XML_CDR"),
		SUNAT("SUNAT_CDR");		
		private String val;
		
		private FileType(String _val) {
			val = _val;
		}
		
		public String getProperyName() {
			return val;
		}		
	}	
	
	private String getNameCollection(String documentType) {		
		switch(documentType) {			
		case "01":
			return "TT_OSE_INVOICE_RESPONSE";		
		case "03":
			return "TT_OSE_VOUCHER_RESPONSE";			
		case "07":
			return "TT_OSE_CREDIT_NOTE_RESPONSE";			
		case "08":
			return "TT_OSE_DEBIT_NOTE_RESPONSE";			
		case "09":
			return "TT_OSE_DESPATCH_ADVICE_RESPONSE";			
		case "20":
			return "TT_OSE_RETENTION_RESPONSE";			
		case "40":
			return "TT_OSE_PERCEPTION_RESPONSE";			
		case "RA":
			return "TT_OSE_CANCEL_SUMMARY_RESPONSE";			
		case "RC":
			return "TT_OSE_VOUCHER_SUMMARY_RESPONSE";			
		case "RR":
			return "TT_OSE_REVERTION_SUMMARY_RESPONSE";		
		default:
			throw new RuntimeException("El tipo de documento "+documentType +" no esta permitido");
		}
	}	
	
	public List<OseResponse> getDocumentGroup(String grupo) {	
		log.info("getDocumentGroup");
		
//		MongoTemplate template = appContext.getBean("noTransactionalTemplate",MongoTemplate.class );
//		
//		Aggregation agg = newAggregation(
//				match(Criteria.where("ESTADO").gte(grupo)),
//				match(Criteria.where("NUEVA_OSE").is(true)),
//				group("ESTADO").count().as("1"),
//				project("total").and("hosting").previousOperation(),
//				sort(Sort.Direction.DESC, "ID")
//			);
//		
//		AggregationResults<OseResponse> groupResults
//		= template.aggregate(agg, InvoiceResponse.class, OseResponse.class);		
//		List<OseResponse> result = groupResults.getMappedResults();
//		return result;
		
		return null;
		//String collection = this.getNameCollection("01");		
		//Query q = new Query();
		//q = q.addCriteria(Criteria.where("ESTADO").is(estado));
		//Collation c = new Collation(collection);
		//q = q.collation(c)	;		
		//if(!mongoTemplate.exists(q, collection)) {
		//	throw new EmptyResultDataAccessException(1);
		//}		
		//return mongoTemplate.findOne(q, OseResponse.class,collection);
				
	}	
		
	public List<PopulationData> getDocumentDataQueryUbl(DataQuery dataQuery) {
		log.info("getDocumentDataQueryUbl {}-{}-{}-{}",dataQuery.getRucEmisor(),dataQuery.getTipoComprobante(), 
					dataQuery.getSerie(),dataQuery.getNumeroCorrelativo());		
		List<PopulationData> listPopulationData = new ArrayList<PopulationData>();	
		int iRows = Integer.parseInt(dataQuery.getFilas());
		for(String collection : dataQuery.getCollection()){	
			log.info("collection: {} | rows: {}", collection, iRows);						
			List<Document> ids =  this.getDocumentUbl(dataQuery, iRows, collection);
			if (ids.size() == 0)
				return listPopulationData;
			log.info("Size {} - {}", ids.size(), collection);
			ids.forEach(id -> {
				//log.info("Processing id: {}", id.get("ID"));
				Long idL = 0l;
				String idS = String.valueOf(id.get("ID"));
				if((idS != null) && (!idS.isEmpty())) {
					if(this.hasString(idS))
						idL = Long.parseLong(idS);
				}
				PopulationData populationData = new PopulationData();
				populationData.setId(idL);
				populationData.setIssuer(String.valueOf(id.get("RUC_EMISOR")));
				populationData.setDocumentType(String.valueOf(id.get("TIPO_COMPROBANTE")));
				populationData.setSerial(String.valueOf(id.get("SERIE")));
				Long nc = Long.parseLong(String.valueOf(id.get("NUMERO_CORRELATIVO")));
				populationData.setCorrelative(nc);
				populationData.setCollection(collection);
				listPopulationData.add(populationData);
			});	
			
		}	
		return listPopulationData;
	}
	
	public List<Document> getDocumentUbl(DataQuery dataQuery, int iRows, String collection) {	
		log.info("getDocumentUbl {}-{}-{}-{}",dataQuery.getRucEmisor(),dataQuery.getTipoComprobante(),
				dataQuery.getSerie(),dataQuery.getNumeroCorrelativo());
//		MongoTemplate template = appContext.getBean("noTransactionalTemplate",MongoTemplate.class );
		List<Document> listDocument = new ArrayList<Document>();
		try {
			Query query = new Query();
//			query.limit(iRows);
//			query.fields().include("ID");
//			query.fields().include("RUC_EMISOR");
//			query.fields().include("TIPO_COMPROBANTE");
//			query.fields().include("SERIE");
//			query.fields().include("NUMERO_CORRELATIVO");
//			if(dataQuery.getRucEmisor()!=null)
//				query.addCriteria(Criteria.where("RUC_EMISOR").is(dataQuery.getRucEmisor()));	
//			if(dataQuery.getTipoComprobante()!=null)
//				query.addCriteria(Criteria.where("TIPO_COMPROBANTE").is(dataQuery.getTipoComprobante()));	
//			if(dataQuery.getSerie()!=null)
//				query.addCriteria(Criteria.where("SERIE").is(dataQuery.getSerie()));	
//			if(dataQuery.getNumeroCorrelativo()!=null)
//				query.addCriteria(Criteria.where("NUMERO_CORRELATIVO").is(new Long(dataQuery.getNumeroCorrelativo())));
//			query.addCriteria(Criteria.where("NUEVA_OSE").is(true));
//			if(!template.exists(query, collection)) {
//				throw new EmptyResultDataAccessException(1);
//			}
//			listDocument= template.find(query, Document.class, collection);
//			listDocument.forEach(std -> log.info("std: "+std));
			return listDocument;
		}catch(Exception e){
			log.error(e.toString());
		}
		return listDocument;
	}	
	
	public List<PopulationData> getDocumentDataQuery(DataQuery dataQuery) {
		log.info("getDocumentDataQuery (1)");		
		List<PopulationData> listPopulationData = new ArrayList<PopulationData>();	
		int iRows = Integer.parseInt(dataQuery.getFilas());
		for(String collection : dataQuery.getCollection()){	
			log.info("collection: {} | rows: {}", collection, iRows);
			String[] status = dataQuery.getEstado().split(",");
			for(int i=0;i< status.length; i++) {				
				if(dataQuery.getRespuestaSunat()!=null) {					
					String[] responseSunat = dataQuery.getRespuestaSunat().split(",");	
					for(int j=0;j< responseSunat.length; j++) {
						log.info("collection: {} | status: {} | response: {}", collection, status[i],responseSunat[j]);
						this.getDocumentStatusResponse(status[i], collection, iRows, responseSunat[j], listPopulationData);
					}
				}else {		
					log.info("collection: {} | status: {}", collection, status[i]);
					this.getDocumentStatus(status[i], collection, iRows, listPopulationData);
				}
			}
		}	
		return listPopulationData;
	}
	
	private void getDocumentStatusResponse(String status, String collection, int iRows, String responseSunat,
			List<PopulationData> listPopulationData){
		log.info("getDocumentStatusResponse: {} | {} | {} | {} | {}",status, collection, iRows, responseSunat, listPopulationData.toString());
		List<Document> ids =  this.getDocumentDataQuery(status, collection, iRows, responseSunat);
		if (ids.size() == 0)
			return;
		log.info("Size {} - {}", ids.size(), collection);
		ids.forEach(id -> {
			//log.info("Processing id: {}", id.get("ID"));
			Long idL = 0l;
			String idS = String.valueOf(id.get("ID"));
			if((idS != null) && (!idS.isEmpty())) {
				if(this.hasString(idS))
					idL = Long.parseLong(idS);
			}
			PopulationData populationData = new PopulationData();
			populationData.setId(idL);
			populationData.setIssuer(String.valueOf(id.get("RUC_EMISOR")));
			populationData.setDocumentType(String.valueOf(id.get("TIPO_COMPROBANTE")));
			populationData.setSerial(String.valueOf(id.get("SERIE")));
			Long nc = Long.parseLong(String.valueOf(id.get("NUMERO_CORRELATIVO")));
			populationData.setCorrelative(nc);
			populationData.setCollection(collection);
			listPopulationData.add(populationData);
		});				
	}
	
	private void getDocumentStatus(String status, String collection, int iRows, 
			List<PopulationData> listPopulationData){
		log.info("getDocumentStatus: {} | {} | {} | {} ",status, collection, iRows, listPopulationData.toString());
		List<Document> ids =  this.getDocumentDataQuery(status, collection, iRows, null);
		if (ids.size() == 0)
			return;
		log.info("Size {} - {}", ids.size(), collection);
		ids.forEach(id -> {
			//log.info("Processing id: {}", id.get("ID"));
			Long idL = 0l;
			String idS = String.valueOf(id.get("ID"));
			if((idS != null) && (!idS.isEmpty())) {
				if(this.hasString(idS))
					idL = Long.parseLong(idS);
			}
			PopulationData populationData = new PopulationData();
			populationData.setId(idL);
			populationData.setIssuer(String.valueOf(id.get("RUC_EMISOR")));
			populationData.setDocumentType(String.valueOf(id.get("TIPO_COMPROBANTE")));
			populationData.setSerial(String.valueOf(id.get("SERIE")));
			Long nc = Long.parseLong(String.valueOf(id.get("NUMERO_CORRELATIVO")));
			populationData.setCorrelative(nc);
			populationData.setCollection(collection);
			listPopulationData.add(populationData);
		});		
	}
	
	private List<Document> getDocumentDataQuery(String status, String collection, int iRows, String responseSunat) {	
		log.info("getDocumentDataQuery: {} | {} | {} | {}",status, collection, iRows, responseSunat);
//		MongoTemplate template = appContext.getBean("noTransactionalTemplate",MongoTemplate.class );
		List<Document> listDocument = new ArrayList<Document>();
		try {
//			Query query = new Query();			
//			query.limit(iRows);
//			query.fields().include("ID");
//			query.fields().include("RUC_EMISOR");
//			query.fields().include("TIPO_COMPROBANTE");
//			query.fields().include("SERIE");
//			query.fields().include("NUMERO_CORRELATIVO");
//			//query = 
//			query.addCriteria(Criteria.where("ESTADO").is(status));		
//			if(responseSunat!=null)
//				query.addCriteria(Criteria.where("SUNAT_CDR.responseSunat").is(responseSunat));
//			//query.addCriteria(Criteria.where("NUEVA_OSE").is(true));
//			query.with(Sort.by("FECHA_CREA").ascending());
//			if(!template.exists(query, collection)) {
//				throw new EmptyResultDataAccessException(1);
//			}			
//			listDocument= template.find(query, Document.class, collection);
//			listDocument.forEach(std -> log.info("std: "+std));
			return listDocument;
		}catch(Exception e){
			log.error(e.toString());
		}
		return listDocument;
	}		
	
	public List<S3File> getS3File(List<S3File> s3FileList){
		log.info("getS3File");
		try {	
			List<S3File> s3FileList2 = new ArrayList<S3File>();
			for(S3File s3File : s3FileList) {
//				final S3Object s3object = clientS3.getObject(new GetObjectRequest(bucketName, s3File.getFilePath()));	
//				S3ObjectInputStream s3ObjectInputStream = s3object.getObjectContent();
//				byte[] fileByte = IOUtils.toByteArray(s3ObjectInputStream);			
//				//String bfile = new String(fileByte);
//				//log.info("bfile: {} | {} | {}", s3File.getFilePath(), s3File.getFileMD5(), bfile); 
//				s3File.setFileContent(fileByte);
//				s3FileList2.add(s3File);
			}
			return s3FileList2;					
		} catch (Exception e) {
			throw new InvalidDocumentException(e.toString());
		}	
	}	
		
	@Transactional
	public void updateSunatResponse(@Valid SunatBeanUpdateResponse updateSunatResponseBean) {
		log.info("updateSunatResponse");
		FileSynchro fileSynchro = generateSynchroBean(updateSunatResponseBean.getIssuerCode(), updateSunatResponseBean.getDocumentType(), 
				updateSunatResponseBean.getSerial(), updateSunatResponseBean.getSunatCdr());
//		Map<String,Object> mapa=updateSunatResponseBean.getSunatMap();		
//		if(fileSynchro.getCDRSunat()!=null) {
//			mapa.put("storagePath", fileSynchro.getCDRSunat().getFilePath());
//			mapa.put("contentMD5", fileSynchro.getCDRSunat().getFileMD5());
//			mapa.put("fileName", updateSunatResponseBean.getSunatCdr().getFileName());
//		}		
//				
//		String collection = getNameCollection(updateSunatResponseBean.getDocumentType());
//		
//		Query q = new Query().addCriteria(Criteria.where("RUC_EMISOR").is(updateSunatResponseBean.getIssuerCode()))
//				.addCriteria(Criteria.where("TIPO_COMPROBANTE").is(updateSunatResponseBean.getDocumentType()))
//				.addCriteria(Criteria.where("SERIE").is(updateSunatResponseBean.getSerial()))
//				.addCriteria(Criteria.where("NUMERO_CORRELATIVO").is(updateSunatResponseBean.getSequence()));
//		
//		if (log.isTraceEnabled()) {
//			log.trace("{}",q);
//			log.trace("Coleccion: {}",collection);
//		}
//		
//		if(!mongoTemplate.exists(q, collection)) {
//			throw new InvalidDocumentException("No existe el documento");
//		}
//				
//		Update u = new Update().set("ESTADO", updateSunatResponseBean.getStatus());
//		u.set(FileType.SUNAT.getProperyName(), mapa);
//		if((updateSunatResponseBean.getStatus()!=null) && updateSunatResponseBean.getStatus().equals("80")){
//			u.set("TICKET_SUNAT", updateSunatResponseBean.getSunatTicket());
//		}			
//		mongoTemplate.findAndModify(q, u, new FindAndModifyOptions().returnNew(true), OseResponse.class, collection);			
	}	
	
	private FileSynchro  generateSynchroBean(String RUC, String documentType, String serial, @Valid ZipFile cdrsunat) {
		log.info("generateSynchroBean");
		FileSynchro fileSynchro = new FileSynchro();				
		if((cdrsunat!=null) && (cdrsunat.getFileContent()!=null)
				&& (cdrsunat.getFileContent().length>0)) {
			String path = documentType.concat("/").concat(RUC).concat("/").concat(FileType.SUNAT.val).concat("/")
					.concat(cdrsunat.getFileName());		
//			String fileMD5 = s3Repository.setFileZip(path, cdrsunat.getFileContent());
//			S3File cdrSunat = new S3File();
//			cdrSunat.setFilePath(path);
//			cdrSunat.setFileContent(cdrsunat.getFileContent());
//			cdrSunat.setFileMD5(fileMD5);
//			//resp.getCDRSunat().setFileMD5(calculateMD5(cdrsunat.getFileContent()));
//			fileSynchro.setCDRSunat(cdrSunat);
		}					
		return fileSynchro;		
	}
	
//	private String calculateMD5(byte[] data) {		
//		MessageDigest md5=null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//
//		}		
//		byte[] resp = DigestUtils.digest(md5, data);				
//		return new String(Base64.encodeBase64(resp));		
//	}	
	
    public boolean hasString(String str){
    	boolean b = false;
    	if(str!=null){
    		String [] digito = {"1","2","3","4","5","6","7","8","9","0"};
    		int id = digito.length;
    		int in = str.length();		
    		for(int j=0; j<in; j++){
    			String chtr = str.substring(j, j+1);
    			b = false;
    			for(int i=0; i<id; i++){	
    				if(chtr.equals(digito[i])) {
    					b = true;
    					break;
    				}							
    			}			
    			if(b) continue;
    			break;
    		}
    	}
		return b;
    }
}
