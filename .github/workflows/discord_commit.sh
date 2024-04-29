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
      "title": "Work Progress Report",
      "color": 3617648,
      "description": "We hereby report that the [**ImmersiveIntelligence_External**](https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence) has received the following new commits\\n$(git log "$COMMIT_FROM...$COMMIT_TO" --format="[\`%h\`](https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence/commit/%h) **%an**: %s%n%n%b\\n" | tr -d '\n' )",
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
