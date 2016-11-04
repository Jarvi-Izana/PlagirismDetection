JAVAC = javac
JFLAGS = -g
SRC = ./

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $*.java

JAVA_FILES = $(wildcard $(SRC)*.java)
CLASS = $(JAVA_FILES:.java=.class)

default: $(CLASS)

run:
	java Client synonyms.txt source.txt target.txt 3

test:
	java Test

clean:
	$(RM) $(CLASS)