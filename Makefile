# To used build and run below, the following must be installed to the local machine: 
# - FFTW version 3.3.9
# - java (only tested with version 8)
# - mvn version 3.6
.PHONY: build unit lint fix test start clean help

help:   # prints all make targets
	@cat Makefile | grep '^[^ ]*:' | grep -v '.PHONY' | grep -v help | sed 's/:.*#/#/' | column -s "#" -t

setup:

run: # runs the program
	@java -jar ./target/beta-distribution-0.0.1-SNAPSHOT-jar-with-dependencies.jar

build: # build the package
	@mvn package

unit: # run unit tests
	@mvn test

lint: # lints all files
	@mvn com.coveo:fmt-maven-plugin:check

fix: # fix code formatting
	@mvn com.coveo:fmt-maven-plugin:format

lint-checkstyle: # lint using checkstyle (https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html)
	@mvn checkstyle::check

test: clean build lint unit    # runs all tests

build-docker: # build docker
	@docker build -t beta-distribution-base:latest -f base.Dockerfile .
	@docker build -t beta-distribution:latest -f Dockerfile .

run-docker: # run docker
	@docker run -t beta-distribution:latest

clean: # clean up java files
	@mvn clean
