<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" version="5.0"/>
	<xsl:template match="/">
		<html>
			<body>
				<h1 style="background:green; color:white; text-align:center; text-shadow:2px 2px grey; font-size:28px; border:5px ridge red;">Diet Survey</h1>
				<h2 style="color:green; text-align:center; text-shadow:1px 1px grey; font-size:24px; border:2px ridge red;">Assesment of dietary nutrients in last 24 hours</h2>
				<table style="border-collapse:collapse; border:2px ridge yellow; width:600px; margin-left:auto; margin-right:auto" align="center">
					<caption style="font-size:20px; color:blue; background:yellow; border:2px ridge yellow;">Summary</caption>
					<xsl:if test="/assessment/nutritionalStatus/energy/status='excess'">
						<tr><td style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Energy consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/energy/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/energy/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Energy consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/energy/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/protein/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Protein consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/protein/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/protein/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Protein consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/protein/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/iron/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Iron consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/iron/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/iron/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Iron consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/iron/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/vitA/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Vitamin A consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/vitA/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/vitA/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Viamin A consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/vitA/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/thiamine/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Thiamine consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/thiamine/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/thiamine/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Thiamine consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/thiamine/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/riboflavin/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Riboflavin consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/riboflavin/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/riboflavin/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Riboflavin consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/riboflavin/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/vitC/status='excess'">
						<tr><td  style="background:Green; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Vitamin C consumption is excess by <xsl:value-of select="format-number(/assessment/nutritionalStatus/vitC/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/assessment/nutritionalStatus/vitC/status='deficient'">
						<tr><td style="background:red; height: 40px; padding-left:20px; border:none; text-align:left; color:white; font-size:16px;">Viamin C consumption is deficient by <xsl:value-of select="format-number(/assessment/nutritionalStatus/vitC/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>