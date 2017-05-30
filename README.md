aws-interface
=========

## Overview ##
This project is a solution to a puzzle for interfacing AWS from justdigitalpeople.

## Prerequisites (Linux) ##
On Ubuntu 16.04 LTS or equivalent and have the following in your PATH:
- GNU Make 4.1+
- JDK 1.8+
- Gradle 3.x
- npm 3.0+

## How to run locally ##
The free and easist way to run aws-interface without configuration is to run it with aws-mock, which is our opensource mock of Amazon Web Services for testing purposes.
- First let's start aws-mock in a console:
```
git clone https://github.com/treelogic-swe/aws-mock.git
cd aws-mock
gradle jettyRun
```
- Then open another console and run aws-interface:
```
git clone https://github.com/maxiaohao/aws-interface.git
cd aws-interface
make
make run
```
- Now you can open your browser at http://localhost:8080/aws-interface and verify the solution to the puzzle.

- If you'd like to try with genuine AWS rather than aws-mock, just configure the AWS endpoint and region in `src/main/resources/aws-conf.properties` and put your AWS credentials into `src/main/resources/aws-conf.properties` and run `make && make run` again.

## To run the test ##
- Run `make test`, though there is only a little code covered for now.


