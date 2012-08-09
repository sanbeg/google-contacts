<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text"
            doctype-public="-//W3C//DTD HTML 4.0//EN"/>

<xsl:template match="contactlist">
  <xsl:apply-templates select="contacts" />
</xsl:template>

<xsl:template match="contacts">
  <xsl:text>fake	field	value
</xsl:text>
  <xsl:apply-templates select="contact"/>
</xsl:template>

<xsl:template match="contact">
  <xsl:text>0	g_id	</xsl:text><xsl:value-of select="@id"/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="fn">
  <xsl:text>	name	</xsl:text><xsl:value-of select="."/>
</xsl:template>

<xsl:template match="photo">
  <xsl:text>	pic_big	</xsl:text><xsl:value-of select="@href"/>
</xsl:template>

<xsl:template match="name"><xsl:apply-templates/></xsl:template>

<xsl:template match="given">
  <xsl:text>	first_name	</xsl:text><xsl:value-of select="."/>
</xsl:template>
<xsl:template match="family">
  <xsl:text>	last_name	</xsl:text><xsl:value-of select="."/>
</xsl:template>

<xsl:template match="*"></xsl:template>

</xsl:stylesheet>
		

