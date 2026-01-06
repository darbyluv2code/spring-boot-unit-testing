#!/bin/bash

echo "Updating JUnit versions in standalone projects..."
echo ""

UPDATED_COUNT=0

# Update JUnit version in section 1.01 projects
find ./1.01-starting-project-solutions/section-02-junit-review -name "pom.xml" -type f | while IFS= read -r file; do
  if grep -q "<version>5.12.2</version>" "$file"; then
    sed -i.bak 's/<version>5\.12\.2<\/version>/<version>6.0.1<\/version>/g' "$file"
    rm "${file}.bak"
    echo "Updated: $file"
    UPDATED_COUNT=$((UPDATED_COUNT + 1))
  fi
done

# Update JUnit version in section 1.12 projects
find ./1.12-fizzbuzz-project-solutions/section-03-test-driven-development-tdd -name "pom.xml" -type f | while IFS= read -r file; do
  if grep -q "<version>5.12.2</version>" "$file"; then
    sed -i.bak 's/<version>5\.12\.2<\/version>/<version>6.0.1<\/version>/g' "$file"
    rm "${file}.bak"
    echo "Updated: $file"
    UPDATED_COUNT=$((UPDATED_COUNT + 1))
  fi
done

echo ""
echo "Completed updating JUnit versions to 6.0.1"
