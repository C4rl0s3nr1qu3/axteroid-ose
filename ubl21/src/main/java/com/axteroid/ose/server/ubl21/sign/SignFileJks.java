package com.axteroid.ose.server.ubl21.sign;

import com.axteroid.ose.server.jpa.model.Documento;

public interface SignFileJks{

    public void sign(Documento tbComprobante);
}

