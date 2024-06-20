package com.axteroid.ose.server.repository.rules.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.bean.OseResponse;
import com.axteroid.ose.server.tools.exception.InvalidDocumentException;

public class SunatDocumentsController {
	private static final Logger log = LoggerFactory.getLogger(SunatDocumentsController.class);
	

	private SunatDocumentsRepository sunatDocumentsRepository = new SunatDocumentsRepository() ;
	
	public List<OseResponse> getDocumentGroup(String grupo) {
		log.info("getDocumentGroup");
		if (StringUtils.isBlank(grupo))
			throw new InvalidDocumentException("El valor GRUPO no puede ser vacio o nulo");
		return sunatDocumentsRepository.getDocumentGroup(grupo);
	}	
	
//	@GetMapping(path = "/oseSunatStatus/{ESTADO}")
//	public ResponsePopulationDataList getDocumentStatus(@PathVariable("ESTADO") String status) {
//		log.info("getDocumentStatus");
//		if (StringUtils.isBlank(status))
//			throw new InvalidDocumentException("El valor ESTADO no puede ser vacio o nulo");	
//		ResponsePopulationDataList responsePopulationDataList = new ResponsePopulationDataList();
//		responsePopulationDataList.setPopulationDataList(sunatDocumentsRepository.getDocumentStatus(status));
//		return responsePopulationDataList;
//	}	
	
//	@PostMapping(path = "/oseSunatDataQuery", consumes =  MediaType.APPLICATION_JSON_VALUE, 
//			produces =  MediaType.APPLICATION_JSON_VALUE)
//	public ResponsePopulationDataList getDocumentDataQuery(@RequestBody DataQuery dataQuery) {
//		log.info("getDocumentDataQuery");	
//		ResponsePopulationDataList responsePopulationDataList = new ResponsePopulationDataList();
//		if(dataQuery.getRepeat()!= null && dataQuery.getRepeat().trim().equals("active"))
//			responsePopulationDataList.setPopulationDataList(sunatDocumentsRepository.getDocumentDataQueryUbl(dataQuery));
//		else
//			responsePopulationDataList.setPopulationDataList(sunatDocumentsRepository.getDocumentDataQuery(dataQuery));
//		return responsePopulationDataList;
//	}
//	
//	@PostMapping(path = "/oseSunatS3File/{RUC}", consumes =  MediaType.APPLICATION_JSON_VALUE, 
//			produces =  MediaType.APPLICATION_JSON_VALUE)
//	public ResponseS3FileList getS3File(@PathVariable("RUC") String ruc, @RequestBody List<S3File> s3FileList) {
//		log.info("getS3File");
//		ResponseS3FileList responseS3FileList = new ResponseS3FileList();
//		responseS3FileList.setS3Filelist(sunatDocumentsRepository.getS3File(s3FileList));
//		return responseS3FileList;
//	}			
//	
//	@PutMapping(path = "/oseSunatResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void updateSunatResponse(@RequestBody UpdateSunatResponseBean upd) {
//		log.info("updateSunatResponse");
//		sunatDocumentsRepository.updateSunatResponse(upd);
//	}	
}
