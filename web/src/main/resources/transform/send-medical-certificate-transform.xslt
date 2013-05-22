<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:smc="urn:riv:insuranceprocess:healthreporting:SendMedicalCertificateResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <smc:SendMedicalCertificateResponse>
       <smc:result>
         <xsl:call-template name="result"/>
       </smc:result>
     </smc:SendMedicalCertificateResponse>
   </xsl:template>

</xsl:stylesheet>