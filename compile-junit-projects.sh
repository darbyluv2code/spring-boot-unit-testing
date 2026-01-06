#!/bin/bash
set -e

echo "=== Compiling JUnit Standalone Projects ==="
echo ""

FAILED_PROJECTS=()
SUCCESSFUL_PROJECTS=()
TOTAL_PROJECTS=0

# Function to compile a project
compile_project() {
    local project_dir="$1"
    local project_name=$(basename "$project_dir")

    if [ -f "$project_dir/pom.xml" ]; then
        TOTAL_PROJECTS=$((TOTAL_PROJECTS + 1))
        echo "[$TOTAL_PROJECTS] Compiling: $project_name"
        echo "    Path: $project_dir"

        cd "$project_dir"
        if mvn clean compile -Dmaven.compiler.showDeprecation=true -Dmaven.compiler.showWarnings=true 2>&1 | tee compile.log; then
            SUCCESSFUL_PROJECTS+=("$project_name")
            echo "    ✓ SUCCESS"
        else
            FAILED_PROJECTS+=("$project_name")
            echo "    ✗ FAILED"
        fi
        cd - > /dev/null
        echo ""
    fi
}

# Save current directory
START_DIR=$(pwd)

# Section 1.00 - Starting project (no dependencies yet)
if [ -d "1.00-starting-project" ]; then
    compile_project "1.00-starting-project"
fi

# Section 1.01 - JUnit Review solutions
if [ -d "1.01-starting-project-solutions/section-02-junit-review" ]; then
    for dir in 1.01-starting-project-solutions/section-02-junit-review/*/; do
        compile_project "$dir"
    done
fi

# Section 1.12 - FizzBuzz TDD solutions
if [ -d "1.12-fizzbuzz-project-solutions/section-03-test-driven-development-tdd" ]; then
    for dir in 1.12-fizzbuzz-project-solutions/section-03-test-driven-development-tdd/*/; do
        compile_project "$dir"
    done
fi

# Return to starting directory
cd "$START_DIR"

# Summary
echo "========================================"
echo "COMPILATION SUMMARY"
echo "========================================"
echo "Total JUnit projects compiled: $TOTAL_PROJECTS"
echo "Successful: ${#SUCCESSFUL_PROJECTS[@]}"
echo "Failed: ${#FAILED_PROJECTS[@]}"
echo ""

if [ ${#FAILED_PROJECTS[@]} -gt 0 ]; then
    echo "FAILED PROJECTS:"
    for project in "${FAILED_PROJECTS[@]}"; do
        echo "  - $project"
    done
    echo ""
    exit 1
else
    echo "✓ ALL JUNIT PROJECTS COMPILED SUCCESSFULLY!"
fi
