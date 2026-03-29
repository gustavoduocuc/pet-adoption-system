#!/usr/bin/env sh
set -e
cd "$(dirname "$0")"
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
	-keyout server.key \
	-out server.crt \
	-subj "/CN=localhost"
echo "Created server.crt and server.key in $(pwd)"
