1) Tomcat beenden (service hvtomcat stop bzw. Dienst HvTomcat beenden)  

2) Bei einem Update oder der Erstinstallation:
a) Das Verzeichnis "./lib" auf das Verzeichnis (beispielsweise)
   /apache-tomcat-7.0.42 inklusive der Struktur kopieren.
   (Dieses Verzeichnis ist ab Build 13085 leer)
b) Die Dateien (restapi-doc.war, ROOT.war) im Verzeichnis "./all" auf das Verzeichnis
   (beispielsweise) /apache-tomcat-7.0.42/webapps kopieren
c) Je nach Applikationsserver die Dateien aus dem Verzeichnis
   "./jboss" oder "./wildfly" auf das Verzeichnis
   (beispielsweise) /apache-tomcat-7.0.42/webapps kopieren
c) Die Verzeichnisse restapi, restapi-doc und ROOT löschen 
   (/apache-tomcat-7.0.42/, es reicht nicht, diese Verzeichnis nur zu leeren)

3) Bei der Erstinstallation zusaetzlich:
a) Das Verzeichnis "./conf" auf das Verzeichnis (beispielsweise)
   /apache-tomcat-7.0.42 inklusive der Struktur kopieren.
b) Die Datei /apache-tomcat-7.0.42/conf/Catalina/localhost/restapi.xml
   oeffnen und
b1) den Hostname des HELIUM V Servers in 
   "Environment name="java.naming.provider.url" eintragen
b2) den NamingProvider des HELIUM V Servers in
   "Environment name="java.naming.factory.initial" eintragen
b3) den Applikationsserver-Typ des HELIUM V Servs in
   "Environment name="heliumv.applicationserver" eintragen (jboss, wildfly)
c) Die Datei /apache-tomcat-7.0.42/conf/server.xml oeffnen und
   den Port "8080" durch "8280" ersetzen. Das kommt insgesamt 2 mal vor.
   