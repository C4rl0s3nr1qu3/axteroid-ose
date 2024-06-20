<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Template Rechazo por Error -->
	<xsl:template name="rejectCall">
		<xsl:param name="errorCode" />
		<xsl:param name="errorMessage" />
		<xsl:message terminate="yes">
			<xsl:value-of
				select="error(QName('http://www.example.com/HR', 'myerr:toohighsal'),concat($errorCode,' - ',$errorMessage))" />
		</xsl:message>
	</xsl:template>

	<!-- Template Rechazo por Warning -->
	<xsl:template name="addWarning">
		<xsl:param name="warningCode" />
		<xsl:param name="warningMessage" />

		<xsl:message terminate="no">
			<xsl:value-of
				select="concat('Warning Valida Factura: ',$warningCode,' - ',$warningMessage)" />
		</xsl:message>
	</xsl:template>
</xsl:stylesheet>
