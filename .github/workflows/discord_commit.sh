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
      "description": "We hereby report that the [**External Repository**](https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence) has received following new commits",
      "fields": [
        {
          "name": "Commit Details",
          "value": "Author: $(git log --format='%an' | sed 's/"/\\"/g')\nBranch: $(git rev-parse --abbrev-ref HEAD)\nCommit name: $(git log --format='%s' | sed 's/"/\\"/g')\nCommit Description: $(git log --format='%b' | sed 's/"/\\"/g')"
        }
      ],
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
