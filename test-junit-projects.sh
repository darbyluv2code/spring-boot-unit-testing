#!/bin/bash
set -e

echo "=== Running Unit Tests on JUnit Standalone Projects ==="
echo ""

FAILED_PROJECTS=()
SUCCESSFUL_PROJECTS=()
TOTAL_PROJECTS=0
TOTAL_TESTS=0

# Function to test a project
test_project() {
    local project_dir="$1"
    local project_name=$(basename "$project_dir")

    if [ -f "$project_dir/pom.xml" ]; then
        TOTAL_PROJECTS=$((TOTAL_PROJECTS + 1))
        echo "[$TOTAL_PROJECTS] Testing: $project_name"
        echo "    Path: $project_dir"

        cd "$project_dir"
        if mvn clean test 2>&1 | tee test.log; then
            SUCCESSFUL_PROJECTS+=("$project_name")
            # Count tests from output (macOS compatible)
            TEST_COUNT=$(grep "Tests run:" test.log | tail -1 | sed -n 's/.*Tests run: \([0-9]*\).*/\1/p' || echo "0")
            if [ -n "$TEST_COUNT" ] && [ "$TEST_COUNT" != "0" ]; then
                TOTAL_TESTS=$((TOTAL_TESTS + TEST_COUNT))
                echo "    ✓ SUCCESS ($TEST_COUNT tests)"
            else
                echo "    ✓ SUCCESS"
            fi
        else
            FAILED_PROJECTS+=("$project_name")
            echo "    ✗ FAILED"
            # Try to extract failure info
            grep -A 5 "FAILURE" test.log || true
        fi
        cd - > /dev/null
        echo ""
    fi
}

# Save current directory
START_DIR=$(pwd)

# Section 1.00 - Starting project (no tests yet)
if [ -d "1.00-starting-project" ]; then
    test_project "1.00-starting-project"
fi

# Section 1.01 - JUnit Review solutions
if [ -d "1.01-starting-project-solutions/section-02-junit-review" ]; then
    for dir in 1.01-starting-project-solutions/section-02-junit-review/*/; do
        test_project "$dir"
    done
fi

# Section 1.12 - FizzBuzz TDD solutions
if [ -d "1.12-fizzbuzz-project-solutions/section-03-test-driven-development-tdd" ]; then
    for dir in 1.12-fizzbuzz-project-solutions/section-03-test-driven-development-tdd/*/; do
        test_project "$dir"
    done
fi

# Return to starting directory
cd "$START_DIR"

# Summary
echo "========================================"
echo "TEST SUMMARY"
echo "========================================"
echo "Total JUnit projects tested: $TOTAL_PROJECTS"
echo "Successful: ${#SUCCESSFUL_PROJECTS[@]}"
echo "Failed: ${#FAILED_PROJECTS[@]}"
echo "Total tests executed: $TOTAL_TESTS"
echo ""

if [ ${#FAILED_PROJECTS[@]} -gt 0 ]; then
    echo "FAILED PROJECTS:"
    for project in "${FAILED_PROJECTS[@]}"; do
        echo "  - $project"
    done
    echo ""
    exit 1
else
    echo "✓ ALL JUNIT PROJECT TESTS PASSED!"
    echo "✓ $TOTAL_TESTS tests executed successfully across all JUnit projects"
fi
