#!/bin/sh
mkdir ../gen-java -p
protoc *.proto --java_out=../gen-java -I=. 
