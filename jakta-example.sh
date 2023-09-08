#!/usr/bin/env sh
DESTINATION=~"/Downloads/jakta-examples-$(date --utc "+%F-%H.%M.%S")"
git clone https://github.com/jakta-bdi/jakta-examples.git "$DESTINATION"
cd "$DESTINATION"
./gradlew jaktaPingPong
