<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"
            indent="yes"
            media-type="text/html"
            doctype-public="-//W3C//DTD HTML 4.0//EN"/>


<xsl:template match="fn"><h2><xsl:value-of select="." /></h2></xsl:template>

<xsl:template match="email|phone">
<dl>
<dt><xsl:value-of select="local-name(.)"/></dt>
<dd><xsl:value-of select="."/></dd>
</dl>
</xsl:template>

<xsl:template match="photo">
<img><xsl:attribute name="src"><xsl:value-of select="@href"/></xsl:attribute></img>
</xsl:template>

</xsl:stylesheet>




