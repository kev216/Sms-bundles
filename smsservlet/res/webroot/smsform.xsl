<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml"
>
 
  <xsl:template match="/">
    <html>
  
  
<head>
<style type="text/css">
<![CDATA[
	.title {
		background-color: #cc99ff;
		border-color: black;
		border-style: solid;
		border-width: medium;
		font-size: 1.5em;
		margin-left: 20%;
		margin-right: 20%;
		padding: .5%;
		text-align: center
	}
]]>
</style>
</head>

<script language="javaScript" src="script.js">

</script>

<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="smsform" method="GET" action="send">
<table align="center" width="284" height="554" border="0" cellspacing="0" cellpadding="0" background="skin.jpg">

<tr> 
    <td colspan="3" height="77">
    </td>
</tr>

<tr> 
    <td colspan="3" height="100" valign="botton">
    	<h1>
	<center><font face="Verdana,Helvetica,Arial" color="#ffffff">SMS<br/>Sender</font></center>
	</h1>
    </td>
</tr>


<tr>
    	<td height="210" width="53" align="center"></td>
	<td height="210" width="181" align="center">
		<font face="Verdana,Helvetica,Arial" size="1" color="#ffffff">
      <xsl:apply-templates select="*"/>
		<center><input type="text" name="charsleft" value="" readonly="1" size="3" maxlength="3"/></center>
		<input type="submit" width="105" height="43" name="send" value="Send"/>
		</font>
	</td>
    	<td height="210" align="center"></td>
</tr>

<tr> 
        <td colspan="3" align="right" valign="top">
		<font face="Verdana,Helvetica,Arial" size="2">
		</font>
	</td>
</tr>
    
     <!-- <div style="margin-left:73px;" align="left"><img src=".jpg"></div> -->  
</table>
</form>
</body>
</html>
</xsl:template>

			
<xsl:template match="sms">
Sender:<input type="text" name="sender" value="{sender}" size="11" maxlength="20"/>
Receiver:<input type="text" name="receiver" value="{receiver}" size="11" maxlength="20"/>
<textarea cols="16" value="" rows="5" name="message" onKeyUp="ShowCharsLeft(this.form);" wrap="physical">
<xsl:value-of select="message"/>
</textarea>
<!--
		<script language="javaScript">
			ShowCharsLeft(document.smsform);
		</script>
-->
</xsl:template>

  <xsl:template match="*">
  
  </xsl:template>


</xsl:stylesheet>