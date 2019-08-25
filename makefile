SOURCES=src/*.java
OLD_JDK_BIN=/Users/owen/Downloads/jdk-9.0.4.jdk/Contents/Home/bin

library/sws.jar: $(SOURCES)
	$(OLD_JDK_BIN)/javac -Xlint:-options -source 1.6 -target 1.6 -classpath /Applications/Processing.app/Contents/Java/core/library/core.jar -d build $(SOURCES)
	$(OLD_JDK_BIN)/jar -cf sws_control.jar -C build .
	mv sws_control.jar library
