#!/usr/bin/env bash
set -xeuo pipefail
IFS=$'\n\t'

./down.sh
./up.sh
