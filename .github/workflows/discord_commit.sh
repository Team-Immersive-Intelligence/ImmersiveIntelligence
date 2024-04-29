# Usage: discord_commit <URL> <tail commit hash> <head commit hash>
f=$COMMIT_FROM
t=$COMMIT_TO
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
      "description": "We hereby report that the [**Development**](https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence) has received following new commits\\n \
      $( git log $f...$t --format="[\`%h\`](https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence/commit/%h) %s\\n" | tr -d '\n' )",
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
