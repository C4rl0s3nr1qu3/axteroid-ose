<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:math="http://exslt.org/math"
                exclude-result-prefixes="math">

<xsl:template match="values">
   <result>
      <xsl:text>Highest: </xsl:text>
<!--       <xsl:call-template name="math:highest"> -->
<!--          <xsl:with-param name="nodes" select="value" /> -->
<!--       </xsl:call-template> -->
   </result>
</xsl:template>

</xsl:stylesheet>