<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rnc="urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3"
                xmlns:hr="urn:riv:insuranceprocess:healthreporting:2">

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="soap:Fault">

    <rnc:RegisterMedicalCertificateResponse>
      <rnc:result>
        <hr:resultCode>ERROR</hr:resultCode>
        <xsl:choose>
          <xsl:when test="contains(faultcode/text(), 'soap:Client')">
            <hr:errorId>VALIDATION_ERROR</hr:errorId>

          </xsl:when>
          <xsl:otherwise>
            <hr:errorId>APPLICATION_ERROR</hr:errorId>
          </xsl:otherwise>
        </xsl:choose>

        <hr:errorText>
          <xsl:value-of select="faultstring/text()"/>
        </hr:errorText>
      </rnc:result>
    </rnc:RegisterMedicalCertificateResponse>
  </xsl:template>


</xsl:stylesheet>