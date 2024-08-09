# Usage: discord_commit <URL> <tail commit hash> <head commit hash>
run_link=https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence
modname="Immersive Intelligence"
cf_link="https://www.curseforge.com/minecraft/mc-mods/immersive-intelligence"
mr_link="https://modrinth.com/mod/immersive-intelligence"
gh_link="https://iiteam.net"
generate_post_data() {
cat <<EOF
{
  "content": "",
  "tts": false,
  "embeds": [
    {
      "id": 85085826,
      "description": "We are glad to inform that a new release of [**$modname:tm:**]($run_link) is available. \n\nIt can be downloaded through [Curseforge]($cf_link) and [Modrinth]($mr_link) where it is marked as an \`Beta Version\`.\n\nHave fun playing, feel free to use it in a modpack of yours and please report any bugs you encounter. ^^",
      "fields": [],
      "title": "New Release has been deployed!",
      "color": 3818346,
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
  "$1"