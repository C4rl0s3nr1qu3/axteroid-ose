package com.axteroid.ose.server.rulesejb.dao;

import javax.ejb.Local;

import com.axteroid.ose.server.jpa.model.Documento;

@Local
public interface SunatPadronContribuyenteDAOLocal {
	public void getPadronContribuyente_RP(Documento tbComprobante);
	public void getPadronContribuyente_Proveedor(Documento tbComprobante, Long documentoProveedor);
	public void getPadronContribuyente_Cliente(Documento tbComprobante, Long documentoCliente);
	public void getPadronContribuyente_Cliente_Percepcion(Documento tbComprobante, Long documentoCliente);
	public void getPadronContribuyente_RC_Cliente(Documento tbComprobante, Long documentoCliente) ;
	public void getPadronContribuyente_FBNCND(Documento tbComprobante) ;
	public void getPadronContribuyente_F(Documento tbComprobante); 
	public void getPadronContribuyente_FB(Documento tbComprobante);
	public void getPadronContribuyenteCustomer(Documento tbComprobante, String rucCustomer );
	public void getPadronContribuyenteSupplier(Documento tbComprobante, String rucSupplier );

}
