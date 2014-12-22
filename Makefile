SBT=sbt
SCALA_VERSION=2.11.4
PROJECT_NAME=graphique
PREFIX=/usr

package:
	$(SBT) clean assembly

install: package
	install -Dm 744 target/scala*/*-assembly*.jar $(destdir)$(PREFIX)/share/$(PROJECT_NAME)/$(PROJECT_NAME).jar
	install -Dm 755 scripts/$(PROJECT_NAME) $(destdir)$(PREFIX)/bin/$(PROJECT_NAME)
