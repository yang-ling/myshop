#!/usr/bin/env bash
set -xeuo pipefail
IFS=$'\n\t'

./run.sh up -d --build "$@"
