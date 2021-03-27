<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <Echo>
            <value><xsl:value-of select="value"/></value>
        </Echo>
    </xsl:template>
</xsl:stylesheet>