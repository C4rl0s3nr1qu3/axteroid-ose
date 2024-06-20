<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:regexp="http://exslt.org/regular-expressions"
    xmlns:dyn="http://exslt.org/dynamic"
    xmlns:gemfunc="http://www.sunat.gob.pe/gem/functions"
    xmlns:date="http://exslt.org/dates-and-times"
    xmlns:func="http://exslt.org/functions"	
	xmlns:dp="http://www.datapower.com/extensions" 
	extension-element-prefixes="dp"
	exclude-result-prefixes="dp" version="2.0">

	<xsl:include href="error_utils.xsl" dp:ignore-multiple="yes" />

	<!-- Template que sirve para validar si un nodo existe y si existe valida 
		que se cumpla la expresion regular -->
	<xsl:template name="existAndRegexpValidateElement">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="errorCodeValidate" />
		<xsl:param name="node" />
		<xsl:param name="regexp" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:choose>
			<xsl:when test="not(string($node))">
				<xsl:choose>
					<xsl:when test="$isError">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
							<xsl:with-param name="errorMessage"
								select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
							<xsl:with-param name="warningMessage"
								select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test='not(fn:matches(string($node),$regexp, ";j"))'>
					<xsl:choose>
						<xsl:when test="$isError">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="$errorCodeValidate" />
								<xsl:with-param name="errorMessage"
									select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="$errorCodeValidate" />
								<xsl:with-param name="warningMessage"
									select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="existAndRegexpValidateElementCount">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="errorCodeValidate" />
		<xsl:param name="node" />
		<xsl:param name="regexp" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:choose>
			<xsl:when test="not(string($node)) and (count($node)=0)">
				<xsl:choose>
					<xsl:when test="$isError">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
							<xsl:with-param name="errorMessage"
								select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
							<xsl:with-param name="warningMessage"
								select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test='not(fn:matches(string($node),$regexp, ";j"))'>
					<xsl:choose>
						<xsl:when test="$isError">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="$errorCodeValidate" />
								<xsl:with-param name="errorMessage"
									select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="$errorCodeValidate" />
								<xsl:with-param name="warningMessage"
									select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template que sirve para validar un nodo y si el nodo existe, valida 
		que se cumpla la expresion regular, si el nodo no existe no hace nada -->
	<!-- Se debe de usar para elementos opcionales -->
	
	<xsl:template name="regexpValidateElementIfExist">
		<xsl:param name="errorCodeValidate" />
		<xsl:param name="node" />
		<xsl:param name="regexp" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(count($node) &gt;= 1) and not(fn:matches($node,$regexp, ';j'))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeValidate" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeValidate" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="regexpValidateElementIfExistTrue">
		<xsl:param name="errorCodeValidate" />
		<xsl:param name="node" />
		<xsl:param name="regexp" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(count($node) &gt;= 1) and (fn:matches($node,$regexp, ';j'))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeValidate" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeValidate" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="existValidateElementExist">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(string($node)) or ($node=null) or ($node='') or (count($node) &gt; 0)">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="existValidateElementIfExist">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />
		
		<xsl:if test="not(string($node))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="existValidateElementNotExist">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(not(string($node))) or (not(string($node)) and (count($node)=0)) 
			or (($node) and (($node=null) or ($node=''))) or (not(string($node)) and (($node=null) or ($node='')))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="existValidateElementNotExistNada">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(not(string($node))) and ($node=null)">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="existValidateElementIfNoExistCount">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="not(string($node)) and (count($node)=0)">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="existValidateElementIfExistVacio">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="(string($node)) and (($node=null) or ($node=''))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>	
	
	<xsl:template name="existValidateElementIfExistNULL">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="($node) and (($node=null) or ($node=''))">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="existValidateElementIfNotExist">
		<xsl:param name="errorCodeNotExist" />
		<xsl:param name="node" />
		<xsl:param name="isError" select="true()" />
		<xsl:param name="descripcion" select="'Error Expr Regular'" />

		<xsl:if test="not(string($node)) or ($node=null) or ($node='')">
			<xsl:choose>
				<xsl:when test="$isError">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="$errorCodeNotExist" />
						<xsl:with-param name="errorMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="$errorCodeNotExist" />
						<xsl:with-param name="warningMessage"
							select="concat($descripcion,': ', $errorCodeNotExist,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
    <xsl:template name="isTrueExpresion">
        <xsl:param name="errorCodeValidate" />
        <xsl:param name="node" />
        <xsl:param name="expresion" />
        <xsl:param name="isError" select="true()"/>
        <xsl:param name="descripcion" select="'INFO '"/>        
        <xsl:if test="$expresion = true()">        
            <xsl:choose>
                <xsl:when test="$isError">
                    <xsl:call-template name="rejectCall">
                        <xsl:with-param name="errorCode" select="$errorCodeValidate" />
                        <xsl:with-param name="errorMessage" select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>                   
                    <xsl:call-template name="addWarning">
                        <xsl:with-param name="warningCode" select="$errorCodeValidate" />
                        <xsl:with-param name="warningMessage" select="concat($descripcion,': ', $errorCodeValidate,' (nodo: &quot;',name($node/parent::*),'/', name($node), '&quot; valor: &quot;', $node, '&quot;)')" />
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>       
        </xsl:if>       
    </xsl:template>	

    <xsl:template name="findElementInCatalogProperty">
        <xsl:param name="errorCodeValidate" />
        <xsl:param name="idCatalogo" />
        <xsl:param name="catalogo" />
        <xsl:param name="propiedad" />
        <xsl:param name="valorPropiedad" />
        <xsl:param name="descripcion" select="''"/>       
        <xsl:variable name="url_catalogo" select="concat('../../../commons/cpe/catalogo/cat_',$catalogo,'.xml')"/>       
        <xsl:variable name="apos">'</xsl:variable>
        <xsl:variable name="vCondition" select="concat('@id=', $apos,$idCatalogo, $apos,' and @', $propiedad, '=', $apos, $valorPropiedad, $apos)" />
        <xsl:variable name="dynEval" select="concat('document(',$apos,$url_catalogo,$apos,')/l/c[', $vCondition, ']')" />
        
        <xsl:if test="count($dynEval) &lt; 1">
            <xsl:call-template name="rejectCall">
                <xsl:with-param name="errorCode" select="$errorCodeValidate" />
                <xsl:with-param name="errorMessage" select="concat($descripcion,': condicion:',$dynEval,' Valor no se encuentra en el catalogo: ',$catalogo,', ID: ', $idCatalogo, '  (nodo: &quot;',name($idCatalogo/parent::*),'/', name($idCatalogo), '&quot; propiedad ',$propiedad,': &quot;', $valorPropiedad, '&quot;)')" />
            </xsl:call-template>
        </xsl:if>				
    </xsl:template>

  <xsl:template name="findElementInCatalog_2">
    <xsl:param name="errorCodeValidate"/>
    <xsl:param name="idCatalogo"/>
    <xsl:param name="catalogo"/>
    <xsl:param name="descripcion" select="''"/>
    <xsl:param name="isError" select="true()"/>
    <xsl:variable name="url_catalogo"
                  select="concat('../../../VALI/commons/cpe/catalogo/cat_',$catalogo,'.xml')"/>
    <xsl:if test="count($idCatalogo) &gt;= 1 and count(document($url_catalogo)/l/c[@id=$idCatalogo]) &lt; 1 ">
      <xsl:choose>
        <xsl:when test="$isError">
          <xsl:call-template name="rejectCall">
            <xsl:with-param name="errorCode"
                            select="$errorCodeValidate"/>
            <xsl:with-param name="errorMessage"
                            select="concat($descripcion,': errorCode ', $errorCodeValidate,': Valor no se encuentra en el catalogo: ',$catalogo,' (nodo: &quot;',name($idCatalogo/parent::*),'/', name($idCatalogo), '&quot; valor: &quot;', $idCatalogo, '&quot;)')"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="addWarning">
            <xsl:with-param name="warningCode"
                            select="$errorCodeValidate"/>
            <xsl:with-param name="warningMessage"
                            select="concat($descripcion,': errorCode ', $errorCodeValidate,': ','Valor no se encuentra en el catalogo: ',$catalogo,' (nodo: &quot;',name($idCatalogo/parent::*),'/', name($idCatalogo), '&quot; valor: &quot;', $idCatalogo, '&quot;)')"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
  
	<!-- Template que sirve para validar la existencia del valor de un tag dentro de un catalogo -->
    <xsl:template name="findElementInCatalog">
	    <xsl:param name="errorCodeValidate"/>
	    <xsl:param name="idCatalogo"/>
	    <xsl:param name="catalogo"/>
	    <xsl:param name="descripcion" select="''"/>
	    <xsl:param name="isError" select="true()"/>
	    <!-- <xsl:variable name="url_catalogo" select="concat('../../../VALI/commons/cpe/catalogo/cat_',$catalogo,'.xml')"/> -->
	    <xsl:variable name="count_catalogo" select="count($idCatalogo)"/>
	    
	    <xsl:variable name="url_catalogo" select="concat('catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document" select="count(document($url_catalogo)/l/c[@id=$idCatalogo])"/>
	    
	    <xsl:variable name="url_catalogo_1" select="concat('/catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_1" select="count(document($url_catalogo_1)/l/c[@id=$idCatalogo])"/>

	    <xsl:variable name="url_catalogo_2" select="concat('../catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_2" select="count(document($url_catalogo_2)/l/c[@id=$idCatalogo])"/>
	    
	   	<xsl:variable name="url_catalogo_3" select="concat('../../catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_3" select="count(document($url_catalogo_3)/l/c[@id=$idCatalogo])"/>
	    
	    <xsl:variable name="url_catalogo_4" select="concat('../../../catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_4" select="count(document($url_catalogo_4)/l/c[@id=$idCatalogo])"/>

	   	<xsl:variable name="url_catalogo_5" select="concat('../../../../catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_5" select="count(document($url_catalogo_5)/l/c[@id=$idCatalogo])"/>
	    
	    <xsl:variable name="url_catalogo_6" select="concat('../../../../../catalogo/cat_',$catalogo,'.xml')"/>	    
	    <xsl:variable name="count_document_6" select="count(document($url_catalogo_6)/l/c[@id=$idCatalogo])"/>
	    	    	
	    <xsl:variable name="apos">'</xsl:variable>
