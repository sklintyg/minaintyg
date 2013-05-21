<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gcr="urn:riv:insuranceprocess:healthreporting:GetConsentResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <gcr:GetConsentResponse>
       <gcr:result>
         <xsl:call-template name="result"/>
       </gcr:result>
     </gcr:GetConsentResponse>
   </xsl:template>

</xsl:stylesheet>