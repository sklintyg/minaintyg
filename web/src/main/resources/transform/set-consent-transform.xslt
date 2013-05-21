<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:scr="urn:riv:insuranceprocess:healthreporting:SetConsentResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <scr:SetConsentResponse>
       <scr:result>
         <xsl:call-template name="result"/>
       </scr:result>
     </scr:SetConsentResponse>
   </xsl:template>

</xsl:stylesheet>