<!--     	<xsl:variable name="vCondition" -->
<!--                   select="concat('@id=', $apos,$idCatalogo, $apos,' and @', $propiedad, '=', $apos, $valorPropiedad, $apos)"/> -->
<!--     	<xsl:variable name="dynEval" -->
<!--                   select="concat('document(',$apos,$url_catalogo,$apos,')/l/c[', $vCondition, ']')"/>	    	 -->
	    	    	
	    <xsl:variable name="documento" select="concat('document(',$apos,$url_catalogo,$apos,')')"/>	
	    <xsl:variable name="documento_1" select="document($documento)"/>	
	    <xsl:variable name="documento_2" select="document($url_catalogo_2)"/>	
	    <xsl:variable name="documento_3" select="document($url_catalogo_3)"/>	
	    <xsl:variable name="documento_4" select="document($url_catalogo_4)"/>
	   	<xsl:variable name="documento_5" select="document($url_catalogo_5)"/>	
	    <xsl:variable name="documento_6" select="document($url_catalogo_6)"/>
	    		    	    
	    <xsl:if test="count($idCatalogo) &gt;= 1 and count(document($url_catalogo)/l/c[@id=$idCatalogo]) &lt; 1 ">
	      <xsl:choose>
	        <xsl:when test="$isError">
	          <xsl:call-template name="rejectCall">
	            <xsl:with-param name="errorCode"
	                            select="$errorCodeValidate"/>
	            <xsl:with-param name="errorMessage"
	                            select="concat($descripcion, ' @ ', $documento,' - ', $documento_1 ,' - ', $documento_2,' - ', $documento_3,' - ', $documento_4,' - ', $documento_5,' - ', $documento_6, ' @ ', $count_document,' - ', $count_document_1,' - ', $count_document_2,' - ', $count_document_3,' - ', $count_document_4,' - ', $count_document_5,' - ', $count_document_6,' @ errorCode ', $errorCodeValidate,': Valor no se encuentra en el catalogo: ',$catalogo,' (nodo: &quot;',name($idCatalogo/parent::*),'/', name($idCatalogo), '&quot; valor: &quot;', $idCatalogo, '&quot;)')"/>
	          </xsl:call-template>
	        </xsl:when>
	        <xsl:otherwise>
	          <xsl:call-template name="addWarning">
	            <xsl:with-param name="warningCode"
	                            select="$errorCodeValidate"/>
	            <xsl:with-param name="warningMessage"
	                            select="concat($descripcion,': errorCode ', $errorCodeValidate,': ','Valor no se encuentra en el catalogo: ',$catalogo,' (nodo: &quot;',name($idCatalogo/parent::*),'/', name($idCatalogo), '&quot; valor: &quot;', $idCatalogo, '&quot;)')"/>
	          </xsl:call-template>
	        </xsl:otherwise>
	      </xsl:choose>
	    </xsl:if>
	    
<!-- 	    				<xsl:call-template name="addWarning"> -->
<!-- 							<xsl:with-param name="warningCode" select="'0001'" /> -->
<!-- 							<xsl:with-param name="warningMessage" -->
<!-- 								select="concat('Error en la linea: ', $url_catalogo,' - ', $count_catalogo,' - ', $count_document)"/> -->
<!-- 						</xsl:call-template> -->
	    
    </xsl:template>  
</xsl:stylesheet>
