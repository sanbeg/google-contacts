
%.class: %.java
	javac -cp '.:../gdata/java/lib/*' $^

%.html: %.xml
	xsltproc -o $@ html.xsl $^
