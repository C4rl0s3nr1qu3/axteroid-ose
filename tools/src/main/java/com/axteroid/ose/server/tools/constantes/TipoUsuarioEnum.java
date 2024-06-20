package com.axteroid.ose.server.tools.constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public enum TipoUsuarioEnum implements Serializable{
	OSE_AVATAR("BR",Constantes.AVATAR_USER),
    ADMIN_GRUPO_EMPRERIAL("BA","label_tipoUsuario_adminGrupo"),
    ADMIN_EMPRESA("EA","label_tipoUsuario_adminEmpresa"),
    USUARIO("RU","label_tipoUsuario_usuario");

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String tipoUsuario;
    private String nombre;

    TipoUsuarioEnum(String tipoUsuario, String nombre){
         this.tipoUsuario=tipoUsuario;
         this.nombre=nombre;
    }

    public static List<TipoUsuarioEnum> getTiposUsuario(){
        List<TipoUsuarioEnum> tipoUsuarioEnums = new ArrayList<TipoUsuarioEnum>();

        tipoUsuarioEnums.add(USUARIO);
        tipoUsuarioEnums.add(ADMIN_EMPRESA);
        tipoUsuarioEnums.add(ADMIN_GRUPO_EMPRERIAL);
        tipoUsuarioEnums.add(OSE_AVATAR);
         return  tipoUsuarioEnums;
    }

}