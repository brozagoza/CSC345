# Alejandro Zaragoza
# June 19, 2016
# CSC 345
# Program 2

CLASSES = $(wildcard *.java)

.PHONY: all classes clean
.SUFFIXES: .java .class
.java.class:
	javac $(JFLAGS) $*.java
all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
