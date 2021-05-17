## What is `MelonBarPlayground`?
`MelonBarPlayground` is a package for testing things out to learn or just to have fun.
All example modules will be defined in `com.melonbar.playground.module.*`.
Currently, all testing must be done in a hacky fashion via modifying the main method in
`Playground.java` (the entrypoint), re-compiling, then running.

---
## How to get started
_NOTE: given that this package's purpose is experimentation, please protect your API credentials
and don't put any amount of money you're willing to lose on the line_

### First get Coinbase Pro API credentials:
1. To communicate via the HTTPS endpoint, API credentials are required for authentication
    * Create an API key on the [Coinbase Pro API page](https://pro.coinbase.com/profile/api)
    * You may skip everything if you only intend to use the websocket feed (meaning no orders may be placed)
2. Create `credentials.properties` in `src/main/resources` folder
3. Define fields: `api_key`, `api_password`, and `api_secret_key`, which you got from the Coinbase Pro website

`credentials.properties` should be ignored by default from `.gitignore`, but always double-check to make sure no
sensitive information is being pushed.

### Modules
1. Want to use an existing module?
    * Import any custom or pre-existing extension of `com.melonbar.playground.module.AbstractModule`
and invoke `run()`
2. Want to create a custom module?
    * Create your new module under `com.melonbar.playground.module.<MODULE_NAME>`
    * Extend `AbstractModule`, and implement methods `run()` and `init()`
      * `init()` is invoked _once_ per instance during initialization
    * Import your custom module to executable code and invoke via `run()`