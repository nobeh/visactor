#!/bin/bash

echo "Compiling visactor ..."
mvn -q clean compile
echo "Starting visactor in demo mode ..."
mvn -q exec:java -Dexec.mainClass="visactor.Visactor" -Dvisactor.demo=true

