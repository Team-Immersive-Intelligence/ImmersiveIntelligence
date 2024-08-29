# Usage:
# COMMIT_FROM - Tail Commit hash
# COMMIT_TO - Head Commit hash
# WEBHOOK - Discord Webhook URL
# REPO - GH Repository name
# REPO_NAME - Human-readable Repository name

generate_post_data() {
cat <<EOF
{
  "content": "",
  "tts": false,
  "embeds": [
    {
      "id": 652627557,
      "title": "Work Progress Report",
      "color": 3617648,
      "description": "We hereby report that [**$REPO_NAME**](https://github.com/$REPO) has received following new commits\\n \
      $( git log $COMMIT_FROM...$COMMIT_TO --format="[\`%h\`](https://github.com/$REPO/commit/%h) %s\\n" | tr -d '\n' )",
      "color": 2631733,
      "fields": [],
      "footer": {
        "text": "Glory to the Engineer Cause!"
      }
    }
  ]
}
EOF
}
# POST request to Discord Webhook
curl \
  -H "Content-Type: application/json" \
  -X POST \
  -d "$(generate_post_data)" \
  "$WEBHOOK"