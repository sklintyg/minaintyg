<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gcr="urn:riv:insuranceprocess:healthreporting:GetCertificateResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <gcr:GetCertificateResponse>
       <gcr:result>
         <xsl:call-template name="result"/>
       </gcr:result>
     </gcr:GetCertificateResponse>
   </xsl:template>

</xsl:stylesheet>