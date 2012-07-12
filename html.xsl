<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"
            indent="yes"
            media-type="text/html"
            doctype-public="-//W3C//DTD HTML 4.0//EN"/>

<xsl:template match="contacts">
  <html>
    <head><title><xsl:value-of select="@title"/></title></head>
    <body>
      <h1><xsl:value-of select="@id"/></h1>
      <div><xsl:value-of select="@updated"/></div>
      
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>

<xsl:template match="contact">
<xsl:apply-templates />
<a>
<xsl:attribute name="href">
<xsl:text>https://www.google.com/contacts/#contact/</xsl:text>
<xsl:value-of select="substring-after(@id,'/base/')" />
</xsl:attribute>
link
</a>
<hr />
</xsl:template>


<xsl:template match="fn"><h3><xsl:value-of select="." /></h3></xsl:template>
<xsl:template match="name"><h2><xsl:value-of select="." /></h2></xsl:template>

<xsl:template match="section">
  <table>
    <caption><xsl:value-of select="@tag"/></caption>
    <xsl:apply-templates/>
  </table>
</xsl:template>

<xsl:template match="email|phone">
  <tr>
    <td><xsl:value-of select="substring-after(@rel,'#')"/></td>
    <td><xsl:value-of select="."/></td>
  </tr>
</xsl:template>

<xsl:template match="photo">
<img><xsl:attribute name="src"><xsl:value-of select="@href"/></xsl:attribute></img>
</xsl:template>

</xsl:stylesheet>




