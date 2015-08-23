#!/bin/sh

cd assets
find . -type f -path '*_10.png' | while read l; do F=`echo $l | sed -e "s/_10//"`; cp $l ../../app/src/main/res/drawable-mdpi/$F; done
find . -type f -path '*_15.png' | while read l; do F=`echo $l | sed -e "s/_15//"`; cp $l ../../app/src/main/res/drawable-hdpi/$F; done
find . -type f -path '*_20.png' | while read l; do F=`echo $l | sed -e "s/_20//"`; cp $l ../../app/src/main/res/drawable-xhdpi/$F; done
cd -
