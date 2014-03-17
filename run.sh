#!/bin/bash

package=io.github.sanbeg.google_contacts
class=$1
shift

exec mvn -q exec:java -Dexec.mainClass="$package.$class" -Dexec.args="$*"

