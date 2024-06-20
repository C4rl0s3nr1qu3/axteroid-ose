package com.axteroid.ose.server.tools.constantes;

import java.util.ArrayList;
import java.util.List;

public enum TipoTaskEnum {
	SEND_CDR("SC","SEND_CDR"),
	SEND_UBL("SU","SEND_UBL"),
    GET_CDR("GC","GET_CDR"),        
    PORTAL_CDR("PC","PORTAL_CDR"), 
    PORTAL_ALL("PA","PORTAL_ALL"),
    UPDATE_CPE("UC","UPDATE_CPE"),
    UPDATE_UBL("UU","UPDATE_UBL");
	private String codigo;
	private String descripcion;
	
	private TipoTaskEnum(String codigo, String descripcion){
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

    public static List<TipoTaskEnum> getTiposUsuario(){
        List<TipoTaskEnum> tipoTaskEnum = new ArrayList<TipoTaskEnum>();

        tipoTaskEnum.add(SEND_CDR);
        //tipoTaskEnum.add(ADMIN_EMPRESA);
        //tipoTaskEnum.add(ADMIN_GRUPO_EMPRERIAL);
        tipoTaskEnum.add(GET_CDR);
         return  tipoTaskEnum;
    }
}
