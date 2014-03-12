<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>Diet Survey</title>
				<link rel="stylesheet" type="text/css" href="style.css"/>
			</head>
			<body>
				<h1>Diet Survey</h1>
				<h2>Diet survey based on 24 hours recall</h2>
				<h3>Family Information</h3>
				<table class="paired">
					<tr><td class="key">Head of the Family: </td><td class="value"><xsl:value-of select="/ds/record/id/hof"/></td></tr>
					<tr><td class="key">Date of Survey: </td><td class="value"><xsl:value-of select="/ds/record/id/date"/></td></tr>
				</table>
				<table>
					<caption>Family Members</caption>
					<tr><th>Name</th><th>Age</th><th>Sex</th></tr>
					<xsl:for-each select="/ds/record/id/member">
						<tr>
							<td><xsl:value-of select="name"/></td>
							<td>
							<xsl:choose>
								<xsl:when test="boolean(infant)">
									<xsl:value-of select="infant"/> months
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="age"/> years
								</xsl:otherwise>
							</xsl:choose>
							</td>
							<td><xsl:value-of select="sex"/></td>
						</tr>
					</xsl:for-each>
				</table>
				<ul>
					<xsl:for-each select="/ds/record/id/member">
						<xsl:if test="age &gt; 18">
							<li><xsl:value-of select="name"/> is a <xsl:value-of select="occupation"/> worker.</li>
							<xsl:if test="sex='female'">
								<xsl:choose>
									<xsl:when test="pregnant='true'">
										<li><xsl:value-of select="name"/> is pregnant.</li>
									</xsl:when>
									<xsl:when test="lactating='true'">
										<xsl:choose>
											<xsl:when test="beyond6m='true'">
												<li><xsl:value-of select="name"/> is lactating over 6 months.</li>
											</xsl:when>
											<xsl:otherwise>
												<li><xsl:value-of select="name"/> is lactating below 6 months.</li>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<li><xsl:value-of select="name"/> is non-pregnant and non-lactating.</li>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</ul>
				<h3>Foods consumed in last 24 hours</h3>
				<h5>(Amount is in mg or ml)</h5>
				<ol>
					<xsl:for-each select="/ds/record/intake/food">
						<li>
							<b><xsl:value-of select="name"/>: </b>
							<xsl:value-of select="amount"/>
						</li>
					</xsl:for-each>
				</ol>
				<h3>Nutrient requirement<sup>1</sup></h3>
				<table>
					<tr>
						<th>Name of Member</th><th>Energy (kcal)</th><th>Protein (gm)</th><th>Iron (mg)</th><th>Vitamin A (&#956;g)</th><th>Thiamine (mg)</th><th>Riboflavin (mg)</th><th>Vitamin C (mg)</th>
					</tr>
					<xsl:for-each select="/ds/assessment/requirement/member">
						<tr>
							<td><xsl:value-of select="name"/></td>
							<td><xsl:value-of select="format-number(energy, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(protein, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(iron, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(vitA, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(thiamine, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(riboflavin, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(vitC, '##0.##')"/></td>
						</tr>
					</xsl:for-each>
					<tr>
						<td class="marked">Total</td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/energy, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/protein, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/iron, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/vitA, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/thiamine, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/riboflavin, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/requirement/total/vitC, '##0.##')"/></td>
					</tr>
				</table>
				<h3>Nutritional value of consumed foods<sup>2</sup></h3>
				<table>
					<tr>
						<th>Name of Food</th><th>Energy (kcal)</th><th>Protein (g)</th><th>Iron (mg)</th><th>Vitamin A (&#956;g)</th><th>Thiamine (mg)</th><th>Riboflavin (mg)</th><th>Vitamin C (mg)</th>
					</tr>
					<xsl:for-each select="/ds/assessment/consumption/food">
						<tr>
							<td><xsl:value-of select="name"/></td>
							<td><xsl:value-of select="format-number(energy, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(protein, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(iron, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(vitA, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(thiamine, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(riboflavin, '##0.##')"/></td>
							<td><xsl:value-of select="format-number(vitC, '##0.##')"/></td>
						</tr>
					</xsl:for-each>
					<tr>
						<td class="marked">Total</td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/energy, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/protein, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/iron, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/vitA, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/thiamine, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/riboflavin, '##0.##')"/></td>
						<td class="marked"><xsl:value-of select="format-number(/ds/assessment/consumption/total/vitC, '##0.##')"/></td>
					</tr>
				</table>
				<h3>Comparison of total nutrients intake</h3>
				<table>
					<tr>
						<th>Nutrient</th><th>Requirement</th><th>Intake</th><th>Deficiency/Excess</th>
					</tr>
					<tr>
						<td>Energy</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/energy, '##0.##')"/> kcal</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/energy, '##0.##')"/> kcal</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/energy/amount, '##0.##')"/> kcal 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/energy/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/energy/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Protein</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/protein, '##0.##')"/> g</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/protein, '##0.##')"/> g</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/protein/amount, '##0.##')"/> g 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/protein/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/protein/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Iron</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/iron, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/iron, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/iron/amount, '##0.##')"/> mg 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/iron/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/iron/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Vitamin A</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/vitA, '##0.##')"/> &#956;g</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/vitA, '##0.##')"/> &#956;g</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitA/amount, '##0.##')"/> &#956;g 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/vitA/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitA/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Thiamine</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/thiamine, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/thiamine, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/thiamine/amount, '##0.##')"/> mg 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/thiamine/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/thiamine/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Riboflavin</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/riboflavin, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/riboflavin, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/riboflavin/amount, '##0.##')"/> mg 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/riboflavin/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/riboflavin/percentage, '##0.##')"/>%)</td>
					</tr>
					<tr>
						<td>Vitamin C</td>
						<td><xsl:value-of select="format-number(/ds/assessment/requirement/total/vitC, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/consumption/total/vitC, '##0.##')"/> mg</td>
						<td><xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitC/amount, '##0.##')"/> mg 
							<xsl:value-of select="/ds/assessment/nutritionalStatus/vitC/status"/>
							(<xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitC/percentage, '##0.##')"/>%)</td>
					</tr>
				</table>
				<h3>Assesment of dietary nutrients</h3>
				<table class="sum">
					<caption class="sum">Summary</caption>
					<xsl:if test="/ds/assessment/nutritionalStatus/energy/status='excess'">
						<tr><td class="excess">Energy consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/energy/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/energy/status='deficient'">
						<tr><td class="deficient">Energy consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/energy/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/protein/status='excess'">
						<tr><td class="excess">Protein consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/protein/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/protein/status='deficient'">
						<tr><td class="deficient">Protein consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/protein/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/iron/status='excess'">
						<tr><td class="excess">Iron consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/iron/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/iron/status='deficient'">
						<tr><td class="deficient">Iron consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/iron/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/vitA/status='excess'">
						<tr><td class="excess">Vitamin A consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitA/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/vitA/status='deficient'">
						<tr><td class="deficient">Viamin A consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitA/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/thiamine/status='excess'">
						<tr><td class="excess">Thiamine consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/thiamine/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/thiamine/status='deficient'">
						<tr><td class="deficient">Thiamine consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/thiamine/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/riboflavin/status='excess'">
						<tr><td class="excess">Riboflavin consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/riboflavin/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/riboflavin/status='deficient'">
						<tr><td class="deficient">Riboflavin consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/riboflavin/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/vitC/status='excess'">
						<tr><td class="excess">Vitamin C consumption is excess by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitC/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
					<xsl:if test="/ds/assessment/nutritionalStatus/vitC/status='deficient'">
						<tr><td class="deficient">Viamin C consumption is deficient by <xsl:value-of select="format-number(/ds/assessment/nutritionalStatus/vitC/percentage, '##0.##')"/> %.</td></tr>
					</xsl:if>
				</table>
				<img src="bar_diagram.png"/>
				<h3>Reference</h3>
				<ol>
					<li>NUTRIENT REQUIREMENTS AND RECOMMENDED DIETARY ALLOWANCES FOR INDIANS- A Report of the Expert Group of the Indian Council of Medical Research 2009, NATIONAL INSTITUTE OF NUTRITION, Indian Council of Medical Research</li>
					<li>Nutritive Value of Indian Foods, ICMR, 1989</li>
				</ol>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>