## Compiler
JC=javac

## Flags to compilation
#	-g:none
#		Doesn't show any debug messages information
# 	-deprecation
#		Shows a description of each use or override of a deprecated member or class.
JFLAGS=-g:none -deprecation

# Tool to create the executable file
JAR=jar

## Path to binary and source code
PATH_BIN=./bin
PATH_SRC=./src

# Another infos to compilation
PKG=cc.sce
PACKAGE=cc/sce
DIR_SRC=$(PATH_SRC)/$(PACKAGE)
CLASSES=$(DIR_SRC)/*.java
ENTRYPOINT_CLASS=Main

all:
	$(JC) $(JFLAGS) -classpath $(PATH_SRC) -d $(PATH_BIN) -s $(PATH_SRC) $(CLASSES)
	$(JAR) cfe $(PATH_BIN)/SCE.jar $(PKG).$(ENTRYPOINT_CLASS) -C $(PATH_BIN) $(PACKAGE)

clean:
	@rm -rf $(PATH_BIN)
