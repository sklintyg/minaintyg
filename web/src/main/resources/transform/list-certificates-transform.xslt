<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:lcr="urn:riv:insuranceprocess:healthreporting:ListCertificatesResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <lcr:ListCertificatesResponse>
       <lcr:result>
         <xsl:call-template name="result"/>
       </lcr:result>
     </lcr:ListCertificatesResponse>
   </xsl:template>

</xsl:stylesheet>