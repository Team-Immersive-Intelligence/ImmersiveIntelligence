# Secrets used in Immersive Intelligence Team's Repositories

## Secret Glossary
- `CARVERBOT_TOKEN` - token for the CarverBot GitHub bot account

- Webhooks
  - `WH_DEPLOY` - discord webhook for the developer server triggered after a deployment is made to any repository
  - `WH_RELEASE_DEV` - discord webhook for the public server triggered after a dev release is made
  - `WH_RELEASE` - discord webhook for the public server triggered after a release is made

  - `WH_PR_PRIVATE` - discord webhook for the developer server triggered after a pull request is made to any repository
  - `WH_PR_PUBLIC` - discord webhook for the public server triggered after a pull request is made to any repository

  - `WH_PUSH_PRIVATE` - discord webhook for the developer server triggered after a commit is pushed to any repository
  - `WH_PUSH_PUBLIC` - discord webhook for the public server triggered after a commit is pushed to any repository

  - `WH_TASK_ADDED` - discord webhook for the developer server triggered after an issue is added to the project
  - `WH_TASK_COMPLETE_PUBLIC` - discord webhook for the public server triggered after an issue is closed in the project
  - `WH_TASK_COMPLETE_PRIVATE` - discord webhook for the developer server triggered after an issue is closed in the project
  - `WH_TASK_REASSIGN` - discord webhook for the developer server triggered after an issue was set to "requesting review" in
the project

  - `WH_PUSH_RELEASE` - discord webhook for the public server triggered after a release is made
  - `WH_PUSH_PRIVATE` - discord webhook for the developer server triggered after a commit is pushed to any repository

- Code Signing
  - `CS_ALIAS` - alias for the code signing certificate
  - `CS_KEYSTORE` - keystore for the code signing certificate
  - `CS_KEY_PASS` - password for the keystore for the code signing certificate
  - `CS_PASS` - password for the code signing certificate
  - `CS_TIMESTAMP_URL` - URL for the timestamp server for the code signing certificate

- Deployment
  - `DEPT_CURSEFORGE` - DEPloyment Token for the CurseForge API
  - `DEPT_MODRINTH` - DEPloyment Token for the Modrinth API

- Maven
  - `MVN_PASS` - password for the II Team Maven repository
  - `MVN_USER` - username for the II Team Maven repository

## A repository should add the following:

- `CARVERBOT_TOKEN`
- `WH_DEPLOY`
- `WH_PR_PRIVATE`
- `WH_PUSH_PRIVATE`
- `WH_TASK_ADDED`
- `WH_TASK_COMPLETE_PRIVATE`
- `WH_TASK_REASSIGN`

## Organization (because of free tier) provides following:

- `CS_ALIAS`
- `CS_KEYSTORE`
- `CS_KEY_PASS`
- `CS_PASS`
- `CS_TIMESTAMP_URL`
- `DEPT_CURSEFORGE`
- `DEPT_MODRINTH`
- `MVN_PASS`
- `MVN_USER`
- `WH_PR_PUBLIC`
- `WH_PUSH_PUBLIC`
- `WH_RELEASE_DEV`
- `WH_RELEASE`
- `WH_TASK_COMPLETE_PUBLIC`