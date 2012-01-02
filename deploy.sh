#!/bin/bash

rm -rf lib-signed
cp -rf lib lib-signed
cp build/libs/*.jar lib-signed

read -p "Storepass: " storepass

find lib-signed -name "*.jar" -exec jarsigner -storepass $storepass {} copad \; -print

s3cmd sync lib-signed/* s3://s3.copad.cc/lib/

rm -rf lib-signed
