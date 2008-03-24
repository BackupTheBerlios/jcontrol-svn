<!--
   * Copyright (C) 2008 The JControl Group and individual authors listed below
   *
   * This library is free software; you can redistribute it and/or modify it
   * under the terms of the GNU Lesser General Public License as published by the
   * Free Software Foundation; either version 2.1 of the License, or (at your
   * option) any later version.
   *
   * This library is distributed in the hope that it will be useful, but WITHOUT
   * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
   * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
   * for more details.
   *
   * You should have received a copy of the GNU Lesser General Public License
   * along with this library; if not, write to the Free Software Foundation,
   * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
   *
-->

<!--  $Id: $ -->

<xsl:stylesheet
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0">

   <xsl:output
      method="xml"
      indent="yes"
      encoding="UTF-8" />

   <xsl:tempalte math="JControl/Classpath">
      <xsl:apply-templates select="BuiltIn" />
      <xsl:apply-templates select="Standard" />
      <xsl:apply-templates select="Optional" />
   </xsl:tempalte>

   <xsl:tempalte math="BuiltIn|Standard|Optional">
      <fileset dir="../supportfiles/profiles/jar/">
         <xsl:element name="include">
            <xsl:attribute name="name">
            <xsl:value-of select="@name" />
            </xsl:attribute>
         </xsl:element>
      </fileset>
   </xsl:tempalte>

</xsl:stylesheet>

