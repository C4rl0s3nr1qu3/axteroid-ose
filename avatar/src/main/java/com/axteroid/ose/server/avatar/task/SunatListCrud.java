package com.axteroid.ose.server.avatar.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.dao.InsertList2SunatPGDao;
import com.axteroid.ose.server.avatar.dao.impl.InsertList2SunatPGDaoImpl;
import com.axteroid.ose.server.avatar.thread.ListSunatThread;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.ComprobantesPagoElectronicosPK;
import com.axteroid.ose.server.jpa.model.SunatAutorizacionComprobPagoFisico;
import com.axteroid.ose.server.jpa.model.SunatAutorizacionComprobPagoFisicoPK;
import com.axteroid.ose.server.jpa.model.SunatCertificadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatCertificadoEmisorPK;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicos;
import com.axteroid.ose.server.jpa.model.SunatComprobantesPagoElectronicosPK;
import com.axteroid.ose.server.jpa.model.SunatContribuyente;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisor;
import com.axteroid.ose.server.jpa.model.SunatContribuyenteAsociadoEmisorPK;
import com.axteroid.ose.server.jpa.model.SunatCuadreDiarioDetalle;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyente;
import com.axteroid.ose.server.jpa.model.SunatPadronContribuyentePK;
import com.axteroid.ose.server.jpa.model.SunatParametro;
import com.axteroid.ose.server.jpa.model.SunatParametroPK;
import com.axteroid.ose.server.jpa.model.SunatPlazosExcepcionales;
import com.axteroid.ose.server.jpa.model.SunatPlazosExcepcionalesPK;
import com.axteroid.ose.server.tools.algoritmo.NcCrypt;
import com.axteroid.ose.server.tools.bean.Homologa;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoListaSUNATEnum;
import com.axteroid.ose.server.tools.constantes.TipoParametroEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class SunatListCrud {
	public static Logger log = LoggerFactory.getLogger(SunatListCrud.class);

	private InsertList2SunatPGDao insertList2TSPGDao = new InsertList2SunatPGDaoImpl();
		
	public void cargarListas(Integer tipLista, String rutaOseServerTxt){		
		// 1
		if(TipoListaSUNATEnum.ASOCIADOS.getCodigo().equals(tipLista))
			this.cargaListaAsociados(rutaOseServerTxt);
		// 2	
		if(TipoListaSUNATEnum.CERTIFICADOS.getCodigo().equals(tipLista))
			this.cargaListaCertificados(rutaOseServerTxt);
		// 3
		if(TipoListaSUNATEnum.CPE.getCodigo().equals(tipLista))
			this.cargaListaCPE(rutaOseServerTxt);
		// 4
		if(TipoListaSUNATEnum.CPF.getCodigo().equals(tipLista))
			this.cargaListaCPF(rutaOseServerTxt);
		// 5	
		if(TipoListaSUNATEnum.PARAMETROS.getCodigo().equals(tipLista))
			this.cargaListaParametros(rutaOseServerTxt);
		// 6	
		if(TipoListaSUNATEnum.PADRONES.getCodigo().equals(tipLista))		
			this.cargaListaPadrones(rutaOseServerTxt);
		// 7
		if(TipoListaSUNATEnum.CONTRIBUYENTES.getCodigo().equals(tipLista))			
			this.cargaListaContribuyentes(rutaOseServerTxt);
		// 8	
		if(TipoListaSUNATEnum.PLAZEXCEP.getCodigo().equals(tipLista))					
			this.cargaListaPlazExcep(rutaOseServerTxt);
		// 21
		if(TipoListaSUNATEnum.USUARIOS.getCodigo().equals(tipLista))					
			this.cargaListaParametroUsuario(rutaOseServerTxt);
		// 21
		if(TipoListaSUNATEnum.CUADREDETALLE.getCodigo().equals(tipLista))					
			this.cargaListaCuadreDiarioDetalle(rutaOseServerTxt);	
		// 22
		if(TipoListaSUNATEnum.HOMOLOGA.getCodigo().equals(tipLista))					
			this.cargaHomologa(rutaOseServerTxt);	
	}
	
	private final ThreadLocal<DateFormat> FORMATO_FECHA = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};
	
	public void cargaListaCertificados(String rutaOseServerTxt) {
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatCertificadoEmisor> listaCertificadoEmisor = new ArrayList<SunatCertificadoEmisor>();

			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				log.info("linea: " + linea);
				SunatCertificadoEmisorPK certificadoPk = new SunatCertificadoEmisorPK();
				certificadoPk.setNumRuc(Long.valueOf(informacion[0]));
				certificadoPk.setNumIdCa(informacion[1]);
				certificadoPk.setNumIdCd(informacion[2]);

				SunatCertificadoEmisor certificadoEmisor = new SunatCertificadoEmisor();
				certificadoEmisor.setSunatCertificadoEmisorPK(certificadoPk);
				String fecha = informacion[3];
				if(!informacion[3].isEmpty() && informacion[3].length()>10)
					fecha=informacion[3]+".0";
				certificadoEmisor.setFecAlta(FORMATO_FECHA.get().parse(fecha));
				String fecha1 = informacion[4];
				if(!informacion[4].isEmpty() && informacion[4].length()>10)
					fecha1=informacion[4]+".0";
				certificadoEmisor.setFecBaja(FORMATO_FECHA.get().parse(fecha1));
				certificadoEmisor.setUserCrea(Constantes.AVATAR_USER);
				certificadoEmisor.setFechaCrea(new Date());

				listaCertificadoEmisor.add(certificadoEmisor);
			}

			log.info("Registros encontrados: " + listaCertificadoEmisor.size());
			if (!listaCertificadoEmisor.isEmpty()) {
				int i=1;
				int j = listaCertificadoEmisor.size();
				for (SunatCertificadoEmisor certificadoEmisor : listaCertificadoEmisor) {
					insertList2TSPGDao.insertSunatCertificadoEmisor(certificadoEmisor);
					log.info("Certificado Emisor: " + i+" de "+j);
					i++;
				}
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCertificados Exception \n"+errors);
		}
	}
	
	public void cargaListaCPE(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			//List<SunatComprobantesPagoElectronicos> listaComprobantePagoElectronico = new ArrayList<SunatComprobantesPagoElectronicos>();
			List<ComprobantesPagoElectronicos> listaComprobantePagoElectronico = new ArrayList<ComprobantesPagoElectronicos>();

			while ((linea = bufferedReader.readLine()) != null) {
				//log.info("linea: " + linea);
				String[] informacion = linea.split("[|]");
				//SunatComprobantesPagoElectronicosPK comprobantePagoElectronicoPk = new SunatComprobantesPagoElectronicosPK();
				ComprobantesPagoElectronicosPK comprobantePagoElectronicoPk = new ComprobantesPagoElectronicosPK();
				comprobantePagoElectronicoPk.setNumRuc(Long.valueOf(informacion[0]));
				comprobantePagoElectronicoPk.setCodCpe(informacion[1]);
				comprobantePagoElectronicoPk.setNumSerieCpe(informacion[2]);
				//comprobantePagoElectronicoPk.setNumCpe(informacion[3]);
				comprobantePagoElectronicoPk.setNumCpe(Long.valueOf(informacion[3]));
				
				//SunatComprobantesPagoElectronicos comprobantePagoElectronico = new SunatComprobantesPagoElectronicos();
				//comprobantePagoElectronico.setSunatComprobantesPagoElectronicosPK(comprobantePagoElectronicoPk);
				ComprobantesPagoElectronicos comprobantePagoElectronico = new ComprobantesPagoElectronicos();
				comprobantePagoElectronico.setComprobantesPagoElectronicosPK(comprobantePagoElectronicoPk);
				comprobantePagoElectronico.setIndEstadoCpe(Short.valueOf(informacion[4]));
				comprobantePagoElectronico.setFecEmisionCpe(FORMATO_FECHA.get().parse(informacion[5]+".0"));
				comprobantePagoElectronico.setMtoImporteCpe(new BigDecimal(informacion[6]));
				comprobantePagoElectronico.setCodMonedaCpe(informacion[7]);
				comprobantePagoElectronico.setUserCrea(Constantes.AVATAR_USER);
				comprobantePagoElectronico.setFechaCrea(new Date());
				//System.out.println("informacion[8]:" + informacion[8].trim()+"-");
				if (informacion.length > 8 && informacion[8] != null && !informacion[8].trim().isEmpty() )
					comprobantePagoElectronico.setCodMotTraslado(Short.valueOf(informacion[8]));

				if (informacion.length > 9 && informacion[9] != null && !informacion[9].trim().isEmpty())
					comprobantePagoElectronico.setCodModTraslado(Short.valueOf(informacion[9]));

				if (informacion.length > 10 && informacion[10] != null && !informacion[10].trim().isEmpty())
					comprobantePagoElectronico.setIndTransbordo(Short.valueOf(informacion[10]));

				if (informacion.length > 11 && informacion[11] != null && !informacion[11].trim().isEmpty())
					comprobantePagoElectronico.setFecIniTraslado(FORMATO_FECHA.get().parse(informacion[11]+" 00:00:00.0"));

				listaComprobantePagoElectronico.add(comprobantePagoElectronico);
			}

			log.info("Registros encontrados: " + listaComprobantePagoElectronico.size());
			if (!listaComprobantePagoElectronico.isEmpty()) {
				int i=1;
				int j = listaComprobantePagoElectronico.size();
//				for (SunatComprobantesPagoElectronicos comprobantePagoElectronico : listaComprobantePagoElectronico) {
//					insertList2TSPGDao.insertSunatComprobantePagoElectronico(comprobantePagoElectronico);
//					log.info("CPE: " + i+" de "+j);
//					i++;
//				}
				for (ComprobantesPagoElectronicos comprobantePagoElectronico : listaComprobantePagoElectronico) {
					insertList2TSPGDao.insertComprobantePagoElectronico(comprobantePagoElectronico);
					log.info("CPE: " + i+" de "+j);
					i++;
				}
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCPE Exception \n"+errors);
		}
	}
	
	public void cargaListaCPF(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatAutorizacionComprobPagoFisico> listaComprobantePagoFisico = new ArrayList<SunatAutorizacionComprobPagoFisico>();

			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				SunatAutorizacionComprobPagoFisicoPK comprobantePagoFisicoPk = new SunatAutorizacionComprobPagoFisicoPK();
				comprobantePagoFisicoPk.setNumRuc(Long.valueOf(informacion[0]));
				comprobantePagoFisicoPk.setCodCpe(informacion[1]);
				comprobantePagoFisicoPk.setNumSerieCpe(informacion[2]);
				comprobantePagoFisicoPk.setNumIniCpe(Integer.valueOf(informacion[3]));

				SunatAutorizacionComprobPagoFisico comprobantePagoFisico = new SunatAutorizacionComprobPagoFisico();
				comprobantePagoFisico.setSunatAutorizacionComprobPagoFisicoPK(comprobantePagoFisicoPk);
				comprobantePagoFisico.setNumFinCpe(Integer.valueOf(informacion[4]));
				comprobantePagoFisico.setUserCrea(Constantes.AVATAR_USER);
				comprobantePagoFisico.setFechaCrea(new Date());

				listaComprobantePagoFisico.add(comprobantePagoFisico);
			}

			log.info("Registros encontrados: " + listaComprobantePagoFisico.size());
			if (!listaComprobantePagoFisico.isEmpty()) {
				int i=1;
				int j = listaComprobantePagoFisico.size();
				for (SunatAutorizacionComprobPagoFisico comprobantePagoFisico : listaComprobantePagoFisico) {
					insertList2TSPGDao.insertSunatComprobantePagoFisico(comprobantePagoFisico);
					log.info("Aut CPF: " + i+" de "+j);
					i++;
				}
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCPF Exception \n"+errors);
		}
	}
	
	public void cargaListaContribuyentes(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatContribuyente> listaContribuyente = new ArrayList<SunatContribuyente>();

			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");

				SunatContribuyente contribuyente = new SunatContribuyente();
				if(!StringUtil.hasString(informacion[0]))
					continue;
				contribuyente.setNumRuc(Long.valueOf(informacion[0]));
				contribuyente.setIndEstado(informacion[1]);
				contribuyente.setIndCondicion(informacion[2]);
				contribuyente.setUserCrea(Constantes.AVATAR_USER);
				contribuyente.setFechaCrea(new Date());

				listaContribuyente.add(contribuyente);
			}

			log.info("Registros encontrados: " + listaContribuyente.size());
			if (!listaContribuyente.isEmpty()) {
				int i=1;
				int j = listaContribuyente.size();
				for (SunatContribuyente contribuyente : listaContribuyente) {
					insertList2TSPGDao.insertSunatContribuyente(contribuyente);
					log.info("Contribuyente: " + i+" de "+j);
					i++;
				}
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaContribuyentes Exception \n"+errors);
		}

	}
	
	public void cargaListaAsociados(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatContribuyenteAsociadoEmisor> listaContribuyenteEmisor = new ArrayList<SunatContribuyenteAsociadoEmisor>();
			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				SunatContribuyenteAsociadoEmisorPK contribuyenteEmisorPk = new SunatContribuyenteAsociadoEmisorPK();
				contribuyenteEmisorPk.setNumRuc(Long.valueOf(informacion[0]));
				contribuyenteEmisorPk.setNumRucAsociado(Long.valueOf(informacion[1]));
				contribuyenteEmisorPk.setIndTipAsociacion(Short.valueOf(informacion[2]));
				SunatContribuyenteAsociadoEmisor contribuyenteEmisor = new SunatContribuyenteAsociadoEmisor();
				contribuyenteEmisor.setTsContribuyenteAsociadoEmisorPK(contribuyenteEmisorPk);
				contribuyenteEmisor.setFecInicio(FORMATO_FECHA.get().parse(informacion[3]+" 00:00:00.0"));
				contribuyenteEmisor.setFecFin(FORMATO_FECHA.get().parse(informacion[4]+" 00:00:00.0"));
				contribuyenteEmisor.setUserCrea(Constantes.AVATAR_USER);
				contribuyenteEmisor.setFechaCrea(new Date());
				listaContribuyenteEmisor.add(contribuyenteEmisor);
			}
			log.info("Registros encontrados: " + listaContribuyenteEmisor.size());
			if (!listaContribuyenteEmisor.isEmpty()) {
				int i=1;
				int j = listaContribuyenteEmisor.size();
				for (SunatContribuyenteAsociadoEmisor contribuyenteEmisor : listaContribuyenteEmisor) {
					insertList2TSPGDao.insertSunatContribuyenteEmisor(contribuyenteEmisor);
					log.info("Contribuyentes Asociados: " + i+" de "+j);
					i++;
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaAsociados Exception \n"+errors);
		}
	}
	
	public void cargaListaPadrones(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatPadronContribuyente> listaPadronContribuyente = new ArrayList<SunatPadronContribuyente>();
			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				SunatPadronContribuyentePK padronContribuyentePk = new SunatPadronContribuyentePK();
				padronContribuyentePk.setNumRuc(Long.valueOf(informacion[0]));
				padronContribuyentePk.setIndPadron(informacion[1]);
				SunatPadronContribuyente padronContribuyente = new SunatPadronContribuyente();
				padronContribuyente.setTsPadronContribuyentePK(padronContribuyentePk);
				padronContribuyente.setUserCrea(Constantes.AVATAR_USER);
				padronContribuyente.setFechaCrea(new Date());
				listaPadronContribuyente.add(padronContribuyente);
			}
			log.info("Registros encontrados: " + listaPadronContribuyente.size());
			if (!listaPadronContribuyente.isEmpty()) {
				int i=1;
				int j = listaPadronContribuyente.size();
				for (SunatPadronContribuyente padronContribuyente : listaPadronContribuyente) {
					insertList2TSPGDao.insertSunatPadronContribuyente(padronContribuyente);
					log.info("Padron Contribuyente: " + i+" de "+j);
					i++;
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaPadrones Exception \n"+errors);
		}
	}
	
	public void cargaListaParametros(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatParametro> listaParametro = new ArrayList<SunatParametro>();
			int k = 1;
			while ((linea = bufferedReader.readLine()) != null) {
				log.info("Leyendo linea {}: ",k , linea);
				String[] informacion = linea.split("[|]");
				SunatParametroPK parametroPk = new SunatParametroPK();
				parametroPk.setCodParametro(informacion[0]);
				parametroPk.setCodArgumento(informacion[1]);
				SunatParametro parametro = new SunatParametro();
				parametro.setSunatParametroPK(parametroPk);
				parametro.setDesArgumento(informacion[2]);
				parametro.setUserCrea(Constantes.AVATAR_USER);
				parametro.setFechaCrea(new Date());
				listaParametro.add(parametro);
				k++;
			}
			log.info("Registros encontrados: " + listaParametro.size());
			if (!listaParametro.isEmpty()) {
				int i=1;
				int j = listaParametro.size();
				for (SunatParametro parametro : listaParametro) {
					insertList2TSPGDao.insertSunatParametro(parametro);
					log.info("Parametro: " + i+" de "+j);
					i++;
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaParametros Exception \n"+errors);
		}		
	}
	
	public void cargaListaParametroUsuario(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatParametro> listaParametro = new ArrayList<SunatParametro>();
			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				SunatParametroPK parametroPk = new SunatParametroPK();
				parametroPk.setCodParametro(informacion[0]);
				parametroPk.setCodArgumento(informacion[1]);
				SunatParametro parametro = new SunatParametro();
				parametro.setSunatParametroPK(parametroPk);
				parametro.setDesArgumento(informacion[2]);
				if(parametroPk.getCodParametro().equals(TipoParametroEnum.SEC.getCodigo())) {
					String clave =NcCrypt.encriptarPassword(informacion[2]);
					parametro.setDesArgumento(clave);
				}
				parametro.setUserCrea(Constantes.AVATAR_USER);
				parametro.setFechaCrea(new Date());
				log.info("parametroPk.getCodArgumento(): " + parametroPk.getCodArgumento());
				listaParametro.add(parametro);
			}
			log.info("Registros encontrados: " + listaParametro.size());
			if (!listaParametro.isEmpty()) {
				int i=1;
				int j = listaParametro.size();
				for (SunatParametro parametro : listaParametro) {
					insertList2TSPGDao.insertSunatParametro(parametro);
					log.info("Parametro: " + i+" de "+j);
					i++;
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaParametroUsuario Exception \n"+errors);
		}		
	}	
	
	public void cargaListaPlazExcep(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("Leyendo Archivo: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			List<SunatPlazosExcepcionales> listaOse = new ArrayList<SunatPlazosExcepcionales>();
			while ((linea = bufferedReader.readLine()) != null) {
				String[] informacion = linea.split("[|]");
				SunatPlazosExcepcionalesPK tsPlazosExcepcionalesPk = new SunatPlazosExcepcionalesPK();
				tsPlazosExcepcionalesPk.setNumRuc(Long.valueOf(informacion[0]));
				tsPlazosExcepcionalesPk.setCodCpe(informacion[1]);
				tsPlazosExcepcionalesPk.setFecEmision(FORMATO_FECHA.get().parse(informacion[2]+" 00:00:00.0"));
				SunatPlazosExcepcionales tsPlazosExcepcionales = new SunatPlazosExcepcionales();
				tsPlazosExcepcionales.setTsPlazosExcepcionalesPK(tsPlazosExcepcionalesPk);
				tsPlazosExcepcionales.setFecLimite(FORMATO_FECHA.get().parse(informacion[3]+" 00:00:00.0"));
				tsPlazosExcepcionales.setUserCrea(Constantes.AVATAR_USER);
				tsPlazosExcepcionales.setFechaCrea(new Date());
				listaOse.add(tsPlazosExcepcionales);
			}
			log.info("Registros encontrados: " + listaOse.size());
			if (!listaOse.isEmpty()) {
				int i=1;
				int j = listaOse.size();
				for (SunatPlazosExcepcionales tsPlazosExcepcionales : listaOse) {
					insertList2TSPGDao.insertSunatPlazosExcepcionales(tsPlazosExcepcionales);
					log.info("Plazos Excepcionales: " + i+" de "+j);
					i++;
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaPlazExcep Exception \n"+errors);
		}
	}		
	
	public void cargaListaCuadreDiarioDetalle(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("cargaListaCuadreDiarioDetalle: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			int dividendo=1;
			int divisor=20;
			while ((linea = bufferedReader.readLine()) != null) {				
				log.info(dividendo+") " + linea);
				ListSunatThread listSunatThread = new ListSunatThread(linea);
				listSunatThread.start();	
				dividendo++;
				int residuo=dividendo%divisor;
				if(residuo==0)		
					Thread.sleep(1500);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCuadreDiarioDetalle Exception \n"+errors);
		}
	}
	
	public void cargaListaCuadreDiraioDetalle_2(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("cargaListaCuadreDiraioDetalle_2: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			int i=1;
			while ((linea = bufferedReader.readLine()) != null) {				
				String[] informacion = linea.split("[|]");
				SunatCuadreDiarioDetalle tsCuadreDiarioDetalle = new SunatCuadreDiarioDetalle();
				tsCuadreDiarioDetalle.setRucEmisor(Long.valueOf(informacion[0]));
				tsCuadreDiarioDetalle.setTipoComprobante(informacion[1]);
				tsCuadreDiarioDetalle.setSerie(informacion[2]);
				tsCuadreDiarioDetalle.setNumeroCorrelativo(informacion[3]);
				tsCuadreDiarioDetalle.setEstadoFinal(informacion[4]);
				String fecha = informacion[5]+" "+informacion[6];
				tsCuadreDiarioDetalle.setFechaRecepcion(DateUtil.parseDate(fecha, "dd/MM/yyyy HH:mm:ss"));
				tsCuadreDiarioDetalle.setHoraRecepcion(informacion[6]);
				insertList2TSPGDao.insertSunatCuadreDiarioDetalle(tsCuadreDiarioDetalle);				
				log.info("N° CDD: " + i);
				i++;
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCuadreDiraioDetalle_2 Exception \n"+errors);
		}
	}	
	
	public void cargaHomologa(String rutaOseServerTxt) {		
		File archivo = new File(rutaOseServerTxt);
		log.info("cargaHomologa: " + rutaOseServerTxt);
		if(!archivo.exists())
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			int i=1;
			while ((linea = bufferedReader.readLine()) != null) {				
				String[] informacion = linea.split("[|]");
				Homologa homologa = new Homologa();				   
				homologa.setCodigo_Respuesta(informacion[0]);
				homologa.setEstado_Respuesta(informacion[1]);
				homologa.setCodigo_Esperado(informacion[2]);
				homologa.setEstado_Esperado(informacion[3]);
				homologa.setA_nivel_de_Codigo(informacion[4]);
				homologa.setA_nivel_de_Estado(informacion[5]);
				homologa.setResultado_Subida(informacion[6]);
				homologa.setEvaluación_INSI(informacion[7]);
				homologa.setE_RazonSocial(informacion[8]);
				homologa.setE_RUC(informacion[9]);
				homologa.setE_Usuario(informacion[10]);
				homologa.setE_Clave(informacion[11]);
				homologa.setE_RutaXML(informacion[12]);
				homologa.setE_WSDL(informacion[13]);
				homologa.setEstado_value_out(informacion[14]);
				homologa.setTicket(informacion[15]);
				homologa.setE_metodo(informacion[16]);
				homologa.setTipo_de_Documento(informacion[17]);
				homologa.setComprobante(informacion[18]);				
				
				//insertList2TSPGDao.insertHomologa(homologa);				
				log.info("N° {} | Documento: {}", i,homologa.getComprobante());
				i++;
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaHomologa Exception \n"+errors);
		}
	}		
}
