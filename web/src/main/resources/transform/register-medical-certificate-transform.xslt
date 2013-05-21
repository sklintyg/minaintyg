<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rmc="urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <rmc:RegisterMedicalCertificateResponse>
       <rmc:result>
         <xsl:call-template name="result"/>
       </rmc:result>
     </rmc:RegisterMedicalCertificateResponse>
   </xsl:template>

</xsl:stylesheet>