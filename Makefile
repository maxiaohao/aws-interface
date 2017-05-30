SHELL = /bin/sh

init:
	mkdir -p src/main/webapp/third_party
	npm install bower
	node_modules/.bin/bower install

init-eclipse:
	gradle cleanEclipse eclipse

test:
	gradle test

run:
	gradle jettyRun

war:
	gradle war

clean:
	-rm -rf build
	-rm -rf node_modules
	-rm -rf src/main/webapp/third_party
	-rm -rf log

