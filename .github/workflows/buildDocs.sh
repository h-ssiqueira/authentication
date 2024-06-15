#!/bin/bash

echo "Building redocCLI"

npx @redocly/cli build-docs backend/src/main/resources/swagger.yaml -o docs/index.html

# Insert favicon at 7th line
sed -i '7 i \ \ <link rel="icon" type="image/x-icon" href="imgs/favicon.ico"/>' docs/index.html
