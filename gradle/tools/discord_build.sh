# Usage: discord_commit <URL> <tail commit hash> <head commit hash>
run_link=https://github.com/Team-Immersive-Intelligence/ImmersiveIntelligence/actions/runs/6686602790
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
      "description": "You are granted access to a new, automatically compiled Dev Build of [**$modname:tm:**]($run_link). \n\nThis build can be downloaded through [Curseforge]($cf_link) and [Modrinth]($mr_link) where it is marked as an \`Alpha Version\`.\nAdditionally, the executable and sources are available as [Build Artifact]($gh_link)\n\nHappy testing and please report any bugs you encounter. ^^",
      "fields": [],
      "title": "Dev Build assembly completed!",
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