#!/bin/bash

# Review the last commit using OpenAI Codex CLI
# Codex automatically reads AGENTS.md for project context
#
# Usage: ./review_last_commit.sh [commit_sha]
#        ./review_last_commit.sh          # reviews HEAD
#        ./review_last_commit.sh abc123   # reviews specific commit
#        ./review_last_commit.sh --json   # output raw JSONL events

set -e

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit 1

# Load API key from .env
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

# Parse arguments
JSON_MODE=false
COMMIT="HEAD"

for arg in "$@"; do
  case $arg in
    --json)
      JSON_MODE=true
      ;;
    *)
      COMMIT="$arg"
      ;;
  esac
done

SCHEMA_FILE="$SCRIPT_DIR/review_schema.json"
OUTPUT_FILE="$SCRIPT_DIR/review_output.json"

if [ "$JSON_MODE" = true ]; then
  # Raw JSONL mode for CI/CD parsing
  codex exec --commit "$COMMIT" --json \
    "Review git commit $COMMIT for bugs and security issues. Verify by reading actual code."
else
  # Human-readable mode with structured output
  echo "🔍 Reviewing commit: $COMMIT"
  echo ""
  
  codex exec \
    --output-schema "$SCHEMA_FILE" \
    -o "$OUTPUT_FILE" \
    "Review git commit $COMMIT. 
     
     For each issue:
     1. Read the actual code (not just diff) to verify
     2. Provide exact file:line_start-line_end
     3. Explain WHY it's a bug
     4. When appropriate, include a fix suggestion with code wrapped in markdown:
        \`\`\`language
        // suggested fix here
        \`\`\`
     
     Return empty reviews if no real issues. Quality over quantity."
  
  # Pretty print the output
  if command -v jq &> /dev/null && [ -f "$OUTPUT_FILE" ]; then
    echo ""
    echo "📋 Review Results:"
    echo "=================="
    jq '.' "$OUTPUT_FILE"
  elif [ -f "$OUTPUT_FILE" ]; then
    cat "$OUTPUT_FILE"
  fi
fi
